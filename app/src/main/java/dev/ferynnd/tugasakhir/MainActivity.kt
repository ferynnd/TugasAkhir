package dev.ferynnd.tugasakhir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.repository.AuthRepository
import dev.ferynnd.tugasakhir.data.viewmodel.AuthViewModel
import dev.ferynnd.tugasakhir.ui.components.MainBottomNav
import dev.ferynnd.tugasakhir.ui.layouts.HomeScreen
import dev.ferynnd.tugasakhir.ui.layouts.TrainingDetail
import dev.ferynnd.tugasakhir.ui.layouts.TrainingList
import dev.ferynnd.tugasakhir.ui.layouts.auth.LoginScreen
import dev.ferynnd.tugasakhir.ui.layouts.auth.RegisterScreen
import dev.ferynnd.tugasakhir.ui.layouts.auth.SplashScreenNavigation
import dev.ferynnd.tugasakhir.ui.theme.TugasAkhirTheme
import io.github.jan.supabase.auth.auth
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dev.ferynnd.tugasakhir.data.helper.SharedPreferenceHelper
import dev.ferynnd.tugasakhir.data.viewmodel.UserViewModel
import dev.ferynnd.tugasakhir.ui.components.LottieDialog
import dev.ferynnd.tugasakhir.ui.layouts.BMIProfileScreen
import dev.ferynnd.tugasakhir.ui.layouts.CameraScreen
import dev.ferynnd.tugasakhir.ui.layouts.EditProfileScreen
import dev.ferynnd.tugasakhir.ui.layouts.ProfileScreen
import dev.ferynnd.tugasakhir.ui.theme.colEmail
import dev.ferynnd.tugasakhir.ui.theme.colSuccess

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = AuthRepository(SupabaseClient.client)
        val authViewModel = AuthViewModel(repository)
        val userViewModel = UserViewModel(SupabaseClient)

        setContent {
            TugasAkhirTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in listOf("home", "profile", "trainingList")

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            MainBottomNav(
                                currentRoute = currentRoute,
                                onNavigate = { targetRoute ->
                                    navController.navigate(targetRoute) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "splash",
                            modifier = androidx.compose.ui.Modifier.padding(innerPadding)

                        ) {
                            composable("splash") {
                                SplashScreenNavigation(
                                    onNavigationComplete = {
                                        // Cek session terbaru langsung saat navigasi selesai
                                        val session = SupabaseClient.client.auth.currentSessionOrNull()
                                        val dest = if (session != null) "home" else "login"

                                        navController.navigate(dest) {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("login") {
                                LoginScreen(
                                    viewModel = authViewModel,
                                    onNavToHome = {
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    },
                                    onNavToRegister = { navController.navigate("register") }
                                )
                            }

                            composable("register") {
                                RegisterScreen(
                                    viewModel = authViewModel,
                                    onNavToLogin = { navController.popBackStack() },
                                )
                                if (authViewModel.isSuccess) {
                                    LottieDialog(
                                        lottieRes = R.raw.email,
                                        title = "Pendaftaran Berhasil",
                                        colorBg = colEmail.copy(alpha = 0.15f),
                                        message = "Kami telah mengirim email aktivasi.\nSilakan cek email kamu untuk mengaktifkan akun.",
                                        confirmText = "OK",
                                        autoDismiss = true,
                                        timeDelay = 5000,
                                        onConfirm = {
                                            authViewModel.resetSuccess()
                                            navController.navigate("login") {
                                                popUpTo("register") { inclusive = true }
                                            }
                                        },
                                        onDismiss = {
                                            authViewModel.resetSuccess()
                                            navController.navigate("login") {
                                                popUpTo("register") { inclusive = true }
                                            }
                                        }
                                    )
                                }
                            }
                            composable("home") {
                                HomeScreen(navController, userViewModel)
                            }
                            composable("trainingList") {
                                TrainingList(navController)
                            }
                            composable("trainingDetail") {
                                TrainingDetail(navController)
                            }
                            composable("profile") {
                                ProfileScreen(navController, userViewModel)
                            }

                            composable("editProfile") {
                                EditProfileScreen(navController, authViewModel, userViewModel)
                            }
                            composable("editBMI") {
                                BMIProfileScreen(navController, userViewModel)
                            }
                            composable(
                                "cameraScan/{exercise}",
                                arguments = listOf(
                                    navArgument("exercise") { type = NavType.StringType }
                                )
                            ) {
                                CameraScreen(navController)
                            }

                        }
                }
            }
        }
    }
}
