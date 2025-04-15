package com.example.myq.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myq.data.model.Surah
import com.example.myq.ui.theme.ThemeState
import com.example.myq.viewmodel.SurahViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: SurahViewModel = viewModel()) {
    val tabTitles = listOf("SURAH", "JUZ", "BOOKMARK")
    var selectedTabIndex by remember { mutableStateOf(0) }
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showSettingsDialog by remember { mutableStateOf(false) }
    val isDarkTheme = ThemeState.isDarkTheme

    // Data surah dari ViewModel
    val surahList by viewModel.surahList.collectAsState()

    // Filter surah sesuai pencarian
    val filteredList = if (searchQuery.isEmpty()) {
        surahList
    } else {
        surahList.filter {
            it.englishName.contains(searchQuery, ignoreCase = true) ||
                    it.name.contains(searchQuery)
        }
    }

    // Ambil data surah ketika pertama kali tampil
    LaunchedEffect(Unit) {
        viewModel.fetchSurahList()
    }

    // Ganti colorScheme agar mirip warna hijau di screenshot
    // Silakan sesuaikan dengan warna Anda sendiri
    val topBarColor = if (isDarkTheme) Color(0xFF00796B) else Color(0xFF009688)

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme(
            primary = topBarColor
        ) else lightColorScheme(
            primary = topBarColor
        )
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    title = {
                        if (isSearching) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Cari surah...") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    cursorColor = Color.White
                                )
                            )
                        } else {
                            Text(
                                text = "Al-Qur'an Indonesia",
                                color = Color.White
                            )
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
                        IconButton(onClick = {
                            showSettingsDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Settings"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                // Tab di bagian atas (Surah, Juz, Bookmark)
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

                // Konten Tab
                when (selectedTabIndex) {
                    0 -> SurahListScreen(navController, filteredList)
                    1 -> JuzListScreen(navController)
                    2 -> BookmarkPlaceholderScreen()
                }
            }
        }

        // Dialog pengaturan
        SettingsDialog(
            showDialog = showSettingsDialog,
            currentLanguage = "id",
            currentTheme = if (isDarkTheme) "dark" else "light",
            onDismiss = { showSettingsDialog = false },
            onConfirm = { lang, theme ->
                ThemeState.toggleTheme(theme == "dark")
                println("Bahasa: $lang, Tema: $theme")
            }
        )
    }
}

@Composable
private fun SurahListScreen(navController: NavHostController, surahList: List<Surah>) {
    LazyColumn {
        items(surahList) { surah ->
            SurahItem(navController, surah)
        }
    }
}

@Composable
private fun SurahItem(navController: NavHostController, surah: Surah) {
    // Klikable untuk berpindah ke detail surah
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("surahDetail/${surah.number}") }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Kotak/lingkaran berisi nomor Surah di sisi kiri
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RectangleShape) // Ubah ke CircleShape jika mau lingkaran
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = surah.number.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(16.dp))

        // Bagian teks (nama Surah, nama Inggris, tipe wahyu, dll)
        Column(
            modifier = Modifier
                .weight(1f) // Agar teks bisa melebar
        ) {
            // Nama Surah (Arab)
            Text(
                text = surah.name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            // Nama Inggris
            Text(
                text = surah.englishName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            // Terjemahan + Tipe Wahyu
            Text(
                text = "${surah.englishNameTranslation} - ${surah.revelationType}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Jumlah ayat di sisi kanan
        Text(
            text = "${surah.numberOfAyahs} ayat",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun BookmarkPlaceholderScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Bookmark belum tersedia")
    }
}

@Composable
fun JuzListScreen(navController: NavHostController) {
    // Placeholder untuk Juz
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Daftar Juz belum tersedia")
    }
}
