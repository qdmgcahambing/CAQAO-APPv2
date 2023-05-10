package com.example.caqao.caqaodetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caqao.models.CaqaoApiStatus
import com.example.caqao.models.USER_TOKEN
import com.example.caqao.network.CacaoApi
import com.example.caqao.network.CacaoDetection
import kotlinx.coroutines.launch


class CacaoDetailViewModel(
    private val cacaoDetectionId: Int = 0) : ViewModel() {

    private val _status = MutableLiveData<CaqaoApiStatus>()
    val status: LiveData<CaqaoApiStatus> = _status

    private val _cacaoDetection = MutableLiveData<CacaoDetection>()
    val cacaoDetection: LiveData<CacaoDetection>
        get() = _cacaoDetection

    private val _detections = MutableLiveData<List<CacaoDetection>>()
    val detections: LiveData<List<CacaoDetection>> = _detections

    private val _detectionStatus = MutableLiveData<String>()
    val detectionStatus: LiveData<String> = _detectionStatus

    init {
        // GET request to get data from the detections db using cacaoDetectionId
        getCacaoDetectionWithId(cacaoDetectionId)
    }

    private val _navigateToGalleryFragment = MutableLiveData<Boolean?>()
    /**
     * When true immediately navigate back to the [GalleryFragment]
     */
    val navigateToGalleryFragment: LiveData<Boolean?>
        get() = _navigateToGalleryFragment


    fun getCacaoDetections() {
        viewModelScope.launch {
            try {
                _detections.value = USER_TOKEN?.let { CacaoApi.retrofitService.getDetections(it) }
                _detectionStatus.value = "Success: CAQAO detections retrieved"
            } catch (e: Exception) {
                _detectionStatus.value = "Failure: ${e.message}"
            }
        }
    }

    fun getCacaoDetectionWithId(cacaoDetectionId: Int) {
        viewModelScope.launch {
            _status.value = CaqaoApiStatus.LOADING
            try {
                _cacaoDetection.value = USER_TOKEN?.let {
                    CacaoApi.retrofitService.getDetectionWithId(
                        it, cacaoDetectionId)
                }
                _status.value = CaqaoApiStatus.DONE
                Log.d("GetByIDSuccess", "Bean Grade: ${_cacaoDetection.value!!.beanGrade}")
            } catch (e: Exception) {
                _status.value = CaqaoApiStatus.ERROR
                Log.d("GetByIDFailed", "Error: ${e}")
            }
        }
    }

    fun deleteCacaoDetectionWithId(cacaoDetectionId: Int) {
        viewModelScope.launch {
            try {
                com.example.caqao.models.USER_TOKEN?.let {
                    CacaoApi.retrofitService.deleteDetectionWithId(
                        it, cacaoDetectionId)
                }
            } catch (e: Exception) {
                Log.d("DeleteByIDFailed", "Error: ${e}")
            }
        }
    }

    /**
     * Call this immediately after navigating to [GalleryFragment]
     */
    fun doneNavigating() {
        _navigateToGalleryFragment.value = null
    }

    fun onClose() {
        _navigateToGalleryFragment.value = true
    }

}