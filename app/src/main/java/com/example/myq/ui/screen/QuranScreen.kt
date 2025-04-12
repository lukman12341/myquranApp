package com.example.myq.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myq.data.network.QuranViewModel
import com.example.myq.data.model.Surah

@Composable
fun QuranScreen(quranViewModel: QuranViewModel = viewModel()) {
    // Mengambil data surah menggunakan collectAsState()
    val surahs = quranViewModel.surahs.collectAsState(initial = emptyList()).value

    // Menampilkan daftar surah dengan Juz dan Zuz
    Column(modifier = Modifier.padding(PaddingValues(16.dp))) {
        surahs.forEach { surah ->
            // Menghitung Zuz berdasarkan Juz
            val zuz = calculateZuz(surah.juz)
            // Menampilkan Surah, Juz, dan Zuz
            Text(text = "Surah: ${surah.surah}") // Nama Surah
            Text(text = "Juz: ${surah.juz}")     // Juz Surah
            Text(text = "Zuz: $zuz")             // Zuz yang dihitung
        }
    }
}

// Fungsi untuk menghitung Zuz berdasarkan Juz
fun calculateZuz(juz: Int): Int {
    return ((juz - 1) / 2) + 1  // Pembagian Zuz per 2 juz
}
