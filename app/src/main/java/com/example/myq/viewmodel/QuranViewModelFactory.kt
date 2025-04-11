package com.example.myq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myq.data.repository.QuranRepository
import com.example.myq.ui.QuranViewModel

class QuranViewModelFactory(
    private val repository: QuranRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuranViewModel::class.java)) {
            return QuranViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}