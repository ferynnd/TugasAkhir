package dev.ferynnd.tugasakhir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import dev.ferynnd.tugasakhir.data.helper.SharedPreferenceHelper
import dev.ferynnd.tugasakhir.ui.layouts.BMIProfileScreen
import dev.ferynnd.tugasakhir.ui.layouts.CameraScreen
import dev.ferynnd.tugasakhir.ui.layouts.ProfileScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        val repository = AuthRepository(SupabaseClient.client)
        val pref = SharedPreferenceHelper(this)
        val authViewModel = AuthViewModel(repository, pref)

        setContent {
            TugasAkhirTheme {
                val navController = rememberNavController()

                val currentUser = SupabaseClient.client.auth.currentUserOrNull()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in listOf("home", "profile", "trainingList", "trainingDetail", "settings")

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
                                        val dest = if (authViewModel.isUserLoggedIn() && currentUser != null) "home" else "login"
//                                        val dest = if (currentUser != null) "home" else "login"
                                        navController.navigate(dest) {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("login") {
                                LoginScreen(
                                    viewModel = authViewModel,
                                    onNavToHome = { navController.navigate("home") },
                                    onNavToRegister = { navController.navigate("register") }
                                )

                                // Jika login berhasil, pindah ke home
                                if (authViewModel.isSuccess) {
                                    LaunchedEffect(Unit) {
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                        authViewModel.isSuccess = false // Reset state
                                    }
                                }
                            }

                            composable("register") {
                                RegisterScreen(
                                    viewModel = authViewModel,
                                    onNavToLogin = { navController.popBackStack() }
                                )

                                // Jika registrasi berhasil (biasanya minta cek email dulu atau langsung login)
                                if (authViewModel.isSuccess) {
                                    LaunchedEffect(Unit) {
                                        navController.navigate("login") {
                                            popUpTo("register") { inclusive = true }
                                        }
                                        authViewModel.isSuccess = false // Reset state
                                    }
                                }
                            }
                            composable("home") {
                                HomeScreen(navController)
                            }
                            composable("trainingList") {
                                TrainingList(navController)
                            }
                            composable("trainingDetail") {
                                TrainingDetail(navController)
                            }
                            composable("settings") {
                                // Settings Screen
                            }
                            composable("profile") {
                                ProfileScreen(navController)
                            }
                            composable("bmiProfile") {
                                BMIProfileScreen(navController)
                            }
                            composable("cameraScan") {
                                CameraScreen(navController)
                            }

                        }
                }
            }
        }
    }
}
