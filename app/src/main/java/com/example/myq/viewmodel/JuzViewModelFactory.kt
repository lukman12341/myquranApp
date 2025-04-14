// JuzViewModelFactory.kt
package com.example.myq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myq.data.network.ApiService

class JuzViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JuzViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JuzViewModel(apiService) as T
        }
        throw IllegalArgumentException("ViewModel class tidak diketahui")
    }
}