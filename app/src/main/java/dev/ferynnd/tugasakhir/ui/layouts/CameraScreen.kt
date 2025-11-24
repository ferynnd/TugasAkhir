package dev.ferynnd.tugasakhir.ui.layouts

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
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
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LifecycleOwner
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import dev.ferynnd.tugasakhir.helper.CameraAnalyzerHelper
import dev.ferynnd.tugasakhir.helper.PoseLandmarkerHelper
import dev.ferynnd.tugasakhir.ui.components.BackgroundImageWithOverlay
import dev.ferynnd.tugasakhir.ui.components.PoseOverlay
import dev.ferynnd.tugasakhir.ui.theme.Secondary
import java.util.concurrent.Executors

@Composable
fun CameraScreen() {
    val context = LocalContext.current as Activity
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

    // setup pose landmarker
    val poseHelper = remember {
        PoseLandmarkerHelper(
            context,
            listener = object : PoseLandmarkerHelper.Listener {
                override fun onPoseResult(result: PoseLandmarkerResult, time: Long) {
                    poseResult = result
                }
                override fun onError(message: String) {
                    println("PoseError: $message")
                }
            }
        )
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundImageWithOverlay(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.5f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
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

                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Black))
                }

                BackButton(
                    icon = Icons.Default.ArrowBack,
                    onClick = { context.finish() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                )

                RepsBadge(
                    count = 30,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ==== BOTTOM INFO PANEL ====
            BottomInfoPanel(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                onEndSessionClick = { /* TODO */ }
            )
        }
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
fun RepsBadge(
    count: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Card) // Background abu gelap
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "REPS",
            color = White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = count.toString(),
            color = Primary, // Warna Kuning
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
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
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Card)
            .padding(24.dp)

    ) {
        // Judul Koreksi Gerakan
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Red
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Koreksi Gerakan",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Deskripsi Lorem Ipsum
        Text(
            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlineButton(
            text = "AKHIRI SESI",
            icon = Icons.Filled.Close,
            onClick = {}
        )

    }
}


@Composable
fun OutlineButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Red.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(50),
        border = BorderStroke(2.dp, Red),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Red, modifier = Modifier.padding(end = 5.dp))
            Text(text, color = Red, fontSize = 16.sp , fontWeight = FontWeight.Bold)
        }
    }
}
