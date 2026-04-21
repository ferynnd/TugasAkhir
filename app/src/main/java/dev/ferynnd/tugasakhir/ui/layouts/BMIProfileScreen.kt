package dev.ferynnd.tugasakhir.ui.layouts

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.ui.theme.*
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.data.model.Gender
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.viewmodel.UserViewModel
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.components.LottieDialog
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun BMIProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel(),
    supabaseClient: SupabaseClient
) {

    // remember untuk menyimpan state saat recomposition
    val user = remember { supabaseClient.auth.currentUserOrNull() }
    val scope = rememberCoroutineScope() // Untuk menjalankan coroutine / async task

    var gender by remember { mutableStateOf(Gender.MALE) }
    var weightValue by remember { mutableIntStateOf(60) } // Default 60kg
    var ageValue by remember { mutableStateOf("") }
    var heightValue by remember { mutableStateOf("") }

    val dialogState by remember { derivedStateOf { userViewModel.dialogState } }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // untuk memastikan data tidak berulang kecuali jika ada perubahan
    LaunchedEffect(user?.id) {
        user?.id?.let { id ->
            userViewModel.getUserBMI(id)
        }
    }

    LaunchedEffect(userViewModel.weightValue,
        userViewModel.heightValue ,
        userViewModel.ageValue,
        userViewModel.selectedGender)
    {
        weightValue = userViewModel.weightValue
        heightValue = userViewModel.heightValue.toString()
        ageValue = userViewModel.ageValue.toString()
        gender = userViewModel.selectedGender
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Primary)

                ) {
                    CustomIcon(
                        R.drawable.icarrowr,
                        contentDescription = null,
                        tint = Black,
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer(scaleX = -1f) // Flip horizontal

                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "UBAH BMI",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = White
                )

            }
        },
    ) { paddingValues ->

        dialogState?.let { state ->
            LottieDialog(
                lottieRes = state.lottieRes,
                title = state.title,
                message = state.message,
                colorBg = state.colorBg,
                autoDismiss = state.autoDismiss,
                onConfirm = { userViewModel.dismissDialog() },
                onDismiss = { userViewModel.dismissDialog() }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "SESUAIKAN",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = TextSub,
                        letterSpacing = (-1).sp
                    )
                )

                Text(
                    text = "PROFIL TUBUH",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Primary,
                        letterSpacing = (-1).sp
                    )
                )

            }

            Column {
                Text("JENIS KELAMIN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSub)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GenderCard(
                        modifier = Modifier.weight(1f),
                        label = "Laki - Laki",
                        isSelected = gender == Gender.MALE,
                        onClick = { gender = Gender.MALE }
                    )
                    GenderCard(
                        modifier = Modifier.weight(1f),
                        label = "Perempuan",
                        isSelected = gender == Gender.FEMALE,
                        onClick = { gender = Gender.FEMALE }
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InputMetricField(
                    modifier = Modifier.weight(1f),
                    label = "UMUR",
                    value = ageValue,
                    onValueChange = { if (it.length <= 3) ageValue = it },
                    unit = "Thn"
                )
                InputMetricField(
                    modifier = Modifier.weight(1f),
                    label = "TINGGI",
                    value = heightValue,
                    onValueChange = { if (it.length <= 3) heightValue = it },
                    unit = "cm"
                )
            }

            Column {
                Text("BERAT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSub)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Black, RoundedCornerShape(16.dp))
                        .border(1.5.dp, TextSub, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { if (weightValue > 0) weightValue-- },
                        modifier = Modifier.background(Primary, RoundedCornerShape(12.dp))
                    ) {
                        Text("-", fontSize = 24.sp, color = Black, fontWeight = FontWeight.Bold)
                    }
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(weightValue.toString(), fontSize = 28.sp, fontWeight = FontWeight.Black, color = TextSub)
                        Text(" kg", fontSize = 14.sp, color = TextSub, modifier = Modifier.padding(bottom = 6.dp))
                    }
                    IconButton(
                        onClick = { weightValue++ },
                        modifier = Modifier.background(Primary, RoundedCornerShape(12.dp))
                    ) {
                        Text("+", fontSize = 24.sp, color = Black, fontWeight = FontWeight.Bold)
                    }
                }
            }

            BMICardEdit(
                height = heightValue.toIntOrNull() ?: 0,
                weight = weightValue
            )

            Button(
                onClick = {
                    scope.launch {
                        val h = heightValue.toIntOrNull() ?: 0
                        val a = ageValue.toIntOrNull() ?: 0

                        if (h > 0 && a > 0 && weightValue > 0) {
                            userViewModel.updateBMI(
                                userId = user?.id ?: "",
                                gender = gender,
                                height = h,
                                weight = weightValue,
                                age = a
                            )
                        } else {
                            userViewModel.showError("Error", "Semua field harus diisi")
                        }
                    }
                    showSuccessDialog = true
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("SIMPAN PROFIL", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Black, fontFamily = Geist)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

     if (showSuccessDialog) {
        ShowDialog(
            textMessage = userViewModel.textMessage,
            onConfirm = { showSuccessDialog = false },
            onDismiss = { showSuccessDialog = false }
        )
     }
}


// ==========================================================
//                   COMPONENT SECTION
// ==========================================================


@Composable
fun GenderCard(modifier: Modifier, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = modifier.height(60.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Primary.copy(alpha = 0.1f) else Black,
        border = BorderStroke(1.5.dp, if (isSelected) Primary else TextSub)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = if (label == "Male") R.drawable.icmale else R.drawable.icfemale),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (isSelected) Primary else TextSub
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        color = if (isSelected) Primary else TextSub,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
    }
}

@Composable
fun InputMetricField(
    modifier: Modifier = Modifier,
    label: String,
    value: String, // Gunakan state value dari parent
    onValueChange: (String) -> Unit, // Callback untuk update state
    unit: String
) {
    // Menambahkan deteksi fokus (apakah sedang diketik)
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Tentukan warna border berdasarkan kondisi fokus atau isi
    val borderColor = if (isFocused ) Primary else TextSub
    val textColor = if (isFocused ) Color.White else TextSub

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextSub
        )
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Black, RoundedCornerShape(12.dp))
                .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = textColor,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center // Memastikan teks di tengah secara vertikal & horizontal
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = "0",
                                style = LocalTextStyle.current.copy(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextSub.copy(alpha = 0.5f), // Placeholder agak transparan
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                        // Penting: innerTextField() harus selalu dipanggil agar teks yang diketik muncul
                        innerTextField()
                    }
                }
            )

            Text(
                text = unit.uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isFocused) Primary else TextSub,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}


@Composable
fun BMICardEdit(height: Int, weight: Int) {
    val bmiData by remember(height, weight) {
        derivedStateOf {
            if (height > 0 && weight > 0) {
                val heightInMeter = height / 100.0
                val score = weight / (heightInMeter * heightInMeter)

                // Tentukan kategori dan progress bar (0.0f sampai 1.0f)
                val (category, progress) = when {
                    score < 18.5 -> "Underweight" to 0.15f
                    score < 25.0 -> "Normal" to 0.5f
                    score < 30.0 -> "Overweight" to 0.85f
                    else -> "Obese" to 0.95f
                }

                Triple(score, category, progress)
            } else {
                Triple(0.0, "-", 0.0f)
            }
        }
    }

    val (bmiResult, categoryName, bmiProgress) = bmiData

    Card(
        modifier = Modifier.fillMaxWidth().border(1.5.dp, TextSub, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Black),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("INDEKS MASSA TUBUH", fontSize = 14.sp, color = TextSub, fontWeight = FontWeight.Bold)
                    // Menampilkan 1 angka di belakang koma
                    Text(
                        text = String.format("%.1f", bmiResult),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("UNDERWEIGHT", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
                Text("NORMAL", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
                Text("OVERWEIGHT", fontSize = 10.sp, color = TextSub, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar dengan Indikator Dinamis
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val barWidth = maxWidth

                // Background Bar (Gradient)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Primary.copy(alpha = 0.3f),
                                    Primary
                                )
                            )
                        )
                )

                // Indikator (Garis Hitam)
                Box(
                    modifier = Modifier
                        .offset(x = (barWidth * bmiProgress) - 3.dp)
                        .width(6.dp)
                        .height(18.dp)
                        .align(Alignment.CenterStart)
                        .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(2.dp))
                        .zIndex(1f)
                )
            }
        }
    }
}