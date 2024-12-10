package com.example.mytanamore.ScanTanaman

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mytanamore.response.PlantInfo

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
