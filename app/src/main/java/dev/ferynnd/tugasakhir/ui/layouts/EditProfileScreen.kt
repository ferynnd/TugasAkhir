package dev.ferynnd.tugasakhir.ui.layouts

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.ferynnd.tugasakhir.ui.components.CustomIcon
import dev.ferynnd.tugasakhir.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.viewmodel.AuthViewModel
import dev.ferynnd.tugasakhir.data.viewmodel.UserViewModel
import dev.ferynnd.tugasakhir.ui.components.LottieDialog
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Border
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.TextSub
import dev.ferynnd.tugasakhir.ui.theme.White
import dev.ferynnd.tugasakhir.ui.theme.colError
import dev.ferynnd.tugasakhir.ui.theme.colFire
import dev.ferynnd.tugasakhir.ui.theme.colSuccess
import dev.ferynnd.tugasakhir.ui.theme.colWarning
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun EditProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel = hiltViewModel(),
    supabaseClient: SupabaseClient
) {

    val user = remember { supabaseClient.auth.currentUserOrNull() }
    val isGoogleUser = user?.appMetadata?.get("provider")?.jsonPrimitive?.contentOrNull == "google"
    val context = LocalContext.current

    var logoutDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    val dialogState by remember { derivedStateOf { authViewModel.dialogState } }

    var fullName by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(user?.id) {
        user?.id?.let { id ->
            userViewModel.getProfile(id)
        }
    }

    LaunchedEffect(userViewModel.fullName, userViewModel.avatar, userViewModel.email) {
        fullName = userViewModel.fullName
        avatar = userViewModel.avatar
        email = userViewModel.email
    }

    val isChanged by remember {
        derivedStateOf {
            fullName != userViewModel.fullName || avatar != userViewModel.avatar
        }
    }

    LaunchedEffect(userViewModel.isSuccess) {
        if (userViewModel.isSuccess) {
            showSuccessDialog= true
            userViewModel.isSuccess = false // RESET agar tidak muncul terus
        }
    }
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            avatar = it.toString()
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
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
                    text = "UBAH PROFIL",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = White
                )

            }
        },
        containerColor = Background,
    ) { padding ->

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
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(24.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(avatar)
                            .size(128) // sesuai UI
                            .crossfade(false)
                            .build(),
                        contentDescription = "Image Profil User",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(3.dp, Primary, CircleShape),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(R.drawable.placeholder), error = painterResource(R.drawable.placeholder)
                    )
                }

                Surface(
                    color = Primary,
                    shape = RoundedCornerShape(100),
                    onClick = { launcher.launch("image/*") }, // Buka galeri
                ) {
                    Text(
                        "UBAH GAMBAR",
                        fontWeight = FontWeight.SemiBold,
                        color = Black,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // Fullname
            ProfileInput(
                label = "Fullname",
                value = fullName,
                onValueChange = { fullName = it }
            )

            Spacer(Modifier.height(12.dp))

            // Email
            ProfileInput(
                label = "Email",
                value = email,
                onValueChange = {},
                enabled = false
            )

            Spacer(Modifier.height(20.dp))

            if(isChanged)
            {
                Button(
                        onClick = {
                            scope.launch {
                                userViewModel.updateProfile(
                                    userId = user?.id ?: "",
                                    name = fullName,
                                    imageUri = avatar, // Ini bisa berupa URL lama atau URI baru
                                    context = context
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = if(userViewModel.isLoading) ButtonDefaults.buttonColors(Color.LightGray)
                                else ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(14.dp),
                         contentPadding = PaddingValues(
                            vertical = 14.dp,
                            horizontal = 16.dp
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            if (userViewModel.isLoading) Text("Loading...", fontWeight = FontWeight.SemiBold, color = White)
                            else Text("Save Changes", fontWeight = FontWeight.SemiBold, color = White)
                        }
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Border)
                Text(" Or Additional ", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 8.dp))
                HorizontalDivider(modifier = Modifier.weight(1f), color = Border)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if(!isGoogleUser) {
                OutlinedButton(
                    onClick = {
                        showPasswordDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, TextSub),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = TextSub.copy(alpha = 0.15f), // Pindahkan background ke sini agar rapi
                        contentColor = TextSub
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp) // Padding internal button 8dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start // Memaksa isi ke kiri
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.lockk),
                            contentDescription = null,
                            tint = TextSub,
                            modifier = Modifier.size(20.dp) // Ukuran icon sedikit diperbesar agar proporsional
                        )
                        Spacer(Modifier.width(12.dp)) // Jarak antara icon dan teks
                        Text(
                            text = "UBAH PASSWORD",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = TextSub
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Logout Button
            OutlinedButton(
                onClick = {
                    logoutDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, colFire),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = colFire.copy(alpha = 0.15f),
                    contentColor = colFire
                ),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start // Memaksa isi ke kiri
                ) {
                    Icon(
                        painter = painterResource(R.drawable.logoout),
                        contentDescription = null,
                        tint = colFire,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "KELUAR  AKUN",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = colFire
                    )
                }
            }
        }

        if (logoutDialog) {
            LogoutDialog(
                onDismissRequest = { logoutDialog = false },
                onConfirmLogout = {
                    logoutDialog = false // Tutup dialog dulu
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        if (showSuccessDialog) {
            ShowDialog(
                textMessage = userViewModel.textMessage,
                onConfirm = { showSuccessDialog = false },
                onDismiss = { showSuccessDialog = false }
            )
        }

        if (showPasswordDialog) {
            ChangePasswordDialog(
                onDismissRequest = { showPasswordDialog = false },
                onConfirm = { oldPass, newPass ->
                    showPasswordDialog = false
                    authViewModel.changePassword(
                        email = authViewModel.email, // Ambil state email dari ViewModel
                        oldPassword = oldPass,
                        newPassword = newPass
                    )
                }
            )
        }

    }
}

// ==========================================================
//                   COMPONENT SECTION
// ==========================================================


@Composable
fun ProfileInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    enabled: Boolean = true,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    Column {
        Text(
            text = label.uppercase(),
            fontWeight = FontWeight.Bold,
            color = TextSub,
            fontSize = 12.sp

        )

        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            placeholder = {
                if (placeholder != null) Text(text = placeholder.toString(), color = Color.LightGray) else Text(text = "")
            },
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                // Warna background saat aktif/mati
                focusedContainerColor = Primary.copy(alpha = 0.2f),
                unfocusedContainerColor = Card,
                disabledContainerColor = Black,

                focusedBorderColor = Primary,
                unfocusedBorderColor = TextSub,
                disabledBorderColor = TextSub.copy(alpha = 0.3f)
            ),
            textStyle = TextStyle(fontSize = 16.sp, color = White),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
            ),
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.iceyeon else R.drawable.iceyeoff
                            ),
                            contentDescription = null,
                            tint = if (passwordVisible) Primary else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun LogoutDialog(
    onDismissRequest: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    LottieDialog(
        lottieRes = R.raw.warning,
        title = "Logout",
        message = "Apakah Anda yakin ingin keluar?",
        confirmText = "Ya",
        dismissText = "Tidak",
        colorBg = colWarning.copy(alpha = 0.15f),
        onConfirm = {
            onConfirmLogout()
        },
        onDismiss = {
            onDismissRequest()
        }
    )
}

@Composable
fun ShowDialog(
    textMessage : String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    LottieDialog(
        lottieRes = R.raw.success,
        title = "Berhasil",
        colorBg = colSuccess.copy(alpha = 0.15f),
        message = textMessage,
        confirmText = "OK",
        autoDismiss = true,
        timeDelay = 3000,
        onConfirm = {
            onConfirm()
        },
        onDismiss = {
            onDismiss()
        }
    )
}


@Composable
fun ChangePasswordDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (oldPassword: String, newPassword: String) -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isPasswordMismatch =
        newPassword.isNotEmpty() &&
        confirmPassword.isNotEmpty() &&
        newPassword != confirmPassword

    val isFormValid =
        oldPassword.isNotBlank() &&
        newPassword.isNotBlank() &&
        confirmPassword.isNotBlank() &&
        !isPasswordMismatch

    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Change Password",
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                ProfileInput(
                    label = "Current Password",
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    isPassword = true
                )

                ProfileInput(
                    label = "New Password",
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    isPassword = true
                )

                ProfileInput(
                    label = "Confirm New Password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    isPassword = true
                )

                if (isPasswordMismatch) {
                    Text(
                        text = "Passwords do not match",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid) {
                        onConfirm(oldPassword, newPassword)
                    }
                },
                enabled = isFormValid,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    disabledContainerColor = Primary.copy(alpha = 0.4f)
                )
            ) {
                Text("Update Password", color = White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = TextSub
                )
            ) {
                Text("Cancel", color = TextSub)
            }
        }
    )
}
