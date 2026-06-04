package fr.zerohour.toquetoque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import fr.zerohour.toquetoque.feature.home.HomeScreen
import fr.zerohour.toquetoque.ui.theme.ToqueToqueTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToqueToqueTheme {
                HomeScreen()
            }
        }
    }
}