package com.example.myq.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myq.data.auth.FirebaseAuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val currentUser = FirebaseAuthManager.getCurrentUser()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(24.dp)
            ) {
                // Judul Profil
                Text(
                    text = "Profil Pengguna",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                )

                // Informasi Pengguna
                if (currentUser != null) {
                    Text(
                        text = "Nama: ${currentUser.displayName ?: "Tidak tersedia"}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                    )
                    Text(
                        text = "Email: ${currentUser.email ?: "Tidak tersedia"}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                    )
                } else {
                    Text(
                        text = "Tidak ada pengguna yang masuk",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                // Tombol Keluar
                Button(
                    onClick = {
                        FirebaseAuthManager.signOut(context)
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = currentUser != null
                ) {
                    Text(
                        text = "Keluar",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
