package com.example.myq.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myq.ui.screen.HomeScreen
import com.example.myq.ui.screen.JuzDetailScreen
import com.example.myq.ui.screen.SurahDetailScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        // Tambahkan ini di setup NavHost Anda
        composable("detailJuz/{nomorJuz}") { backStackEntry ->
            val nomorJuz = backStackEntry.arguments?.getString("nomorJuz")?.toIntOrNull() ?: 1
            JuzDetailScreen(navController, nomorJuz)
        }
        composable("surahDetail/{surahNumber}") { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getString("surahNumber")?.toIntOrNull() ?: 1
            SurahDetailScreen(surahNumber)
        }
    }
}
