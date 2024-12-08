package com.example.mytanamore.ScanTanaman

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytanamore.api.ApiConfig
import com.example.mytanamore.response.PlantInfo
import com.example.mytanamore.response.PredictEnsiklopediaResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ScanTanamanViewModel : ViewModel() {

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> get() = _imageUri

    private val _analysisResult = MutableLiveData<String?>()
    val analysisResult: LiveData<String?> get() = _analysisResult

    private val _plantInfo = MutableLiveData<PlantInfo?>()
    val plantInfo: LiveData<PlantInfo?> get() = _plantInfo

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun setAnalysisResult(result: String?) {
        _analysisResult.value = result
    }

    fun setPlantInfo(info: PlantInfo?) {
        _plantInfo.value = info
    }

}
