package com.example.myq.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myq.data.model.Ayah
import com.example.myq.data.model.Surah
import com.example.myq.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuranViewModel(private val repository: QuranRepository) : ViewModel() {

    private val _surahList = MutableStateFlow<List<Surah>>(emptyList())
    val surahList: StateFlow<List<Surah>> = _surahList

    private val _ayahList = MutableStateFlow<List<Ayah>>(emptyList())
    val ayahList: StateFlow<List<Ayah>> = _ayahList

    fun fetchSurahList() {
        viewModelScope.launch {
            _surahList.value = repository.getSurahList().data
        }
    }

    fun fetchSurahDetail(number: Int) {
        viewModelScope.launch {
            _ayahList.value = repository.getSurahDetail(number).data.ayahs
        }
    }
}
