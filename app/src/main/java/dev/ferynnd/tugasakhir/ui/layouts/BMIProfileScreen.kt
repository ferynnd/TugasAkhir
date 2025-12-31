package dev.ferynnd.tugasakhir.ui.layouts

import android.graphics.drawable.Icon
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.ui.theme.*
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.components.CustomIcon

@Composable
fun BMIProfileScreen(navController: NavController) {
    var weight by remember { mutableStateOf(70) }
    var gender by remember { mutableStateOf("Male") }

    var ageValue by remember { mutableStateOf("") }
    var heightValue by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .border(1.dp, Border, CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TextMain)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Text
            Column {
                Text(
                    text = "Let's customize",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMain
                )
                Text(
                    text = "your metrics",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Primary
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Accurate body data helps our AI analyze your movements with precision.",
                    fontSize = 14.sp,
                    color = TextSub,
                    lineHeight = 20.sp
                )
            }

            // Gender Selection
            Column {
                Text("GENDER", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSub)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GenderCard(
                        modifier = Modifier.weight(1f),
                        label = "Male",
                        isSelected = gender == "Male",
                        onClick = { gender = "Male" }
                    )
                    GenderCard(
                        modifier = Modifier.weight(1f),
                        label = "Female",
                        isSelected = gender == "Female",
                        onClick = { gender = "Female" }
                    )
                }
            }

            // Age and Height
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InputMetricField(
                    modifier = Modifier.weight(1f),
                    label = "AGE",
                    value = ageValue,
                    onValueChange = { ageValue = it },
                    unit = "yrs"
                )
                InputMetricField(
                    modifier = Modifier.weight(1f),
                    label = "HEIGHT",
                    value = heightValue,
                    onValueChange = { heightValue = it },
                    unit = "cm"
                )
            }

            // Weight Counter
            Column {
                Text("WEIGHT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSub)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Card, RoundedCornerShape(16.dp))
                        .border(1.dp, Border, RoundedCornerShape(16.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { if (weight > 0) weight-- },
                        modifier = Modifier.background(Input, RoundedCornerShape(12.dp))
                    ) {
                        Text("-", fontSize = 24.sp, color = Primary, fontWeight = FontWeight.Bold)
                    }
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(weight.toString(), fontSize = 28.sp, fontWeight = FontWeight.Black, color = TextMain)
                        Text(" kg", fontSize = 14.sp, color = TextSub, modifier = Modifier.padding(bottom = 6.dp))
                    }
                    IconButton(
                        onClick = { weight++ },
                        modifier = Modifier.background(Input, RoundedCornerShape(12.dp))
                    ) {
                        Text("+", fontSize = 24.sp, color = Primary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Activity Level Dropdown
            Column {
                Text("ACTIVITY LEVEL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSub)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Card, RoundedCornerShape(16.dp))
                        .border(1.dp, Border, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Select activity level", color = TextMain)
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Primary)
                }
            }

            // BMI Result Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Card),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFF22C55E), CircleShape))
                            Spacer(Modifier.width(8.dp))
                            Text("BMI CALCULATION", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSub)
                        }
                        Surface(color = Color(0xFFDCFCE7), shape = RoundedCornerShape(50)) {
                            Text("NORMAL", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = Color(0xFF166534), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text("22.8", fontSize = 48.sp, fontWeight = FontWeight.Black, color = TextMain)

                    // BMI Multi-color Slider
                    Box(modifier = Modifier.fillMaxWidth().height(12.dp).clip(CircleShape).background(Color.LightGray)) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Box(modifier = Modifier.weight(0.2f).fillMaxHeight().background(Color(0xFFBFDBFE))) // Under
                            Box(modifier = Modifier.weight(0.3f).fillMaxHeight().background(Color(0xFFBBF7D0))) // Normal
                            Box(modifier = Modifier.weight(0.2f).fillMaxHeight().background(Color(0xFFFEF08A))) // Over
                            Box(modifier = Modifier.weight(0.3f).fillMaxHeight().background(Color(0xFFFECACA))) // Obese
                        }
                    }
                }
            }

            // Save Button
            Button(
                onClick = { /* Save action */ },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Save Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(painterResource(R.drawable.icarrowr), contentDescription = null, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun GenderCard(modifier: Modifier, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = modifier.height(60.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Primary.copy(alpha = 0.05f) else Card,
        border = BorderStroke(1.5.dp, if (isSelected) Primary else Border)
    ) {
        Box(
            modifier = Modifier.fillMaxSize() // Mengisi seluruh area Surface/Card
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
                        color = if (isSelected) Primary else TextMain,
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
                .background(Card, RoundedCornerShape(16.dp))
                .border(1.dp, Border, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            // Komponen Input Utama
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.width(IntrinsicSize.Min), // Lebar mengikuti isi teks
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMain,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                decorationBox = { innerTextField ->
                    // Tempat teks diketik, jika kosong tampilkan placeholder
                    if (value.isEmpty()) {
                        Text(
                            text = "0",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = TextSub
                        )
                    }
                    innerTextField()
                }
            )

            Text(
                text = " $unit",
                fontSize = 12.sp,
                color = TextSub,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}