package com.example.myq.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myq.viewmodel.SurahViewModel

@Composable
fun SurahDetailScreen(
    surahNumber: Int,
    viewModel: SurahViewModel = viewModel()
) {
    val ayahList by viewModel.ayahList.collectAsState()
    val translationList by viewModel.translationList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchSurahDetail(surahNumber)
        viewModel.fetchSurahTranslation(surahNumber)
    }

    LazyColumn {
        items(ayahList.zip(translationList)) { pair ->
            val (ayah, translation) = pair
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = ayah.text,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = translation.text,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}