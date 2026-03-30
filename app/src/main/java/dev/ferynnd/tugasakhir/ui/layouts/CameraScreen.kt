package dev.ferynnd.tugasakhir.ui.layouts

import android.Manifest
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.ui.components.BackButton
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import dev.ferynnd.tugasakhir.data.model.ExerciseCode
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.viewmodel.ExerciseViewModel
import dev.ferynnd.tugasakhir.data.viewmodel.UserViewModel
import dev.ferynnd.tugasakhir.helper.exercise.BodyCalibration
import dev.ferynnd.tugasakhir.helper.exercise.CalibrationManager
import dev.ferynnd.tugasakhir.helper.CameraAnalyzerHelper
import dev.ferynnd.tugasakhir.helper.ExerciseState
import dev.ferynnd.tugasakhir.helper.PoseLandmarkerHelper
import dev.ferynnd.tugasakhir.helper.exercise.TypeOfExercise
import dev.ferynnd.tugasakhir.ui.components.BottomInfoPanel
import dev.ferynnd.tugasakhir.ui.components.CalibrationDoneOverlay
import dev.ferynnd.tugasakhir.ui.components.CalibrationProgressOverlay
import dev.ferynnd.tugasakhir.ui.components.CountdownOverlay
import dev.ferynnd.tugasakhir.ui.components.FinishDialog
import dev.ferynnd.tugasakhir.ui.components.PoseOverlay
import dev.ferynnd.tugasakhir.ui.components.QuitDialog
import dev.ferynnd.tugasakhir.ui.components.ReadyCalibrationOverlay
import dev.ferynnd.tugasakhir.ui.components.RepsBadge
import dev.ferynnd.tugasakhir.ui.components.ScreenPhase
import dev.ferynnd.tugasakhir.ui.components.rememberElapsedSeconds
import dev.ferynnd.tugasakhir.ui.theme.BackgroundDark
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import dev.ferynnd.tugasakhir.helper.exercise.PostureGate
import dev.ferynnd.tugasakhir.ui.theme.Background
import java.util.Optional

@Composable
fun CameraScreen(
    navController: NavController,
    exerciseCode: ExerciseCode,
    viewModel: ExerciseViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    supabaseClient: SupabaseClient
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val user = remember { supabaseClient.auth.currentUserOrNull() }

    LaunchedEffect(user?.id) {
        user?.id?.let { id ->
            userViewModel.getUserBMI(id)
        }
    }

    var weightValue by remember { mutableIntStateOf(60) } // Default 60kg

    LaunchedEffect(userViewModel.weightValue) {
        weightValue = userViewModel.weightValue
    }

    LaunchedEffect(viewModel.isSuccess) {
        if (viewModel.isSuccess) {
            navController.navigate("trainingHistory") {
                popUpTo("currentScreen") { inclusive = true }
            }
        }
    }

    ////
    /// SPEECH FEEDBACK DLL, MENGIKUTI RECOMPOSE / FRAME DARI DETEKSI
    ///


    var quitDialog by remember { mutableStateOf(false) }
    var finishDialog by remember { mutableStateOf(false) }

    var elapsedSeconds by remember { mutableIntStateOf(0) }

    var startCountdown by remember { mutableStateOf(false) }

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }

    var lastRepsSpoken by remember { mutableStateOf(0) }
    var lastFeedbackSpoken by remember { mutableStateOf<String?>(null) }
    var feedbackJob by remember { mutableStateOf<Job?>(null) }

    var isCountdownActive by remember { mutableStateOf(true) }
    var countdownValue by remember { mutableStateOf(0) }

    val runningSeconds = rememberElapsedSeconds(isRunning = !isCountdownActive)

    LaunchedEffect(runningSeconds) {
        elapsedSeconds = runningSeconds
    }

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
    var feedback by remember { mutableStateOf<String?>(null) }

    val lastUpdateTime = remember { mutableStateOf(0L) }

    var screenPhase by remember { mutableStateOf(ScreenPhase.WAITING_CALIBRATION) }

    val calibrationManager = remember { CalibrationManager(exerciseCode) }
    var calibration by remember { mutableStateOf(BodyCalibration()) }

    var calibrationProgress by remember { mutableStateOf(0f) }

    var isBodyDetected by remember { mutableStateOf(false) }
    val postureGate = remember { PostureGate(invalidFramesRequired = 10) }
    var isFormCorrect by remember { mutableStateOf(true) }

    val landmarkBuffer = remember { ArrayDeque<List<NormalizedLandmark>>() }
    val BUFFER_SIZE = 5

    val poseHelper = remember {
        PoseLandmarkerHelper(
            context,
            listener = object : PoseLandmarkerHelper.Listener {

                override fun onPoseResult(result: PoseLandmarkerResult, time: Long) {

                    val allLandmarks = result.landmarks()
                    if (allLandmarks.isEmpty()) return
                    val firstPerson = allLandmarks[0]

                    // ✅ Handle kalibrasi dulu
                    when (screenPhase) {

                        ScreenPhase.CALIBRATING -> {
                            // ✅ Cek minimal landmark terdeteksi (visibility > 0.5)
                            val isBodyVisible = firstPerson.size >= 25 &&
                                    firstPerson[11].visibility().orElse(0f) > 0.5f && // bahu kiri
                                    firstPerson[12].visibility().orElse(0f) > 0.5f && // bahu kanan
                                    firstPerson[23].visibility().orElse(0f) > 0.5f && // pinggul kiri
                                    firstPerson[24].visibility().orElse(0f) > 0.5f    // pinggul kanan

                            if (!isBodyVisible) {
                                // Tubuh tidak terdeteksi — reset progress, minta user perbaiki posisi
                                calibrationManager.reset()
                                isBodyDetected = false
                                calibrationProgress = 0f
                                poseResult = result
                                return
                            }
                            isBodyDetected = true

                            calibrationManager.addSample(firstPerson)
                            calibrationProgress = calibrationManager.progress.toFloat() / calibrationManager.requiredSamples

                            if (calibrationManager.isDone) {
                                calibration = calibrationManager.buildCalibration()
                                screenPhase = ScreenPhase.CALIBRATION_DONE
                            }
                            poseResult = result
                            return // jangan evaluate dulu
                        }

                        ScreenPhase.EXERCISE -> {
                            val now = System.currentTimeMillis()
                            if (now - lastUpdateTime.value < 60) return
                            lastUpdateTime.value = now

                            // ✅ Kumpulkan landmark ke buffer
                            landmarkBuffer.addLast(firstPerson)
                            if (landmarkBuffer.size > BUFFER_SIZE) landmarkBuffer.removeFirst()

                            // ✅ Belum cukup frame, skip evaluate dulu
                            if (landmarkBuffer.isEmpty()) return

                            // ✅ Rata-rata tiap titik landmark dari semua frame di buffer
                            val landmarkCount = firstPerson.size
                            val averaged = (0 until landmarkCount).map { i ->
                                val avgX = landmarkBuffer.map { it[i].x() }.average().toFloat()
                                val avgY = landmarkBuffer.map { it[i].y() }.average().toFloat()
                                val avgZ = landmarkBuffer.map { it[i].z() }.average().toFloat()
                                val avgVis = landmarkBuffer.map {
                                    it[i].visibility().orElse(0f).toDouble()
                                }.average().toFloat()
                                val avgPres = landmarkBuffer.map {
                                    it[i].presence().orElse(0f).toDouble()
                                }.average().toFloat()

                                // Buat NormalizedLandmark baru dari nilai rata-rata
                                NormalizedLandmark.create(
                                    avgX,
                                    avgY,
                                    avgZ,
                                    Optional.of(avgVis),
                                    Optional.of(avgPres)
                                )
                            }

                            // ✅ Kirim landmark yang sudah di-average ke evaluate
                            val exerciseLogic = TypeOfExercise(averaged)

                            val evaluation = when (exerciseCode) {
                                ExerciseCode.PUSH_UP ->
                                    exerciseLogic.evaluatePushUp(counter, currentState, calibration, postureGate)
                                ExerciseCode.SQUAT ->
                                    exerciseLogic.evaluateSquat(counter, currentState, calibration, postureGate)
                                ExerciseCode.SIT_UP ->
                                    exerciseLogic.evaluateSitUp(counter, currentState, calibration, postureGate)
                            }

                            poseResult = result
                            counter = evaluation.reps
                            currentState = evaluation.state
                            feedback = evaluation.feedback
                            isFormCorrect = evaluation.isCorrect

                        }

                        else -> {
                            poseResult = result
                        }
                    }
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

                var result = tts?.setLanguage(Locale("id", "ID"))

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {

                    Log.e("TTS", "Bahasa Indonesia tidak tersedia, switch ke English")

                    // 2️⃣ Fallback ke English
                    result = tts?.setLanguage(Locale.US)

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {

                        Log.e("TTS", "English juga tidak tersedia, pakai default device")

                        // 3️⃣ Fallback terakhir
                        tts?.setLanguage(Locale.getDefault())
                    }
                }


                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {

                    override fun onStart(utteranceId: String?) {}

                    override fun onDone(utteranceId: String?) {
                        currentContinuation?.let {
                            if (it.isActive) it.resume(Unit)
                        }
                        currentContinuation = null
                    }

                    override fun onError(utteranceId: String?) {
                        currentContinuation?.let {
                            if (it.isActive) it.resume(Unit)
                        }
                        currentContinuation = null
                    }
                })

                isTtsReady = true

            } else {
                Log.e("TTS", "Gagal inisialisasi TextToSpeech")
            }
        }

        onDispose {
            tts?.stop()
            tts?.shutdown()
            tts = null
            isTtsReady = false
        }
    }


    suspend fun speakAndWait(text: String) {

        Log.d("TTS", "speakAndWait called: $text | isTtsReady=$isTtsReady | tts=$tts")
        if (!isTtsReady || tts == null) {
            Log.d("TTS", "TTS belum siap, skip")
            return
        }

        kotlinx.coroutines.withTimeoutOrNull(5000L) {
            suspendCancellableCoroutine<Unit> { continuation ->
                currentContinuation = continuation
                val utteranceId = "utt_${System.currentTimeMillis()}"

                tts?.speak(
                    text,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    utteranceId
                )

                continuation.invokeOnCancellation {
                    tts?.stop()
                    currentContinuation = null
                }
            }
        } ?: Log.w("TTS", "speakAndWait timeout untuk: $text")
    }


    LaunchedEffect(counter) {
        if (counter > lastRepsSpoken) {
            tts?.stop()
            speakAndWait("$counter")
            lastRepsSpoken = counter
            lastFeedbackSpoken = null
        }
    }

    LaunchedEffect(feedback) {
        feedback?.let { newFeedback ->
            if (
                newFeedback.isBlank() ||
                newFeedback == "-"
            ) return@let

            // Cancel timer sebelumnya
            feedbackJob?.cancel()

            feedbackJob = launch {
                delay(800)

                if (newFeedback == feedback) {
                    // ✅ Ucapkan meski sama, tapi batasi dengan waktu
                    if (newFeedback != lastFeedbackSpoken) {
                        speakAndWait(newFeedback)
                        lastFeedbackSpoken = newFeedback

                        // ✅ Reset lastFeedbackSpoken setelah 4 detik
                        // agar feedback yang sama bisa diucapkan lagi
                        delay(4000)
                        if (lastFeedbackSpoken == newFeedback) {
                            lastFeedbackSpoken = null
                        }
                    }
                }
            }
        }
    }

    // Effect 1 — hanya handle sampai trigger countdown
    LaunchedEffect(screenPhase) {
        Log.d("PHASE", "screenPhase changed: $screenPhase")

        if (screenPhase == ScreenPhase.CALIBRATION_DONE) {
            var waited = 0
            while (!isTtsReady && waited < 30) {
                delay(100)
                waited++
            }

            delay(500)
            speakAndWait("Kalibrasi selesai")
            startCountdown = true  // ✅ trigger effect 2
        }
    }

// Effect 2 — jalankan countdown, tidak akan di-restart
    LaunchedEffect(startCountdown) {
        if (!startCountdown) return@LaunchedEffect

        Log.d("COUNTDOWN", "countdown dimulai")
        screenPhase = ScreenPhase.COUNTDOWN

        var current = 5
        while (current > 0) {
            countdownValue = current
            Log.d("COUNTDOWN", ">>> SET angka: $current")
            speakAndWait(current.toString())
            Log.d("COUNTDOWN", ">>> SELESAI angka: $current")
            delay(100)
            current--
        }

        countdownValue = 0
        speakAndWait("Mulai")
        Log.d("COUNTDOWN", ">>> MULAI EXERCISE")
        screenPhase = ScreenPhase.EXERCISE
        isCountdownActive = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (hasCameraPermission) {
                CameraPreviewContent(
                    lifecycle = lifecycleOwner,
                    poseHelper = poseHelper,
                    modifier = Modifier.fillMaxSize()
                )
                PoseOverlay(
                    poseResult = poseResult,
                    modifier = Modifier.fillMaxSize(),
                    correct = isFormCorrect
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

            // ✅ OVERLAY 1: Siap kalibrasi?
            if (screenPhase == ScreenPhase.WAITING_CALIBRATION) {
                ReadyCalibrationOverlay(
                    exerciseCode = exerciseCode,
                    instruction = calibrationManager.instruction,
                    onStart = {
                        calibrationManager.reset()
                        screenPhase = ScreenPhase.CALIBRATING
                    }
                )
            }

            // ✅ OVERLAY 2: Progress kalibrasi
            if (screenPhase == ScreenPhase.CALIBRATING) {
                CalibrationProgressOverlay(
                    progress = calibrationProgress,
                    instruction = calibrationManager.instruction,
                    isBodyDetected = isBodyDetected
                )
            }

            // ✅ OVERLAY 3: Kalibrasi selesai
            if (screenPhase == ScreenPhase.CALIBRATION_DONE) {
                CalibrationDoneOverlay()
            }

            // ✅ OVERLAY 4: Countdown
            if (screenPhase == ScreenPhase.COUNTDOWN) {
                CountdownOverlay(countdownValue)
            }

            if ( screenPhase == ScreenPhase.EXERCISE ) {
                BottomInfoPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter) // INI KUNCINYA
                        .padding(24.dp),
                    onEndSessionClick = {
                        finishDialog = true
                    },
                    feedback = feedback ?: "",
                    elapsedTime = runningSeconds,
                    exerciseName = exerciseCode.name
                )
            }
        }

    }

    if (quitDialog) {
        QuitDialog(
            onDismissRequest = { quitDialog = false },
            onConfirmQuit = {
                landmarkBuffer.clear()
                quitDialog = false // Tutup dialog dulu
                navController.popBackStack() // Kembali ke route sebelumnya
            }
        )
    }


    if (finishDialog) {
        FinishDialog(
            onDismissRequest = { finishDialog = false },
            onConfirm = {
                landmarkBuffer.clear()

                val durationSeconds = elapsedSeconds % 60

                viewModel.storeHistoryExercise(
                    userId = user?.id ?: "",
                    reps = counter,
                    durationSeconds = durationSeconds,
                    weightKg = weightValue,
                    exerciseCode = exerciseCode
                )
                finishDialog = false
            }
        )
    }

    LaunchedEffect(viewModel.lastInsertedId) {
        viewModel.lastInsertedId?.let { newId ->
            // Navigasi ke summary dengan ID asli dari DB
            navController.navigate("trainingSummary/$newId") {
                // Bersihkan CameraScreen dari history agar tidak bisa "Back" ke kamera
                popUpTo("cameraScreen") { inclusive = true }
            }
        }
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
