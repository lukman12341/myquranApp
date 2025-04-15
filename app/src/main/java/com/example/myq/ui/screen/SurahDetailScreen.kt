package com.example.myq.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myq.data.model.Ayah
import com.example.myq.data.model.AyahTranslation
import com.example.myq.ui.theme.ThemeState
import com.example.myq.viewmodel.SurahViewModel
import kotlinx.coroutines.delay

@Composable
fun SurahDetailScreen(
    surahNumber: Int,
    viewModel: SurahViewModel = viewModel()
) {
    val ayahList by viewModel.ayahList.collectAsState()
    val translationList by viewModel.translationList.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentAyah by viewModel.currentAyah.collectAsState()
    val isDarkTheme = ThemeState.isDarkTheme
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(surahNumber) {
        isLoading = true
        viewModel.fetchSurahData(surahNumber)
        delay(500) // Beri waktu untuk memuat data
        isLoading = false
    }

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Column {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Kontrol Audio Surah
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Putar Surah",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Row {
                            IconButton(
                                onClick = {
                                    Log.d("SurahDetailScreen", "Play/Pause Surah clicked for surah $surahNumber")
                                    if (isPlaying && currentAyah == null) {
                                        viewModel.pauseAudio()
                                    } else {
                                        viewModel.playSurahAudio(surahNumber)
                                    }
                                },
                                enabled = ayahList.isNotEmpty()
                            ) {
                                Icon(
                                    imageVector = if (isPlaying && currentAyah == null) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                    contentDescription = if (isPlaying && currentAyah == null) "Jeda Surah" else "Putar Surah"
                                )
                            }
                            IconButton(
                                onClick = {
                                    Log.d("SurahDetailScreen", "Stop Surah clicked for surah $surahNumber")
                                    viewModel.stopAudio()
                                },
                                enabled = isPlaying && currentAyah == null
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Stop,
                                    contentDescription = "Hentikan Surah"
                                )
                            }
                        }
                    }
                }

                // Daftar Ayat dengan Kontrol Audio
                LazyColumn {
                    items(ayahList.zip(translationList)) { pair ->
                        val (ayah, translation) = pair
                        AyahItem(
                            ayah = ayah,
                            translation = translation,
                            isPlaying = isPlaying && currentAyah == ayah.number,
                            onPlay = {
                                Log.d("SurahDetailScreen", "Play clicked for ayah ${ayah.number}, audio: ${ayah.audio}")
                                viewModel.playAudio(ayah.audio, ayah.number)
                            },
                            onPause = {
                                Log.d("SurahDetailScreen", "Pause clicked for ayah ${ayah.number}")
                                viewModel.pauseAudio()
                            },
                            onStop = {
                                Log.d("SurahDetailScreen", "Stop clicked for ayah ${ayah.number}")
                                viewModel.stopAudio()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AyahItem(
    ayah: Ayah,
    translation: AyahTranslation,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = ayah.text,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = translation.text,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        Log.d("AyahItem", "Play/Pause clicked for ayah ${ayah.number}, audio: ${ayah.audio}, isPlaying: $isPlaying")
                        if (isPlaying) {
                            onPause()
                        } else {
                            onPlay()
                        }
                    },
                    enabled = ayah.audio != null
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Jeda Ayat" else "Putar Ayat"
                    )
                }
                IconButton(
                    onClick = {
                        Log.d("AyahItem", "Stop clicked for ayah ${ayah.number}")
                        onStop()
                    },
                    enabled = isPlaying
                ) {
                    Icon(
                        imageVector = Icons.Filled.Stop,
                        contentDescription = "Hentikan Ayat"
                    )
                }
            }
        }
    }
}
