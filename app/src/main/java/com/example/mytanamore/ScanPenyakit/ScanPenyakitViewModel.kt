package com.example.mytanamore.ScanPenyakit

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScanPenyakitViewModel : ViewModel() {

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    private val _analysisResult = MutableLiveData<String?>()
    val analysisResult: LiveData<String?> = _analysisResult

    private val _diseaseInfo = MutableLiveData<String?>()
    val diseaseInfo: LiveData<String?> = _diseaseInfo

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun setAnalysisResult(result: String?) {
        _analysisResult.value = result
    }

    fun setDiseaseInfo(info: String?) {
        _diseaseInfo.value = info
    }
}