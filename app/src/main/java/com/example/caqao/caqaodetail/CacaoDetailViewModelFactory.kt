package com.example.caqao.caqaodetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CacaoDetailViewModelFactory(
    private val cacaoDetectionId: Int) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CacaoDetailViewModel::class.java)) {
            return CacaoDetailViewModel(cacaoDetectionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}