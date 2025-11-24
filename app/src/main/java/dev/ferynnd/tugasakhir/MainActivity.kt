package dev.ferynnd.tugasakhir

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.ferynnd.tugasakhir.ui.layouts.HomeScreen
import dev.ferynnd.tugasakhir.ui.layouts.TrainingDetail
import dev.ferynnd.tugasakhir.ui.layouts.TrainingList
import dev.ferynnd.tugasakhir.ui.theme.Foreground
import dev.ferynnd.tugasakhir.ui.theme.PrimaryForeground
import dev.ferynnd.tugasakhir.ui.theme.Secondary
import dev.ferynnd.tugasakhir.ui.theme.TugasAkhirTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TugasAkhirTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("trainingList") {
                        TrainingList(navController)
                    }
                    composable("trainingDetail") {
                        TrainingDetail(navController)
                    }
                }
            }
        }
    }
}
