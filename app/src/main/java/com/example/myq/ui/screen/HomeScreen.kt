package com.example.myq.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myq.data.model.Surah
import com.example.myq.viewmodel.SurahViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: SurahViewModel = viewModel()) {
    val surahList by viewModel.surahList.collectAsState()

    // Ambil list surah ketika pertama kali tampil
    LaunchedEffect(Unit) {
        viewModel.fetchSurahList()
    }

    LazyColumn {
        items(surahList) { surah ->
            ListItem(
                headlineContent = {
                    // Tampilkan nama surah dalam bahasa Arab dan Latin secara berurutan
                    Column {
                        // Nama surah dalam Bahasa Arab (right aligned)
                        Text(
                            text = surah.name,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        // Nama surah dalam Bahasa Latin
                        Text(
                            text = surah.englishName,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                supportingContent = {
                    // Tampilkan arti/terjemahan dan tipe wahyu
                    Text(text = "${surah.englishNameTranslation} - ${surah.revelationType}")
                },
                trailingContent = {
                    // Tampilkan jumlah ayat
                    Text(text = "${surah.numberOfAyahs} ayat")
                },
                modifier = Modifier
                    .clickable {
                        navController.navigate("surahDetail/${surah.number}")
                    }
                    .padding(vertical = 8.dp)
            )
        }
    }
}
