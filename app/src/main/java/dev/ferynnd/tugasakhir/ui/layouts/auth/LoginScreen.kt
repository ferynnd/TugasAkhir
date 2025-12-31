package dev.ferynnd.tugasakhir.ui.layouts.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.R // Pastikan import R untuk icon google
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.viewmodel.AuthViewModel
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.theme.Border
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.TextMain
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: AuthViewModel, onNavToHome: () -> Unit, onNavToRegister: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val googleLoginAction = SupabaseClient.client.composeAuth.rememberSignInWithGoogle(
        onResult = { result ->
            when (result) {
                is NativeSignInResult.Success -> {
                    scope.launch {
                        val user = SupabaseClient.client.auth.currentUserOrNull()
                                   ?: SupabaseClient.client.auth.currentSessionOrNull()?.user
                        val email = user?.email
                        if (email != null) {
                            viewModel.onGoogleLoginSuccess(email)
                            onNavToHome()
                        } else {
                            Toast.makeText(context, "Gagal mengambil data user", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                is NativeSignInResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        },
        fallback = {
            scope.launch { SupabaseClient.client.auth.signInWith(Google) }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    brush = Brush.verticalGradient(listOf(Primary, Color(0xFFB0161D))),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            CustomIcon(
                iconRes = R.drawable.jump,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome Back",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = TextMain
        )
        Text(
            text = "Sign in to continue your movement\nanalysis and research.",
            fontSize = 16.sp,
            color = TextSub,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Email", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("students@pnm.ac.id", color = Color.LightGray) },
                leadingIcon = { CustomIcon(R.drawable.icmail, null, tint = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, unfocusedBorderColor = Border)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Password", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.LightGray) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        CustomIcon(
                            iconRes = if (passwordVisible) R.drawable.iceyeon else R.drawable.iceyeoff,
                            contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password",
                            tint = Color.LightGray
                        )
                    }
                },
                visualTransformation = if (passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, unfocusedBorderColor = Border)
            )
        }

        TextButton(
            onClick = {},
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Forgot password?", color = Primary, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.onLogin(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            if (viewModel.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("Log In", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

//        Row(verticalAlignment = Alignment.CenterVertically) {
//            HorizontalDivider(modifier = Modifier.weight(1f), color = Border)
//            Text(" Or continue with ", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 8.dp))
//            HorizontalDivider(modifier = Modifier.weight(1f), color = Border)
//        }

//        Spacer(modifier = Modifier.height(24.dp))
//
//        OutlinedButton(
//            onClick = { googleLoginAction.startFlow() },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp),
//            shape = RoundedCornerShape(12.dp),
//            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
//            border = androidx.compose.foundation.BorderStroke(1.dp, Border)
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    painter = painterResource(id = R.drawable.icgoogle), // Ganti dengan R.drawable.ic_google jika ada
//                    contentDescription = null,
//                    modifier = Modifier.size(20.dp),
//                    tint = Color.Unspecified
//                )
//                Spacer(modifier = Modifier.width(12.dp))
//                Text("Sign in with Google", fontWeight = FontWeight.Bold)
//            }
//        }

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.padding(bottom = 32.dp)) {
            Text("Don't have an account? ", color = Color.Gray)
            Text(
                text = "Sign up",
                color = Primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                        try {
                            onNavToRegister()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
            )
        }
    }
}