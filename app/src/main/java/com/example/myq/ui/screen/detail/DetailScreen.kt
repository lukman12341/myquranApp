package com.example.myq.ui.screen.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myq.data.model.Ayah
import com.example.myq.ui.QuranViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: QuranViewModel,
    surahNumber: Int,
    navController: NavController
) {
    val ayahList by viewModel.ayahList.collectAsState()

    LaunchedEffect(surahNumber) {
        viewModel.fetchSurahDetail(surahNumber)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Surah") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(
                items = ayahList,
                key = { ayah -> ayah.number }  // <-- Tambahkan KEY di sini!
            ) { ayah ->
                AyahItem(ayah = ayah)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
fun AyahItem(ayah: Ayah) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "${ayah.numberInSurah}. ${ayah.text}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}