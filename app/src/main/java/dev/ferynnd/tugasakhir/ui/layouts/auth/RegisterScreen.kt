package dev.ferynnd.tugasakhir.ui.layouts.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ferynnd.tugasakhir.data.viewmodel.AuthViewModel
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.theme.TextMain
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.R // Pastikan import R untuk icon google
import dev.ferynnd.tugasakhir.ui.components.LottieDialog
import dev.ferynnd.tugasakhir.ui.theme.Border
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.colEmail
import dev.ferynnd.tugasakhir.ui.theme.colSuccess

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavToLogin: () -> Unit
) {
    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(true) }
    var confirmPasswordVisible by remember { mutableStateOf(true) }
    var isChecked by remember { mutableStateOf(false) }
    val dialogState = viewModel.dialogState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {

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
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = TextMain, fontWeight = FontWeight.Bold)) {
                    append("Create\n")
                }
                withStyle(style = SpanStyle(color = Primary, fontWeight = FontWeight.Bold)) {
                    append("Account")
                }
            },
            fontSize = 32.sp,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Create a new account to get started and enjoy.",
            fontSize = 16.sp,
            color = TextSub,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Fullname", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextSub,  modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = fullname,
                onValueChange = { fullname = it },
                placeholder = { Text("student", color = Color.LightGray) },
                leadingIcon = { CustomIcon(iconRes = R.drawable.icuser, null, tint = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, unfocusedBorderColor = Border)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Email",  fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextSub,  modifier = Modifier.padding(bottom = 8.dp))
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

            Text("Password",  fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextSub,  modifier = Modifier.padding(bottom = 8.dp))
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

            Spacer(modifier = Modifier.height(20.dp))

            Text("Confirm Password",  fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextSub,  modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("••••••••", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.LightGray) },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        CustomIcon(
                            iconRes = if (confirmPasswordVisible) R.drawable.iceyeon else R.drawable.iceyeoff,
                            contentDescription = if (confirmPasswordVisible) "Sembunyikan password" else "Tampilkan password",
                            tint = Color.LightGray
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, unfocusedBorderColor = Border)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = CheckboxDefaults.colors(checkedColor = Primary),
            )
            val annotatedString = buildAnnotatedString {
                append("I agree to the ")
                withStyle(style = SpanStyle(color = Primary, fontWeight = FontWeight.Bold)) {
                    append("Terms of Service")
                }
                append(" and consent to data collection for research purposes.")
            }
            Text(
                text = annotatedString,
                fontSize = 14.sp,
                color = TextSub,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.validateInputRegister(
                email =email,
                pass =password,
                confirmPass =confirmPassword,
                fullname = fullname,
                isChecked = isChecked)
                      },
            enabled = !viewModel.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Create Account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account? ", color = TextSub)
            Text(
                text = "Sign in here",
                color = Primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onNavToLogin() }
            )

        }

    }

}
