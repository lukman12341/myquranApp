package com.example.myq.viewmodel

import android.media.MediaPlayer
import android.util.Log
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

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentAyah = MutableStateFlow<Int?>(null)
    val currentAyah: StateFlow<Int?> = _currentAyah

    private var mediaPlayer: MediaPlayer? = null

    fun fetchSurahList() {
        viewModelScope.launch {
            try {
                val surahs = repository.getAllSurah()
                _surahList.value = surahs
            } catch (e: Exception) {
                Log.e("SurahViewModel", "Error fetching surah list", e)
                _surahList.value = emptyList()
            }
        }
    }

    fun fetchSurahDetail(surahNumber: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getSurahDetail(surahNumber)
                _ayahList.value = response.data.ayahs
            } catch (e: Exception) {
                Log.e("SurahViewModel", "Error fetching surah detail", e)
                _ayahList.value = emptyList()
            }
        }
    }

    fun fetchSurahTranslation(surahNumber: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getSurahTranslation(surahNumber)
                _translationList.value = response.data.ayahs
            } catch (e: Exception) {
                Log.e("SurahViewModel", "Error fetching translation", e)
                _translationList.value = emptyList()
            }
        }
    }

    fun fetchSurahAudio(surahNumber: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getSurahAudio(surahNumber)
                _ayahList.value = response.data.ayahs
            } catch (e: Exception) {
                Log.e("SurahViewModel", "Error fetching audio", e)
                _ayahList.value = emptyList()
            }
        }
    }

    fun playAudio(audioUrl: String?, ayahNumber: Int? = null) {
        viewModelScope.launch {
            if (audioUrl.isNullOrEmpty()) {
                Log.e("SurahViewModel", "Audio URL is null or empty")
                return@launch
            }
            stopAudio() // Stop any existing playback
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(audioUrl)
                    prepare()
                    start()
                    _isPlaying.value = true
                    _currentAyah.value = ayahNumber
                    setOnCompletionListener {
                        stopAudio()
                    }
                    setOnErrorListener { _, what, extra ->
                        Log.e("SurahViewModel", "MediaPlayer error: what=$what, extra=$extra")
                        stopAudio()
                        true
                    }
                }
            } catch (e: Exception) {
                Log.e("SurahViewModel", "Error playing audio", e)
                _isPlaying.value = false
                _currentAyah.value = null
            }
        }
    }

    fun playSurahAudio(surahNumber: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getSurahAudio(surahNumber)
                val surahAudio = response.data.ayahs.firstOrNull()?.audio?.replace(Regex("\\d+\\.mp3$"), ".mp3")
                if (surahAudio.isNullOrEmpty()) {
                    Log.e("SurahViewModel", "Surah audio URL is null or empty")
                    return@launch
                }
                playAudio(surahAudio, null)
            } catch (e: Exception) {
                Log.e("SurahViewModel", "Error fetching surah audio", e)
            }
        }
    }

    fun pauseAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                try {
                    it.pause()
                    _isPlaying.value = false
                } catch (e: IllegalStateException) {
                    Log.e("SurahViewModel", "Error pausing audio", e)
                }
            }
        } ?: run {
            Log.w("SurahViewModel", "MediaPlayer is null, cannot pause")
            _isPlaying.value = false
        }
    }

    fun resumeAudio() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                try {
                    it.start()
                    _isPlaying.value = true
                } catch (e: IllegalStateException) {
                    Log.e("SurahViewModel", "Error resuming audio", e)
                }
            }
        } ?: run {
            Log.w("SurahViewModel", "MediaPlayer is null, cannot resume")
        }
    }

    fun stopAudio() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
            } catch (e: IllegalStateException) {
                Log.e("SurahViewModel", "Error stopping audio", e)
            }
        }
        mediaPlayer = null
        _isPlaying.value = false
        _currentAyah.value = null
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }
}