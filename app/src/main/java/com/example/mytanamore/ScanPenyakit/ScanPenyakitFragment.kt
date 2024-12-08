package com.example.mytanamore.ScanPenyakit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.mytanamore.api.ApiConfig
import com.example.mytanamore.databinding.FragmentScanPenyakitBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class ScanPenyakitFragment : Fragment() {

    private lateinit var viewModel: ScanPenyakitViewModel
    private lateinit var binding: FragmentScanPenyakitBinding
    private lateinit var imageCapture: ImageCapture
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.setImageUri(it) }
    }

    private val CAMERA_PERMISSION_REQUEST_CODE = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanPenyakitBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ScanPenyakitViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkCameraPermission()

        binding.buttonGallery.setOnClickListener { openGallery() }
        binding.buttonShutter.setOnClickListener { takePhoto() }
        binding.buttonAnalyze.setOnClickListener { analyzeImage() }

        viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                binding.previewView.visibility = View.GONE
                binding.imagePreview.visibility = View.VISIBLE
                Glide.with(this)
                    .load(uri)
                    .into(binding.imagePreview)
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            setupCamera()
        }
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                preview.setSurfaceProvider(binding.previewView.surfaceProvider)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error initializing camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val file = File(requireContext().externalCacheDir, "${System.currentTimeMillis()}.jpg")
            val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val uri = Uri.fromFile(file)
                        viewModel.setImageUri(uri)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(requireContext(), "Error capturing image: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        } else {
            Toast.makeText(requireContext(), "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun analyzeImage() {
        val uri = viewModel.imageUri.value
        if (uri == null) {
            Toast.makeText(requireContext(), "No image selected!", Toast.LENGTH_SHORT).show()
            return
        }

        val file = uriToFile(uri)
        if (file == null) {
            Toast.makeText(requireContext(), "Error processing image file", Toast.LENGTH_SHORT).show()
            return
        }

        val apiService = ApiConfig.getApiService()
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                val response = apiService.uploadImage(imagePart)
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val deteksiPenyakitResponse = response.body() // Response with DeteksiPenyakitResponse

                    // Extract the necessary data from the response
                    val result = deteksiPenyakitResponse?.data?.result ?: "No result"
                    val confidence = deteksiPenyakitResponse?.data?.confidence ?: "Unknown confidence"
                    val diseaseInfo = deteksiPenyakitResponse?.data?.diseaseInfo

                    // Log or check the values
                    Log.d("AnalyzeImage", "Result: $result, Confidence: $confidence")

                    // Create intent to pass data to the next activity
                    val intent = Intent(requireContext(), DetailPenyakitActivity::class.java).apply {
                        putExtra("ANALYSIS_RESULT", result)
                        putExtra("CONFIDENCE", confidence)
                        putExtra("DISEASE_INFO", diseaseInfo) // Passing Parcelable DiseaseInfo
                        putExtra("IMAGE_URI", uri.toString())
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Analysis failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun uriToFile(uri: Uri): File? {
        val contentResolver = requireContext().contentResolver
        val tempFile = File(requireContext().cacheDir, "temp_image.jpg")
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return tempFile
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show()
            }
        }
    }
}