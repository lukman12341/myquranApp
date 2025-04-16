package com.example.myq.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myq.data.model.AyatDetail
import com.example.myq.data.network.ApiClient
import com.example.myq.viewmodel.JuzViewModel
import com.example.myq.viewmodel.JuzViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuzDetailScreen(
    navController: NavController,
    juzNumber: Int,
    viewModel: JuzViewModel = viewModel(factory = JuzViewModelFactory(ApiClient.apiService))
) {
    val ayatDetails by viewModel.ayatDetails.collectAsState()
    val sedangMemuat by viewModel.sedangMemuat.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(juzNumber) {
        if (juzNumber in 1..30) {
            viewModel.ambilDetailJuzDanTerjemahan(juzNumber)
        } else {
            viewModel._errorMessage.value = "Nomor Juz tidak valid: $juzNumber"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Juz $juzNumber") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            sedangMemuat -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Gagal memuat data: $errorMessage")
                }
            }
            ayatDetails.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada data ayat untuk Juz $juzNumber")
                }
            }
            else -> {
                LazyColumn(
                    contentPadding = paddingValues,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val groupedAyat = ayatDetails.groupBy { it.surahNumber }
                    if (groupedAyat.isEmpty()) {
                        item {
                            Text(
                                text = "Tidak ada ayat yang dikelompokkan",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        groupedAyat.forEach { (surahNumber, ayats) ->
                            item {
                                Text(
                                    text = ayats.first().surahName,
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(ayats) { ayat ->
                                AyatItem(ayat)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AyatItem(ayatDetail: AyatDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = ayatDetail.teksArab,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ayatDetail.terjemahan,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}