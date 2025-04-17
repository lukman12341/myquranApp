package com.example.myq.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

object FontSettings {
    var translationFontSize by mutableStateOf(14f) // Default 14sp
    var arabicFontSize by mutableStateOf(20f) // Default 20sp
}