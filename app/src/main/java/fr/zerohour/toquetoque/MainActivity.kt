package fr.zerohour.toquetoque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import fr.zerohour.toquetoque.data.local.SettingsManager
import fr.zerohour.toquetoque.ui.navigation.MainScreen
import fr.zerohour.toquetoque.ui.theme.ToqueToqueTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val settingsManager = SettingsManager(this)
        setContent {
            val themeMode by settingsManager.themeMode.collectAsState()
            ToqueToqueTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(settingsManager = settingsManager)
                }
            }
        }
    }
}