package com.example.myq.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myq.data.model.Surah
import com.example.myq.viewmodel.SurahViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: SurahViewModel = viewModel()) {
    val tabTitles = listOf("SURAH", "JUZ", "BOOKMARK")
    var selectedTabIndex by remember { mutableStateOf(0) }
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val surahList by viewModel.surahList.collectAsState()

    // Filter list sesuai query pencarian
    val filteredList = if (searchQuery.isEmpty()) {
        surahList
    } else {
        surahList.filter {
            it.englishName.contains(searchQuery, ignoreCase = true) ||
                    it.name.contains(searchQuery)
        }
    }

    // Panggil API saat pertama kali
    LaunchedEffect(Unit) {
        viewModel.fetchSurahList()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (isSearching) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Cari surah...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("Al-Qur'an Indonesia")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (isSearching) searchQuery = ""
                        isSearching = !isSearching
                    }) {
                        Icon(
                            imageVector = if (isSearching) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> TabContentSurah(navController, filteredList)
                1 -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Fitur Juz belum tersedia")
                }
                2 -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Bookmark belum tersedia")
                }
            }
        }
    }
}

@Composable
fun TabContentSurah(navController: NavHostController, surahList: List<Surah>) {
    LazyColumn {
        items(surahList) { surah ->
            ListItem(
                headlineContent = {
                    Column {
                        Text(
                            text = surah.name,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = surah.englishName,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                supportingContent = {
                    Text(text = "${surah.englishNameTranslation} - ${surah.revelationType}")
                },
                trailingContent = {
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