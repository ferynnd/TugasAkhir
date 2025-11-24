package dev.ferynnd.tugasakhir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import dev.ferynnd.tugasakhir.ui.theme.TugasAkhirTheme
import androidx.activity.compose.setContent
import dev.ferynnd.tugasakhir.ui.layouts.CameraScreen

class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TugasAkhirTheme {
                CameraScreen()
            }
        }
    }
}