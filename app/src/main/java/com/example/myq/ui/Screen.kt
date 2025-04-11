package com.example.myq.ui

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{surahNumber}") {
        fun createRoute(surahNumber: Int) = "detail/$surahNumber"
    }
}