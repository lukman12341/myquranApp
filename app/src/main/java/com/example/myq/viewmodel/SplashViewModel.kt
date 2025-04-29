package com.example.myq.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SplashViewModel : ViewModel() {
    private val _showSplash = MutableStateFlow(true)
    val showSplash: StateFlow<Boolean> get() = _showSplash

    fun hideSplash() {
        _showSplash.value = false
    }
}