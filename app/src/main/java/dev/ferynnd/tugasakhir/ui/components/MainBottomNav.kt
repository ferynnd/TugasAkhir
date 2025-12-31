package dev.ferynnd.tugasakhir.ui.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.ui.theme.Primary // Warna merah Anda
import dev.ferynnd.tugasakhir.R

@Composable
fun MainBottomNav (
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    Box(contentAlignment = Alignment.BottomCenter) {
        BottomAppBar(
            containerColor = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier.height(80.dp),
            actions = {
                // Item Sebelah Kiri (Home)
                NavigationBarItem(
                    selected = currentRoute == "home",
                    onClick = { onNavigate("home") },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.homes),
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Home", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Primary,
                        selectedTextColor = Primary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                Spacer(Modifier.weight(1f))

                NavigationBarItem(
                    selected = currentRoute == "profile",
                    onClick = { onNavigate("profile") },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = "Settings",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Settings", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Primary,
                        selectedTextColor = Primary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        )

        // Tombol Tengah yang Menonjol (Floating Action Button)
        FloatingActionButton(
            onClick = { onNavigate("cameraScan") },
            shape = CircleShape,
            containerColor = Primary,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            modifier = Modifier
                .offset(y = (-30).dp) // Mengangkat tombol ke atas agar menonjol
                .size(64.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icbodyscan), // Icon kotak bidik tengah
                contentDescription = "Scan",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}