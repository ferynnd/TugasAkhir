package dev.ferynnd.tugasakhir.ui.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ferynnd.tugasakhir.ui.theme.Primary // Warna merah Anda
import dev.ferynnd.tugasakhir.R
import dev.ferynnd.tugasakhir.ui.theme.Background
import dev.ferynnd.tugasakhir.ui.theme.Card
import dev.ferynnd.tugasakhir.ui.theme.White

@Composable
fun MainBottomNav (
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    Box(contentAlignment = Alignment.BottomCenter) {
     NavigationBar(
            containerColor = Card,
            tonalElevation = 1.dp,
        ) {
            NavigationBarItem(
                selected = currentRoute == "home",
                onClick = { onNavigate("home") },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.homes),
                        contentDescription = "Home",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("Beranda", fontSize = 12.sp) },
                colors = navColors()
            )

            NavigationBarItem(
                selected = currentRoute == "trainingList",
                onClick = { onNavigate("trainingList") },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.iclist),
                        contentDescription = "Exercise",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("Latihan", fontSize = 12.sp) },
                colors = navColors()
            )

            NavigationBarItem(
                selected = currentRoute == "trainingHistory",
                onClick = { onNavigate("trainingHistory") },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ichistory),
                        contentDescription = "History",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text("Riwayat", fontSize = 12.sp) },
                colors = navColors()
            )

            NavigationBarItem(
                selected = currentRoute == "profile",
                onClick = { onNavigate("profile") },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.profileline),
                        contentDescription = "Profile",
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = { Text("Profil", fontSize = 12.sp) },
                colors = navColors()
            )
        }
    }
}

@Composable
private fun navColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Primary,
    selectedTextColor = Primary,
    unselectedIconColor = Color.Gray,
    unselectedTextColor = Color.Gray,
    indicatorColor = Color.Transparent
)
