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
import dev.ferynnd.tugasakhir.data.viewmodel.AuthViewModel
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.ui.theme.TextMain
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.R // Pastikan import R untuk icon google
import dev.ferynnd.tugasakhir.ui.theme.Border
import dev.ferynnd.tugasakhir.ui.theme.Primary

@Composable
fun RegisterScreen(viewModel: AuthViewModel, onNavToLogin: () -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = TextMain, fontWeight = FontWeight.Bold)) {
                    append("Create Your\n")
                }
                withStyle(style = SpanStyle(color = Primary, fontWeight = FontWeight.Bold)) {
                    append("Profile")
                }
            },
            fontSize = 36.sp,
            lineHeight = 42.sp
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Start your fitness evaluation journey with BlazePose analysis.",
            fontSize = 16.sp,
            color = TextSub,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Full Name", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("Student", color = Color.LightGray) },
                leadingIcon = { CustomIcon(iconRes = R.drawable.icuser, null, tint = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary, unfocusedBorderColor = Border)
            )

            Spacer(modifier = Modifier.height(20.dp))

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

            Spacer(modifier = Modifier.height(20.dp))

            Text("Confirm Password", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
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


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = CheckboxDefaults.colors(checkedColor = Primary)
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

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.onRegister(email, password, fullName) },
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
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
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
                text = "Log in",
                color = Primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavToLogin() }
            )
        }
    }
}
