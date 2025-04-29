package com.example.myq.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myq.R
import com.example.myq.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Peluncur untuk Google Sign-In menggunakan IntentSenderRequest
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        isLoading = true
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        authViewModel.handleGoogleSignInResult(task) { error ->
            isLoading = false
            errorMessage = error
        }
    }

    // Navigasi ke beranda jika terautentikasi
    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthState.Authenticated) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(24.dp)
            ) {
                // Logo Aplikasi
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "Logo Aplikasi",
                    modifier = Modifier.size(100.dp)
                )

                // Judul Aplikasi
                Text(
                    text = "Al-Qur'an Indonesia",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )

                // Pesan Selamat Datang
                Text(
                    text = "Masuk untuk melanjutkan",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )

                // Tombol Masuk dengan Google
                Button(
                    onClick = {
                        isLoading = true
                        authViewModel.startGoogleSignIn(context, launcher)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_1),
                                contentDescription = "Ikon Google",
                                modifier = Modifier.size(24.dp),
                            )
                            Text(
                                text = "Masuk dengan Google",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }

                // Pesan Kesalahan
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}