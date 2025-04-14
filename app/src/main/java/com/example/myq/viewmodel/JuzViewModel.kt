// JuzViewModel.kt
package com.example.myq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myq.data.model.JuzResponse
import com.example.myq.data.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JuzViewModel(private val apiService: ApiService) : ViewModel() {
    private val _daftarJuz = MutableStateFlow<List<Int>>(emptyList())
    val daftarJuz: StateFlow<List<Int>> = _daftarJuz

    private val _detailJuz = MutableStateFlow<JuzResponse?>(null)
    val detailJuz: StateFlow<JuzResponse?> = _detailJuz

    private val _sedangMemuat = MutableStateFlow(false)
    val sedangMemuat: StateFlow<Boolean> = _sedangMemuat

    fun ambilDaftarJuz() {
        viewModelScope.launch {
            _sedangMemuat.value = true
            try {
                // Nomor Juz adalah 1-30
                _daftarJuz.value = (1..30).toList()
            } catch (e: Exception) {
                // Tangani error
            } finally {
                _sedangMemuat.value = false
            }
        }
    }

    fun ambilDetailJuz(nomorJuz: Int) {
        viewModelScope.launch {
            _sedangMemuat.value = true
            try {
                val response = apiService.getDetailJuz(nomorJuz)
                _detailJuz.value = response
            } catch (e: Exception) {
                // Tangani error
            } finally {
                _sedangMemuat.value = false
            }
        }
    }
}