package dev.ferynnd.tugasakhir.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.Black
import dev.ferynnd.tugasakhir.ui.theme.Input
import dev.ferynnd.tugasakhir.ui.theme.Primary
import dev.ferynnd.tugasakhir.ui.theme.White

@Composable
fun EditProfileScreen(navController: NavController) {

    var fullName by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("john@mail.com") }

    Scaffold(
        containerColor = Color.White,
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
                       .background(Color(0xFFE5E5E5), CircleShape)

                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Black
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Your Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

            }
        },
//        bottomBar = {
//            Button(
//                onClick = { /* save */ },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(20.dp)
//                    .height(56.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Primary),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Text(
//                    "Save Changes",
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp
//                )
//            }
//        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(24.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0xFFE0E0E0), CircleShape)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Change Photo",
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(32.dp))

            // Fullname
            ProfileInput(
                label = "Fullname",
                value = fullName,
                onValueChange = { fullName = it }
            )

            Spacer(Modifier.height(20.dp))

            // Email
            ProfileInput(
                label = "Email",
                value = email,
                onValueChange = {},
                enabled = false
            )

            Spacer(Modifier.height(28.dp))

            // Change Password
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* navigate change password */ }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.lockk),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Change Password",
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.logoout),
                        contentDescription = null
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Logout
            OutlinedButton(
                onClick = { /* logout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.Red),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Red
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.icbolt),
                    contentDescription = null,
                    tint = Color.Red
                )
                Spacer(Modifier.width(8.dp))
                Text("Log Out", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}


@Composable
fun ProfileInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true
) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )

        Spacer(Modifier.height(6.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color(0xFFF0F0F0),
                focusedContainerColor = Color(0xFFF0F0F0),
                unfocusedContainerColor = Color(0xFFF0F0F0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}
