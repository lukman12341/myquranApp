package com.example.myq.ui.screen

import androidx.compose.foundation.clickable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myq.data.model.Ayah
import com.example.myq.data.network.ApiClient
import com.example.myq.viewmodel.JuzViewModel
import com.example.myq.viewmodel.JuzViewModelFactory

@Composable
fun JuzListScreen(navController: NavHostController) {
    val viewModel: JuzViewModel = viewModel(factory = JuzViewModelFactory(ApiClient.apiService))
    val daftarJuz by viewModel.daftarJuz.collectAsState()
    val sedangMemuat by viewModel.sedangMemuat.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.ambilDaftarJuz()
    }

    if (sedangMemuat) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn {
            items(daftarJuz) { nomorJuz ->
                JuzItem(navController, nomorJuz)
            }
        }
    }
}

@Composable
private fun JuzItem(navController: NavHostController, nomorJuz: Int) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { navController.navigate("detailJuz/$nomorJuz") },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = "Juz $nomorJuz",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun JuzDetailScreen(navController: NavHostController, nomorJuz: Int) {
    val viewModel: JuzViewModel = viewModel(factory = JuzViewModelFactory(ApiClient.apiService))
    val detailJuz by viewModel.detailJuz.collectAsState()
    val sedangMemuat by viewModel.sedangMemuat.collectAsState()

    LaunchedEffect(nomorJuz) {
        viewModel.ambilDetailJuz(nomorJuz)
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            CenterAlignedTopAppBar(
                title = { Text("Juz $nomorJuz") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (sedangMemuat) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            detailJuz?.data?.let { dataJuz ->
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    items(dataJuz.ayah) { ayat ->
                        AyahItem(ayat)
                    }
                }
            }
        }
    }
}

@Composable
private fun AyahItem(ayat: Ayah) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Teks Arab
            Text(
                text = ayat.text,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Terjemahan
            Text(
                text = "Terjemahan akan ditampilkan di sini",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}