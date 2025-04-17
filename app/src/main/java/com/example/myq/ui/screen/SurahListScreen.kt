package com.example.myq.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myq.ui.theme.FontSettings
import com.example.myq.viewmodel.SurahViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahListScreen(
    navController: NavController,
    surahNumber: Int,
    viewModel: SurahViewModel = viewModel()
) {
    val surahList = viewModel.surahList.collectAsState(initial = emptyList()).value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Surah") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(surahList) { surah ->
                ListItem(
                    headlineContent = {
                        Text(
                            text = surah.name,
                            style = TextStyle(fontSize = FontSettings.arabicFontSize.sp) // Gunakan ukuran huruf Arab
                        )
                    },
                    supportingContent = {
                        Text(
                            text = "${surah.englishName} - ${surah.englishNameTranslation}",
                            style = TextStyle(fontSize = FontSettings.translationFontSize.sp) // Gunakan ukuran huruf terjemahan
                        )
                    },
                    modifier = Modifier.clickable {
                        navController.navigate("surahDetail/${surah.number}")
                    }
                )
                Divider()
            }
        }
    }
}