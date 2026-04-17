package dev.ferynnd.tugasakhir

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.ferynnd.tugasakhir.data.model.ExerciseCode
import dev.ferynnd.tugasakhir.data.remote.supabase.SupabaseClient
import dev.ferynnd.tugasakhir.data.viewmodel.ExerciseViewModel
import dev.ferynnd.tugasakhir.data.viewmodel.UserViewModel
import dev.ferynnd.tugasakhir.ui.components.LottieDialog
import dev.ferynnd.tugasakhir.ui.layouts.BMIProfileScreen
import dev.ferynnd.tugasakhir.ui.layouts.CameraScreen
import dev.ferynnd.tugasakhir.ui.layouts.EditProfileScreen
import dev.ferynnd.tugasakhir.ui.layouts.ExerciseHistory
import dev.ferynnd.tugasakhir.ui.layouts.ProfileScreen
import dev.ferynnd.tugasakhir.ui.layouts.TrainingSummary
import dev.ferynnd.tugasakhir.ui.theme.colEmail

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val supabaseClient = SupabaseClient()

        val authViewModel = AuthViewModel(supabaseClient)
        val userViewModel = UserViewModel(supabaseClient)
        val exerciseViewModel = ExerciseViewModel(supabaseClient)

        setContent {
            TugasAkhirTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in listOf("home", "profile", "trainingList", "trainingHistory")

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            MainBottomNav(
                                currentDestination = currentDestination,
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
                                        val session = supabaseClient.auth.currentSessionOrNull()
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
                                    onNavToRegister = { navController.navigate("register") },
                                    supabaseClient = supabaseClient
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
                                HomeScreen(navController, exerciseViewModel,userViewModel,  supabaseClient = supabaseClient)
                            }
                            composable("trainingList") {
                                TrainingList(navController)
                            }

                            composable(
                                route = "trainingDetail/{id}",
                                arguments = listOf(
                                    navArgument("id") {
                                        type = NavType.IntType
                                    }
                                )
                            ) { backStackEntry ->

                                val id = backStackEntry.arguments?.getInt("id") ?: 0
                                TrainingDetail(navController, id)
                            }

                            composable("trainingHistory") {
                                ExerciseHistory( exerciseViewModel , supabaseClient = supabaseClient, navController)
                            }

                            composable("profile") {
                                ProfileScreen(navController, userViewModel, exerciseViewModel,  supabaseClient = supabaseClient)
                            }

                            composable("editProfile") {
                                EditProfileScreen(navController, authViewModel, userViewModel,  supabaseClient = supabaseClient)
                            }
                            composable("editBMI") {
                                BMIProfileScreen(navController, userViewModel,  supabaseClient = supabaseClient)
                            }

                            composable(
                                "cameraScan/{exercise}",
                                arguments = listOf(
                                    navArgument("exercise") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->

                                val exerciseArg = backStackEntry.arguments?.getString("exercise")

                                val exerciseType = try {
                                    ExerciseCode.valueOf(exerciseArg ?: ExerciseCode.PUSH_UP.name)
                                } catch (e: Exception) {
                                    ExerciseCode.PUSH_UP
                                }

                                CameraScreen(
                                    navController = navController,
                                    exerciseCode = exerciseType ,
                                    supabaseClient = supabaseClient
                                )
                            }

                            composable("trainingSummary/{historyId}",
                                arguments = listOf(
                                    navArgument("historyId") { type = NavType.IntType }
                                )
                            ) {
                                val historyId = it.arguments?.getInt("historyId") ?: 0
                                TrainingSummary(
                                    historyId = historyId,
                                    viewModel = exerciseViewModel,
                                    onClose = { navController.popBackStack() }
                                )
                            }


                        }
                }
            }
        }
    }

}
