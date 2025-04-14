package com.example.myq.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// com.example.myq.ui.theme.ThemeState
object ThemeState {
    var isDarkTheme by mutableStateOf(false)
        private set

    fun toggleTheme(isDark: Boolean) {
        isDarkTheme = isDark
    }
}
