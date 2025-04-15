package com.example.myq.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myq.ui.screen.HomeScreen
import com.example.myq.ui.screen.JuzDetailScreen
import com.example.myq.ui.screen.JuzListScreen
import com.example.myq.ui.screen.SurahDetailScreen
import com.example.myq.ui.screen.SurahListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        // Halaman Home
        composable("home") {
            HomeScreen(navController)
        }

        // Halaman Daftar Juz
        composable("juz_list") {
            JuzListScreen(navController)
        }

        // Halaman Detail Juz
        composable("juz_detail/{juzNumber}") { backStackEntry ->
            val juzNumber = backStackEntry.arguments?.getString("juzNumber")?.toIntOrNull() ?: 1
            JuzDetailScreen(navController, juzNumber)
        }

        // Halaman Daftar Surah
        composable("surah_list/{surahNumber}") { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getString("surahNumber")?.toInt() ?: 1
            SurahListScreen(navController, surahNumber)
        }

        // Halaman Detail Surah
        composable("surahDetail/{surahNumber}") { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getString("surahNumber")?.toIntOrNull() ?: 1
            SurahDetailScreen(surahNumber)
        }
    }
}