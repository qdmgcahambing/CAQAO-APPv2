package com.example.caqao.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caqao.network.GalleryApi
import com.example.caqao.network.GalleryPhoto
import kotlinx.coroutines.launch

enum class GalleryApiStatus { LOADING, ERROR, DONE }

class GalleryViewModel : ViewModel() {
    private val _status = MutableLiveData<GalleryApiStatus>()
    val status: LiveData<GalleryApiStatus> = _status
    private val _photos = MutableLiveData<List<GalleryPhoto>>()
    val photos: LiveData<List<GalleryPhoto>> = _photos

    init {
        getGalleryPhotos()
    }

    private fun getGalleryPhotos() {
        viewModelScope.launch {
            _status.value = GalleryApiStatus.LOADING
            try{
                _photos.value = GalleryApi.retrofitService.getPhotos()
                _status.value = GalleryApiStatus.DONE
            } catch (e: Exception){
                _status.value = GalleryApiStatus.ERROR
                _photos.value = listOf()
            }

        }
    }
}