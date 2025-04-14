package com.example.myq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myq.data.model.Ayah
import com.example.myq.data.model.AyahTranslation
import com.example.myq.data.model.Surah
import com.example.myq.data.network.ApiClient
import com.example.myq.data.repository.SurahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SurahViewModel : ViewModel() {
    private val repository: SurahRepository = SurahRepository()

    private val _surahList = MutableStateFlow<List<Surah>>(emptyList())
    val surahList: StateFlow<List<Surah>> = _surahList

    private val _ayahList = MutableStateFlow<List<Ayah>>(emptyList())
    val ayahList: StateFlow<List<Ayah>> = _ayahList

    private val _translationList = MutableStateFlow<List<AyahTranslation>>(emptyList())
    val translationList: StateFlow<List<AyahTranslation>> = _translationList

    fun fetchSurahList() {
        viewModelScope.launch {
            try {
                val surahs = repository.getAllSurah()
                _surahList.value = surahs
            } catch (e: Exception) {
                // Handle error
                _surahList.value = emptyList() // Bisa tambahkan error handling di sini
            }
        }
    }

    fun fetchSurahDetail(surahNumber: Int) {
        viewModelScope.launch {
            val response = ApiClient.apiService.getSurahDetail(surahNumber)
            _ayahList.value = response.data.ayahs
        }
    }

    fun fetchSurahTranslation(surahNumber: Int) {
        viewModelScope.launch {
            val response = ApiClient.apiService.getSurahTranslation(surahNumber)
            _translationList.value = response.data.ayahs
        }
    }
}
