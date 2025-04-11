package com.example.myq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myq.data.network.QuranApiService
import com.example.myq.data.repository.QuranRepository
import com.example.myq.ui.QuranViewModel
import com.example.myq.ui.Screen
import com.example.myq.ui.screen.detail.DetailScreen
import com.example.myq.ui.screen.home.HomeScreen
import com.example.myq.ui.theme.QuranAppTheme
import com.example.myq.viewmodel.QuranViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuranAppTheme {
                val navController = rememberNavController()

                // Setup Retrofit
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.alquran.cloud/") // API Quran gratis
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(QuranApiService::class.java)
                val repository = QuranRepository(apiService)
                val viewModel: QuranViewModel = viewModel(
                    factory = QuranViewModelFactory(repository)
                )

                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        HomeScreen(viewModel = viewModel, navController = navController)
                    }
                    composable(
                        route = Screen.Detail.route,
                        arguments = listOf(navArgument("surahNumber") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val surahNumber = backStackEntry.arguments?.getInt("surahNumber") ?: 1
                        DetailScreen(viewModel = viewModel, surahNumber = surahNumber, navController = navController)
                    }
                }
            }
        }
    }
}