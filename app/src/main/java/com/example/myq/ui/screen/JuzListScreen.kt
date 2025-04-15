package com.example.myq.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myq.data.model.juzList
import com.example.myq.data.network.ApiClient
import com.example.myq.viewmodel.JuzViewModel
import com.example.myq.viewmodel.JuzViewModelFactory

// JuzListScreen menggunakan data statis "juzList"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuzListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Daftar Juz") })
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(juzList) { juz ->
                ListItem(
                    headlineContent = { Text("Juz ${juz.juzNumber}") },
                    supportingContent = {
                        Text("${juz.startSurah} (${juz.startAyah}) sampai ${juz.endSurah} (${juz.endAyah})")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("juz_detail/${juz.juzNumber}")
                        }
                )
                Divider()
            }
        }
    }
}

// JuzListScreenWithViewModel menggunakan ViewModel untuk mengambil data dari API
@Composable
fun JuzListScreenWithViewModel(navController: NavHostController) {
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
            .clickable { navController.navigate("juz_detail/$nomorJuz") },
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