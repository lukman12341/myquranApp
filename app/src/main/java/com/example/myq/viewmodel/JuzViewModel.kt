package com.example.myq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myq.data.model.AyatDetail
import com.example.myq.data.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JuzViewModel(private val apiService: ApiService) : ViewModel() {
    private val _daftarJuz = MutableStateFlow<List<Int>>(emptyList())
    val daftarJuz: StateFlow<List<Int>> = _daftarJuz

    private val _ayatDetails = MutableStateFlow<List<AyatDetail>>(emptyList())
    val ayatDetails: StateFlow<List<AyatDetail>> = _ayatDetails

    private val _sedangMemuat = MutableStateFlow(false)
    val sedangMemuat: StateFlow<Boolean> = _sedangMemuat

    val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun ambilDaftarJuz() {
        viewModelScope.launch {
            _sedangMemuat.value = true
            try {
                _daftarJuz.value = (1..30).toList()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat daftar Juz: ${e.message}"
            } finally {
                _sedangMemuat.value = false
            }
        }
    }

    fun ambilDetailJuzDanTerjemahan(nomorJuz: Int) {
        viewModelScope.launch {
            _sedangMemuat.value = true
            try {
                val arabResponse = apiService.getDetailJuz(nomorJuz)
                val terjemahanResponse = apiService.getTerjemahanJuz(nomorJuz)
                val ayatArab = arabResponse.data.ayahs
                val ayatTerjemahan = terjemahanResponse.data.ayahs

                // Logging untuk debugging
                println("Juz $nomorJuz: Jumlah ayat Arab = ${ayatArab.size}, Jumlah ayat Terjemahan = ${ayatTerjemahan.size}")

                if (ayatArab.isEmpty() || ayatTerjemahan.isEmpty()) {
                    _errorMessage.value = "Data ayat kosong untuk Juz $nomorJuz"
                    _ayatDetails.value = emptyList()
                    return@launch
                }

                // Cocokkan ayat berdasarkan numberInSurah dan surah number
                val ayatDetails = ayatArab.mapNotNull { arab ->
                    val terjemahan = ayatTerjemahan.find {
                        it.number == arab.numberInSurah && arab.surah.number == arab.surah.number/* surah number dari terjemahan, jika ada */
                    }
                    if (terjemahan != null) {
                        AyatDetail(
                            surahNumber = arab.surah.number,
                            surahName = arab.surah.name,
                            ayahNumberInSurah = arab.numberInSurah,
                            teksArab = arab.text,
                            terjemahan = terjemahan.text
                        )
                    } else {
                        println("Terjemahan tidak ditemukan untuk ayat ${arab.numberInSurah} di surah ${arab.surah.number}")
                        null
                    }
                }

                if (ayatDetails.isEmpty()) {
                    _errorMessage.value = "Tidak ada ayat yang cocok untuk Juz $nomorJuz"
                }
                _ayatDetails.value = ayatDetails
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat detail Juz $nomorJuz: ${e.message}"
                println("Error: ${e.message}")
                _ayatDetails.value = emptyList()
            } finally {
                _sedangMemuat.value = false
            }
        }
    }
}