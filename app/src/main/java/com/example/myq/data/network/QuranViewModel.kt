package com.example.myq.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.example.myq.data.model.Surah
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuranViewModel : ViewModel() {
    private val _surahs = MutableStateFlow<List<Surah>>(emptyList())
    val surahs: StateFlow<List<Surah>> = _surahs

    init {
        fetchSurahs()
    }

    private fun fetchSurahs() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getSurahs()
                _surahs.value = response
                Log.d("QuranViewModel", "Data Surah: $response")  // Cek data yang diterima
            } catch (e: Exception) {
                Log.e("QuranViewModel", "Error fetching surahs", e)
            }
        }
    }
}
