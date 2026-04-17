package dev.ferynnd.tugasakhir.ui.layouts.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ferynnd.tugasakhir.R // Pastikan import R untuk icon google
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.viewmodel.AuthViewModel
import dev.ferynnd.tugasakhir.ui.components.LottieDialog
import dev.ferynnd.tugasakhir.ui.layouts.ProfileInput
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.Border
import dev.ferynnd.tugasakhir.ui.theme.Geist
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.colSuccess
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavToHome: () -> Unit,
    onNavToRegister: () -> Unit,
    supabaseClient: SupabaseClient
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(true) }
    val dialogState = viewModel.dialogState

    val googleLoginAction = supabaseClient.composeAuth.rememberSignInWithGoogle(
        onResult = { result ->
            when (result) {
                is NativeSignInResult.Success -> {
                    scope.launch {
                        val user = supabaseClient.auth.currentUserOrNull()
                                   ?: supabaseClient.auth.currentSessionOrNull()?.user
                        Log.d("GOOGLE", "cek ${user}")
                        val email = user?.email
                        Log.d("GOOGLE", "cek ${email}")
                        if (email != null) {
                            viewModel.onGoogleLoginSuccess()
                        } else {
                            Toast.makeText(context, "Gagal mengambil data user", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                is NativeSignInResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    Log.d("GOOGLE", result.message)
                }

                else -> {}
            }
        },
        fallback = {
            scope.launch { supabaseClient.auth.signInWith(Google) }
        }
    )

    Scaffold(
        containerColor = Background // Pastikan ini warna hitam pekat/dark
    ) { paddingValues ->

        dialogState?.let { state ->
            LottieDialog(
                lottieRes = state.lottieRes,
                title = state.title,
                message = state.message,
                colorBg = state.colorBg,
                autoDismiss = state.autoDismiss,
                onConfirm = { viewModel.dismissDialog() },
                onDismiss = { viewModel.dismissDialog() }
            )
        }

        val layotDirection = LocalLayoutDirection.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp, start = paddingValues.calculateStartPadding(layotDirection),end = paddingValues.calculateEndPadding(layotDirection))
        ){
            // Efek Cahaya Glow di pojok kiri atas
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .offset(x = (-150).dp, y = (-100).dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Primary.copy(alpha = 0.15f), Color.Transparent)
                        )
                    )
            )

            // Konten utama yang bisa di-scroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()) // INI AGAR BISA DI-SCROLL
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo dengan tema baru
                Icon(
                    painter = painterResource(id = R.drawable.fitlenlogo),
                    contentDescription = "Logo",
                    tint = Primary,
                    modifier = Modifier.size(180.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Gunakan akun yang terdaftar untuk melanjutkan",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    ProfileInput(
                        label = "Email",
                        placeholder = "Masukkan email Anda",
                        value = email,
                        onValueChange = { email = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ProfileInput(
                        label = "Password",
                        placeholder = "Masukkan kata sandi Anda",
                        value = password,
                        onValueChange = { password = it },
                        isPassword = true
                    )

                }

                TextButton(
                    onClick = { /* Lupa Password */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Lupa kata sandi?", color = Primary, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.validateInputLogin(email, password) },
                    enabled = !viewModel.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    if (viewModel.isLoading) CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                    else Text("MASUK SEKARANG", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Divider
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.DarkGray)
                    Text(" ATAU ", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp))
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.DarkGray)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Google Login
                OutlinedButton(
                    onClick = { googleLoginAction.startFlow() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(Primary.copy(0.2f)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, Primary)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.icgoogle),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("MASUK DENGAN GOOGLE", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, fontFamily = Geist)
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Footer
                Row(modifier = Modifier.padding(bottom = 32.dp)) {
                    Text("Belum punya akun? ", color = Color.Gray, fontSize = 14.sp , fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Daftar",
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavToRegister() }
                    )
                }
            }
        }
    }
    if (viewModel.isSuccess) {
        LottieDialog(
            lottieRes = R.raw.success,
            title = "Login Berhasil",
            colorBg = colSuccess.copy(alpha = 0.15f),
            message = " Proses login berhasil, selamat datang di aplikasi",
            confirmText = "OK",
            autoDismiss = true,
            timeDelay = 3000,
            onConfirm = {
                viewModel.resetSuccess()
                onNavToHome()
            },
            onDismiss = {
                viewModel.resetSuccess()
                onNavToHome()
            }
        )
    }
}