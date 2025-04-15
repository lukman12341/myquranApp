package com.example.myq.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myq.data.model.juzList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuzDetailScreen(navController: NavController, juzNumber: Int) {
    val juz = juzList.find { it.juzNumber == juzNumber }
    if (juz == null) {
        Text(
            text = "Juz tidak ditemukan",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Juz ${juz.juzNumber}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Surah: ${juz.startSurah} (${juz.startAyah}) sampai ${juz.endSurah} (${juz.endAyah})",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}