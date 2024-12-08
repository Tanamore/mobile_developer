package com.example.mytanamore.Ensiklopedia

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytanamore.api.ApiConfig
import com.example.mytanamore.response.DataItem
import com.example.mytanamore.response.SearchEnsiklopediaResponse
import kotlinx.coroutines.launch

class EnsiklopediaViewModel : ViewModel() {

    private val _ensiklopediaState = MutableLiveData<EnsiklopediaState?>()
    val ensiklopediaState: LiveData<EnsiklopediaState?> = _ensiklopediaState

    private val _ensiklopediaDetailState = MutableLiveData<EnsiklopediaDetailState?>()
    val ensiklopediaDetailState: LiveData<EnsiklopediaDetailState?> = _ensiklopediaDetailState

    private val _searchResults = MutableLiveData<SearchEnsiklopediaResponse?>()
    val searchResults: LiveData<SearchEnsiklopediaResponse?> = _searchResults

    private val apiService = ApiConfig.getApiService()

    fun fetchEnsiklopedia() {
        viewModelScope.launch {
            _ensiklopediaState.value = EnsiklopediaState.Loading
            try {
                val response = apiService.getEnsiklopedia()
                if (response.isSuccessful) {
                    Log.d("EnsiklopediaViewModel", "Response: ${response.body()}")
                    val plants = response.body()?.data?.filterNotNull()
                    Log.d("EnsiklopediaViewModel", "Number of plants: ${plants?.size}")
                    if (plants.isNullOrEmpty()) {
                        _ensiklopediaState.value = EnsiklopediaState.Error("No data available")
                    } else {
                        _ensiklopediaState.value = EnsiklopediaState.Success(plants)
                    }
                } else {
                    Log.e("EnsiklopediaViewModel", "Error: ${response.message()}")
                    _ensiklopediaState.value = EnsiklopediaState.Error("Failed to fetch data: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("EnsiklopediaViewModel", "Error fetching encyclopedias", e)
                _ensiklopediaState.value = EnsiklopediaState.Error("Error: ${e.message}")
            }
        }
    }

    fun fetchEnsiklopediaDetail(plantId: String) {
        viewModelScope.launch {
            _ensiklopediaDetailState.value = EnsiklopediaDetailState.Loading
            try {
                val response = apiService.getEnsiklopediaDetail(plantId)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        // Sesuaikan data dengan DataItem yang sesuai
                        _ensiklopediaDetailState.value = EnsiklopediaDetailState.Success(data)
                    } else {
                        _ensiklopediaDetailState.value = EnsiklopediaDetailState.Error("Story not found.")
                    }
                } else {
                    _ensiklopediaDetailState.value = EnsiklopediaDetailState.Error("Failed to fetch story detail: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("EnsiklopediaViewModel", "Error fetching story detail", e)
                _ensiklopediaDetailState.value = EnsiklopediaDetailState.Error("Error: ${e.message}")
            }
        }
    }

    fun searchEnsiklopedia(query: String) {
        _ensiklopediaState.value = EnsiklopediaState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.searchEnsiklopedia(query)
                if (response.isSuccessful) {
                    // Filter data null menjadi list yang hanya berisi non-null items
                    val nonNullData = response.body()?.data?.filterNotNull() ?: emptyList()
                    _searchResults.value = response.body()
                    _ensiklopediaState.value = EnsiklopediaState.Success(nonNullData) // Pastikan tipe data sesuai
                } else {
                    _ensiklopediaState.value = EnsiklopediaState.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _ensiklopediaState.value = EnsiklopediaState.Error("Exception: ${e.message}")
            }
        }
    }

    sealed class EnsiklopediaState {
        data class Success(val ensiklopedia: List<DataItem>) : EnsiklopediaState()
        data class Error(val message: String) : EnsiklopediaState()
        object Loading : EnsiklopediaState()
    }

    sealed class EnsiklopediaDetailState {
        data class Success(val ensiklopediaDetail: DataItem) : EnsiklopediaDetailState()
        data class Error(val message: String) : EnsiklopediaDetailState()
        object Loading : EnsiklopediaDetailState()
    }
}