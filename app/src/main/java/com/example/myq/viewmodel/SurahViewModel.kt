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
    private var currentSurahNumber: Int? = null
    private var ayahQueue: List<String> = emptyList()
    private var currentAyahIndex: Int = 0

    // Flag untuk membedakan mode pemutaran surah penuh dan per ayat
    private var isSurahPlaying: Boolean = false

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

    fun fetchSurahData(surahNumber: Int) {
        viewModelScope.launch {
            try {
                Log.d("SurahViewModel", "Fetching data for surah $surahNumber")
                // Panggil fetchSurahAudio terlebih dahulu untuk memastikan audio tersedia
                val audioResponse = ApiClient.apiService.getSurahAudio(surahNumber)
                Log.d("SurahViewModel", "Audio response: ${audioResponse.data.ayahs}")
                val audioAyahs = audioResponse.data.ayahs.associateBy { it.number }

                // Kemudian ambil detail surah
                val detailResponse = ApiClient.apiService.getSurahDetail(surahNumber)
                val newAyahs = detailResponse.data.ayahs.map { detailAyah ->
                    val audioAyah = audioAyahs[detailAyah.number]
                    Ayah(
                        number = detailAyah.number,
                        text = detailAyah.text,
                        audio = audioAyah?.audio
                    )
                }
                _ayahList.value = newAyahs
                Log.d("SurahViewModel", "Fetched ayahs for surah $surahNumber: ${newAyahs.map { it.audio }}")

                // Terakhir, ambil terjemahan
                val translationResponse = ApiClient.apiService.getSurahTranslation(surahNumber)
                _translationList.value = translationResponse.data.ayahs
            } catch (e: Exception) {
                Log.e("SurahViewModel", "Error fetching surah data: ${e.message}")
                _ayahList.value = emptyList()
                _translationList.value = emptyList()
            }
        }
    }

    fun playAudio(audioUrl: String?, ayahNumber: Int? = null) {
        viewModelScope.launch {
            if (audioUrl.isNullOrEmpty()) {
                Log.e("SurahViewModel", "Audio URL is null or empty for ayah $ayahNumber")
                return@launch
            }
            // Jika sedang dalam mode surah dan chaining (ayahNumber == null),
            // cukup lepas mediaPlayer tanpa mereset antrian
            if (!(isSurahPlaying && ayahNumber == null)) {
                stopAudio() // Untuk mode individual, reset state terlebih dahulu
            } else {
                mediaPlayer?.let {
                    try {
                        if (it.isPlaying) it.stop()
                        it.reset()
                        it.release()
                    } catch (e: Exception) {
                        Log.e("SurahViewModel", "Error releasing mediaPlayer: ${e.message}")
                    }
                }
                mediaPlayer = null
            }
            try {
                Log.d("SurahViewModel", "Preparing MediaPlayer for audio: $audioUrl, ayah: $ayahNumber")
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(audioUrl)
                    setOnPreparedListener {
                        Log.d("SurahViewModel", "MediaPlayer prepared for audio: $audioUrl")
                        start()
                        _isPlaying.value = true
                        _currentAyah.value = ayahNumber
                    }
                    setOnCompletionListener {
                        Log.d("SurahViewModel", "Audio completed for ayah $ayahNumber, currentAyahIndex: $currentAyahIndex, queueSize: ${ayahQueue.size}")
                        // Jika mode surah aktif dan masih ada audio berikutnya, lanjutkan chaining
                        if (isSurahPlaying && currentAyahIndex < ayahQueue.size - 1) {
                            currentAyahIndex++
                            Log.d("SurahViewModel", "Playing next ayah in queue: ${ayahQueue[currentAyahIndex]}")
                            playAudio(ayahQueue[currentAyahIndex], null)
                        } else {
                            Log.d("SurahViewModel", "No more ayahs to play or non-surah mode, stopping")
                            isSurahPlaying = false
                            stopAudio()
                        }
                    }
                    setOnErrorListener { _, what, extra ->
                        Log.e("SurahViewModel", "MediaPlayer error for ayah $ayahNumber: what=$what, extra=$extra")
                        stopAudio()
                        true
                    }
                    prepareAsync()
                }
            } catch (e: Exception) {
                Log.e("SurahViewModel", "Error preparing audio for ayah $ayahNumber: ${e.message}")
                _isPlaying.value = false
                _currentAyah.value = null
            }
        }
    }

    fun playSurahAudio(surahNumber: Int) {
        viewModelScope.launch {
            try {
                Log.d("SurahViewModel", "Starting playSurahAudio for surah $surahNumber")
                isSurahPlaying = true // Aktifkan mode surah
                val ayahs = _ayahList.value
                if (ayahs.isEmpty()) {
                    Log.e("SurahViewModel", "No ayahs found for surah $surahNumber")
                    return@launch
                }
                ayahQueue = ayahs.mapNotNull { it.audio }
                currentAyahIndex = 0
                currentSurahNumber = surahNumber
                Log.d("SurahViewModel", "Ayah queue: $ayahQueue")
                if (ayahQueue.isNotEmpty()) {
                    playAudio(ayahQueue[0], null)
                } else {
                    Log.e("SurahViewModel", "No audio URLs found for surah $surahNumber")
                }
            } catch (e: Exception) {
                Log.e("SurahViewModel", "Error in playSurahAudio: ${e.message}")
                _isPlaying.value = false
                _currentAyah.value = null
                isSurahPlaying = false
            }
        }
    }

    fun pauseAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                try {
                    Log.d("SurahViewModel", "Pausing audio")
                    it.pause()
                    _isPlaying.value = false
                } catch (e: IllegalStateException) {
                    Log.e("SurahViewModel", "Error pausing audio: ${e.message}")
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
                    Log.d("SurahViewModel", "Resuming audio")
                    it.start()
                    _isPlaying.value = true
                } catch (e: IllegalStateException) {
                    Log.e("SurahViewModel", "Error resuming audio: ${e.message}")
                }
            }
        } ?: run {
            Log.w("SurahViewModel", "MediaPlayer is null, cannot resume")
        }
    }

    fun stopAudio() {
        mediaPlayer?.let {
            try {
                Log.d("SurahViewModel", "Stopping audio")
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
            } catch (e: IllegalStateException) {
                Log.e("SurahViewModel", "Error stopping audio: ${e.message}")
            }
        }
        mediaPlayer = null
        _isPlaying.value = false
        _currentAyah.value = null
        currentSurahNumber = null
        ayahQueue = emptyList()
        currentAyahIndex = 0
        isSurahPlaying = false // Reset flag mode surah
        Log.d("SurahViewModel", "Audio stopped, state reset")
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }
}
