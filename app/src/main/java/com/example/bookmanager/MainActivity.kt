package com.example.bookmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.bookmanager.ui.theme.BookManagerTheme
import com.example.bookmanager.ui.AppNavGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            val systemDark = isSystemInDarkTheme()
            var isDarkThemeState by remember { mutableStateOf(systemDark) }

            BookManagerTheme(darkTheme = isDarkThemeState) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavGraph(
                        isDarkTheme = isDarkThemeState,
                        onThemeToggle = { isDarkThemeState = !isDarkThemeState }
                    )
                }
            }
        }
    }
}