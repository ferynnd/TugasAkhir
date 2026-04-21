package dev.ferynnd.tugasakhir.ui.layouts.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ferynnd.tugasakhir.data.viewmodel.AuthViewModel
import dev.ferynnd.tugasakhir.ui.layouts.ProfileInput
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.Primary

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavToLogin: () -> Unit
) {
    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Background
    ) { paddingValues ->
        val layotDirection = LocalLayoutDirection.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp, start = paddingValues.calculateStartPadding(layotDirection),end = paddingValues.calculateEndPadding(layotDirection))
        ) {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 100.dp, y = (-50).dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Primary.copy(alpha = 0.12f), Color.Transparent)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Section
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Black)) {
                            append("BUAT ")
                        }
                        withStyle(style = SpanStyle(color = Primary, fontWeight = FontWeight.Black)) {
                            append("AKUN BARU")
                        }
                    },
                    fontSize = 32.sp,
                    lineHeight = 36.sp,
                    letterSpacing = (-1).sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Daftar sekarang untuk mulai memantau latihan Anda dengan presisi AI.",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Form Input menggunakan komponen ProfileInput yang sudah kita buat
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileInput(
                        label = "Nama Lengkap",
                        value = fullname,
                        onValueChange = { fullname = it },
                        placeholder = "Nama Lengkap"
                    )

                    ProfileInput(
                        label = "Email ",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "jhondoe@gmail.com"
                    )

                    ProfileInput(
                        label = "Kata Sandi",
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "••••••••",
                        isPassword = true
                    )

                    ProfileInput(
                        label = "Konfirmasi Kata Sandi",
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = "••••••••",
                        isPassword = true
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Primary,
                            uncheckedColor = Color.DarkGray,
                            checkmarkColor = Color.Black
                        )
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Saya setuju dengan ")
                            withStyle(style = SpanStyle(color = Primary, fontWeight = FontWeight.Bold)) {
                                append("Ketentuan Layanan")
                            }
                        },
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol Daftar Utama
                Button(
                    onClick = {
                        viewModel.validateInputRegister(email, password, confirmPassword, fullname, isChecked)
                    },
                    enabled = !viewModel.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Color.Black,
                        disabledContainerColor = Primary.copy(alpha = 0.3f)
                    )
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                    } else {
                        Text("DAFTAR SEKARANG", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    }
                }

                // Link ke Login
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Sudah punya akun? ", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text(
                        text = "Masuk di sini",
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavToLogin() }
                    )
                }
            }
        }
    }

}
