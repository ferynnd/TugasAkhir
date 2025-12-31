package dev.ferynnd.tugasakhir.ui.layouts

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.ui.components.BackButton
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.Red
import dev.ferynnd.tugasakhir.ui.theme.White
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LifecycleOwner
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import dev.ferynnd.tugasakhir.helper.CameraAnalyzerHelper
import dev.ferynnd.tugasakhir.helper.PoseLandmarkerHelper
import dev.ferynnd.tugasakhir.helper.TypeOfExercise
import dev.ferynnd.tugasakhir.ui.components.BackgroundImageWithOverlay
import dev.ferynnd.tugasakhir.ui.components.PoseOverlay
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.BackgroundDark
import dev.ferynnd.tugasakhir.ui.theme.Secondary
import java.util.concurrent.Executors

@Composable
fun CameraScreen( navController: NavController ) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) Toast.makeText(context, "Izin kamera diperlukan", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) launcher.launch(Manifest.permission.CAMERA)
    }


    var poseResult by remember { mutableStateOf<PoseLandmarkerResult?>(null) }
    var counter by remember { mutableStateOf(0) }
    var status by remember { mutableStateOf(true) }

    // setup pose landmarker
    val poseHelper = remember {
        PoseLandmarkerHelper(
            context,
            listener = object : PoseLandmarkerHelper.Listener {
                override fun onPoseResult(result: PoseLandmarkerResult, time: Long) {
                    poseResult = result
                    val allLandmarks = result.landmarks()
                    if (allLandmarks.isNotEmpty()) {
                        val firstPerson = allLandmarks[0] // Ambil orang pertama yang terdeteksi

                        val exerciseLogic = TypeOfExercise(firstPerson)
                        val resultData = exerciseLogic.pushUp(counter, status) // Nanti Bisa Sesuai Item di Latihan

                        counter = resultData.counter
                        status = resultData.status
                    }
                }
                override fun onError(message: String) {
                    println("PoseError: $message")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary.copy(alpha = 0.6f))
            .statusBarsPadding() // Padding untuk area notch/status bar
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // MENGAMBIL SISA RUANG di atas Bottom Panel
                .padding(10.dp) // Memberikan "Space" agar kamera tidak nempel ke pinggir layar
                .clip(RoundedCornerShape(12.dp)) // Membuat sudut kamera membulat
        ) {
            if (hasCameraPermission) {
                CameraPreviewContent(
                    lifecycle = lifecycleOwner,
                    poseHelper = poseHelper,
                    modifier = Modifier.fillMaxSize()
                )
                PoseOverlay(
                    poseResult = poseResult,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Tombol Back di dalam area kamera
            BackButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )

            // Reps Badge di dalam area kamera
            RepsBadge(
                count = counter,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
        }

        BottomInfoPanel(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(), // Padding untuk navigasi bar sistem (garis bawah HP)
            onEndSessionClick = { navController.navigate("home") }
        )
    }
}

@Composable
fun CameraPreviewContent(
    lifecycle: LifecycleOwner,
    poseHelper: PoseLandmarkerHelper,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val executor = remember { Executors.newSingleThreadExecutor() }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        update = { previewView ->

            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // 1. Preview
                val preview = Preview.Builder().build().apply {
                    surfaceProvider = previewView.surfaceProvider
                }

                // 2. Analyzer
                val analyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build().apply {
                        setAnalyzer(
                            executor,
                            CameraAnalyzerHelper(poseHelper)
                        )
                    }

                // 3. Bind
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycle,
                        CameraSelector.DEFAULT_FRONT_CAMERA,
                        preview,
                        analyzer
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }, ContextCompat.getMainExecutor(context))
        }
    )
}


@Composable
fun RepsBadge(count: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .size(60.dp)
            .clip(RectangleShape)
            .background(Brush.verticalGradient(listOf(Primary, Color(0xFF991B1B))), RoundedCornerShape(10.dp))
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "REPS",
            color = White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = count.toString(),
            color = Color(0xFFF3F10A), // Warna merah sesuai desain
            fontSize = 28.sp,
            fontWeight = FontWeight.Black
        )
    }
}


@Composable
fun BottomInfoPanel(
    modifier: Modifier = Modifier,
    onEndSessionClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(Color.White)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {

        // Judul dengan Icon Bulat Pink
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFEBEB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFFE12524)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Koreksi Gerakan",
                color = Color(0xFF1A1C1E),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.",
            color = Color.Gray,
            fontSize = 14.sp,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ensure your back is straight and lower your chest closer to the ground for better activation.",
            color = Color(0xFF1A1C1E),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Tombol Akhiri Sesi (Merah Solid)
        androidx.compose.material3.Button(
            onClick = onEndSessionClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE12524)),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(12.dp).background(Color.White)) // Icon kotak stop
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "AKHIRI SESI",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
