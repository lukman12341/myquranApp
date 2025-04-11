package com.example.myq.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myq.data.model.Surah
import com.example.myq.ui.QuranViewModel
import com.example.myq.ui.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: QuranViewModel, navController: NavController) {
    val surahList by viewModel.surahList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchSurahList()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Al-Qur'an App") })
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(surahList) { surah ->
                SurahItem(surah = surah, onClick = {
                    navController.navigate(Screen.Detail.createRoute(surah.number))
                })
            }
        }
    }
}

@Composable
fun SurahItem(surah: Surah, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${surah.number}. ${surah.englishName} (${surah.name})", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = surah.englishNameTranslation, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Ayat: ${surah.numberOfAyahs} | Tipe: ${surah.revelationType}", style = MaterialTheme.typography.bodySmall)
        }
    }
}