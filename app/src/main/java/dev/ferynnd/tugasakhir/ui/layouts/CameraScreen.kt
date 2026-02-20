package dev.ferynnd.tugasakhir.ui.layouts

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.LifecycleOwner
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.data.model.ExerciseCode
import dev.ferynnd.tugasakhir.helper.CameraAnalyzerHelper
import dev.ferynnd.tugasakhir.helper.ExerciseState
import dev.ferynnd.tugasakhir.helper.PoseLandmarkerHelper
import dev.ferynnd.tugasakhir.helper.TypeOfExercise
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.components.LottieDialog
import dev.ferynnd.tugasakhir.ui.components.PoseOverlay
import dev.ferynnd.tugasakhir.ui.theme.BackgroundDark
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.colWarning
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.Month
import java.util.Locale
import java.util.concurrent.Executors
import kotlin.coroutines.resume

@Composable
fun CameraScreen( navController: NavController, exerciseCode: ExerciseCode) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

//    val reps by remember { mutableStateOf(20) }
//    val durationSeconds by remember { mutableStateOf(300) }

    var quitDialog by remember { mutableStateOf(false) }

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }

    var lastRepsSpoken by remember { mutableStateOf(0) }
    var lastFeedbackSpoken by remember { mutableStateOf<String?>(null) }

    var isCountdownActive by remember { mutableStateOf(true) }
    var countdownValue by remember { mutableStateOf(0) }

    var currentContinuation by remember {
        mutableStateOf<CancellableContinuation<Unit>?>(null)
    }

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
    var currentState by remember { mutableStateOf(ExerciseState.WAITING_START) }
//    var currentState by remember { mutableStateOf(ExerciseState.TOP) } // Default
    var feedback by remember { mutableStateOf<String?>(null) }

    val lastUpdateTime = remember { mutableStateOf(0L) }

    val poseHelper = remember {
        PoseLandmarkerHelper(
            context,
            listener = object : PoseLandmarkerHelper.Listener {

                override fun onPoseResult(result: PoseLandmarkerResult, time: Long) {

                    if (isCountdownActive) return

                    val now = System.currentTimeMillis()

                    // 🔥 THROTTLE 100ms (≈10 FPS UI)
                    if (now - lastUpdateTime.value < 100) return
                    lastUpdateTime.value = now

                    val allLandmarks = result.landmarks()
                    if (allLandmarks.isEmpty()) return

                    val firstPerson = allLandmarks[0]

                    val exerciseLogic = TypeOfExercise(firstPerson)

                    val evaluation = when (exerciseCode) {
                        ExerciseCode.PUSH_UP ->
                            exerciseLogic.evaluatePushUp(counter, currentState)

                        ExerciseCode.SQUAT ->
                            exerciseLogic.evaluateSquat(counter, currentState)

                        ExerciseCode.SIT_UP ->
                            exerciseLogic.evaluateSitUp(counter, currentState)
                    }

                    // Update Compose state hanya setelah throttle
                    poseResult = result
                    counter = evaluation.reps
                    currentState = evaluation.state
                    feedback = evaluation.feedback
                }

                override fun onError(message: String) {
                    Log.e("PoseError", message)
                }
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            poseHelper.close()
        }
    }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {

                tts?.setLanguage(Locale("id", "ID"))
                isTtsReady = true

                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {

                    override fun onStart(utteranceId: String?) {}

                    override fun onDone(utteranceId: String?) {
                        currentContinuation?.resume(Unit)
                    }

                    override fun onError(utteranceId: String?) {
                        currentContinuation?.resume(Unit)
                    }
                })
            }
        }

        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    suspend fun speakAndWait(text: String) {

        if (!isTtsReady) return

        suspendCancellableCoroutine<Unit> { continuation ->

            currentContinuation = continuation

            tts?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                System.currentTimeMillis().toString()
            )
        }
    }

    LaunchedEffect(Unit) {
        var current = 5
        countdownValue = current

        while (current > 0) {

            countdownValue = current   // ✅ Update UI dulu
            Log.e("Countdown", "Countdown: $current")
            speakAndWait(current.toString())
            delay(1000)
            current--
        }

        countdownValue = 0
        speakAndWait("Mulai")

        isCountdownActive = false
    }

    LaunchedEffect(counter) {
        if (counter > lastRepsSpoken) {
            speakAndWait("Repetisi $counter")
            lastRepsSpoken = counter
        }
    }

    LaunchedEffect(feedback) {
        feedback?.let {
            if (it != lastFeedbackSpoken) {
                speakAndWait(it)
                lastFeedbackSpoken = it
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding() // Padding untuk area notch/status bar
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp)
                .clip(RoundedCornerShape(12.dp))
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

            BackButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = {
                    quitDialog = true
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )

            RepsBadge(
                count = counter,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )

            if (isCountdownActive) {
                CountdownOverlay(countdownValue)
            }

        }

        BottomInfoPanel(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(), // Padding untuk navigasi bar sistem (garis bawah HP)
            onEndSessionClick = { navController.navigate("home") },
            feedback = feedback ?: "-",
            isCountdownActive = isCountdownActive
        )
    }

    if (quitDialog) {
        QuitDialog(
            onDismissRequest = { quitDialog = false },
            onConfirmQuit = {
                quitDialog = false // Tutup dialog dulu
                navController.popBackStack() // Kembali ke route sebelumnya
            }
        )
    }

    fun saveHistory()
    {

    }
}


// ==========================================================
//                   COMPONENT SECTION
// ==========================================================

@Composable
fun CameraPreviewContent(
    lifecycle: LifecycleOwner,
    poseHelper: PoseLandmarkerHelper,
    modifier: Modifier = Modifier
) {

    val executor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(Unit) {
        onDispose {
            executor.shutdown() //  FOR FIX THREAD LEAK
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->

            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({

                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().apply {
                    surfaceProvider = previewView.surfaceProvider
                }

                val analyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(
                        ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                    )
                    .build().apply {
                        setAnalyzer(executor, CameraAnalyzerHelper(poseHelper))
                    }

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycle,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    analyzer
                )

            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}

@Composable
fun RepsBadge(count: Int, modifier: Modifier = Modifier)
{
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
    onEndSessionClick: () -> Unit,
    feedback: String? = null,
    isCountdownActive: Boolean
) {
    val elapsedTime = rememberElapsedTime(isRunning = !isCountdownActive)

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
            text = feedback.toString(),
            fontWeight = FontWeight.Medium,
            color = TextSub,
            fontSize = 18.sp,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        SessionRunningCard(
            elapsedTime = elapsedTime,
            onEndSessionClick = onEndSessionClick
        )
    }
}

@Composable
fun CountdownOverlay(count: Int)
{

    val animatedScale by animateFloatAsState(
        targetValue = 1.2f,
        animationSpec = tween(600),
        label = ""
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(600),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (count > 0) count.toString() else "Mulai",
            fontSize = 90.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = animatedScale,
                    scaleY = animatedScale,
                    alpha = animatedAlpha
                )
        )
    }
}

@Composable
fun SessionRunningCard(
    elapsedTime: String,
    onEndSessionClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        colors = CardDefaults.cardColors(
            containerColor = TextSub.copy(alpha = 0.15f) // seperti textSub.copy(0.15f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TIME ELAPSED",
                        fontSize = 12.sp,
                        color = Black.copy(alpha = 0.8f)
                    )

                    Text(
                        text = elapsedTime,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSub
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onEndSessionClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(56.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        CustomIcon(
                            iconRes = R.drawable.cancel,
                            contentDescription = "berhenti",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "AKHIRI LATIHAN",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun rememberElapsedTime(isRunning: Boolean): String
{
    var startTime by remember { mutableStateOf<Long?>(null) }
    var currentTime by remember { mutableStateOf(0L) }
    LaunchedEffect(isRunning) {
        if (isRunning) {
            if (startTime == null) {
                startTime = System.currentTimeMillis()
            }
            while (isActive) {
                currentTime = System.currentTimeMillis()
                delay(1000)
            }
        }
    }

    val elapsedSeconds = startTime?.let {
        ((currentTime - it) / 1000).toInt()
    } ?: 0

    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60

    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
fun QuitDialog(
    onDismissRequest: () -> Unit,
    onConfirmQuit: () -> Unit
) {
    LottieDialog(
        lottieRes = R.raw.warning,
        title = "Keluar",
        message = "Apakah Anda yakin ingin keluar, ini tidak akan menyimpan progress latihan anda?",
        confirmText = "Ya",
        dismissText = "Tidak",
        colorBg = colWarning.copy(alpha = 0.15f),
        onConfirm = {
            onConfirmQuit()
        },
        onDismiss = {
            onDismissRequest()
        }
    )
}
