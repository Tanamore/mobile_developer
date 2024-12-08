package com.example.mytanamore.ScanTanaman

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mytanamore.R
import com.example.mytanamore.api.ApiConfig
import com.example.mytanamore.databinding.ActivityScanTanamanBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class ScanTanamanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanTanamanBinding
    private lateinit var viewModel: ScanTanamanViewModel
    private lateinit var imageCapture: ImageCapture
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.setImageUri(it) }
    }

    private val CAMERA_PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanTanamanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ScanTanamanViewModel::class.java)

        checkCameraPermission()

        binding.buttonGallery.setOnClickListener { openGallery() }
        binding.buttonShutter.setOnClickListener { takePhoto() }
        binding.buttonAnalyze.setOnClickListener { analyzeImage() }

        viewModel.imageUri.observe(this) { uri ->
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            setupCamera()
        }
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                preview.setSurfaceProvider(binding.previewView.surfaceProvider)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error initializing camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val file = File(externalCacheDir, "${System.currentTimeMillis()}.jpg")
            val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val uri = Uri.fromFile(file)
                        viewModel.setImageUri(uri)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(this@ScanTanamanActivity, "Error capturing image: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        } else {
            Toast.makeText(this, "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun analyzeImage() {
        val uri = viewModel.imageUri.value
        if (uri == null) {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show()
            return
        }

        val file = uriToFile(uri)
        if (file == null) {
            Toast.makeText(this, "Error processing image file", Toast.LENGTH_SHORT).show()
            return
        }

        val apiService = ApiConfig.getApiService()
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

        lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                val response = apiService.uploadImageEnsiklopedia(imagePart)
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val predictEnsiklopediaResponse = response.body()

                    // Log the entire response for debugging
                    Log.d("AnalyzeImage", "Response: ${predictEnsiklopediaResponse?.data}")

                    // Extract the necessary data from the response
                    val result = predictEnsiklopediaResponse?.data?.result ?: "No result"
                    val confidence = predictEnsiklopediaResponse?.data?.confidence ?: "Unknown confidence"
                    val plantInfo = predictEnsiklopediaResponse?.data?.plantInfo

                    // Log or check the values
                    Log.d("AnalyzeImage", "Result: $result, Confidence: $confidence, PlantInfo: $plantInfo")

                    val intent = Intent(this@ScanTanamanActivity, DetailTanamanActivity::class.java).apply {
                        intent.putExtra("ANALYSIS_RESULT", result)
                        intent.putExtra("CONFIDENCE", confidence)
                        plantInfo?.let {
                            intent.putExtra("PLANT_INFO", it)
                        }
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this@ScanTanamanActivity, "Analysis failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AnalyzeImage", "Error: ${e.message}", e)
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@ScanTanamanActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uriToFile(uri: Uri): File? {
        val contentResolver = contentResolver
        val tempFile = File(cacheDir, "temp_image.jpg")
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
                Toast.makeText(this, "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show()
            }
        }
    }
}