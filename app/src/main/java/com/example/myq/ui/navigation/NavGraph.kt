package com.example.myq.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myq.ui.screen.HomeScreen
import com.example.myq.ui.screen.JuzDetailScreen
import com.example.myq.ui.screen.JuzListScreen
import com.example.myq.ui.screen.LoginScreen
import com.example.myq.ui.screen.ProfileScreen
import com.example.myq.ui.screen.SurahDetailScreen
import com.example.myq.ui.screen.SurahListScreen
import com.example.myq.viewmodel.AuthViewModel

@Composable
fun NavGraph(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    val authState = authViewModel.authState.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = if (authState is AuthViewModel.AuthState.Authenticated) "home" else "login"
    ) {
        composable("login") {
            LoginScreen(navController, authViewModel)
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable("juz_list") {
            JuzListScreen(navController)
        }

        composable("juz_detail/{juzNumber}") { backStackEntry ->
            val juzNumber = backStackEntry.arguments?.getString("juzNumber")?.toIntOrNull() ?: 1
            JuzDetailScreen(navController, juzNumber)
        }

        composable("surah_list/{surahNumber}") { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getString("surahNumber")?.toInt() ?: 1
            SurahListScreen(navController, surahNumber)
        }

        composable("surahDetail/{surahNumber}") { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getString("surahNumber")?.toIntOrNull() ?: 1
            SurahDetailScreen(surahNumber)
        }

        composable("profile") {
            ProfileScreen(navController)
        }
    }
}