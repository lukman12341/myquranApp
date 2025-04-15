package com.example.myq
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.navigation.compose.rememberNavController
import com.example.myq.ui.navigation.NavGraph
import com.example.myq.ui.theme.ThemeState // import ini

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDark = ThemeState.isDarkTheme

            MaterialTheme(
                colorScheme = if (isDark) darkColorScheme() else lightColorScheme()
            ) {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}
