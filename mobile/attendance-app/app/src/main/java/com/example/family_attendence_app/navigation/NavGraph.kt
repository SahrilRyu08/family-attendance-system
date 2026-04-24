package com.example.family_attendence_app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.family_attendence_app.ui.screen.ConfirmationScreen
import com.example.family_attendence_app.ui.screen.HomeScreen
import com.example.family_attendence_app.ui.screen.ManualInputScreen
import com.example.family_attendence_app.ui.screen.ReportScreen
import com.example.family_attendence_app.ui.viewmodel.AttendanceViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    val vm: AttendanceViewModel = viewModel()

    NavHost(navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                vm          = vm,
                onInputManual = { navController.navigate(Screen.ManualInput.route) },
                onReport      = { navController.navigate(Screen.Report.route) }
            )
        }

        composable(Screen.ManualInput.route) {
            ManualInputScreen(
                vm        = vm,
                onBack    = { navController.popBackStack() },
                onSuccess = { nama ->
                    navController.navigate(Screen.Confirmation.createRoute(nama)) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(Screen.Report.route) {
            ReportScreen(
                vm     = vm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Screen.Confirmation.route,
            arguments = listOf(navArgument("nama") { type = NavType.StringType })
        ) { back ->
            ConfirmationScreen(
                nama      = back.arguments?.getString("nama") ?: "",
                onDismiss = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}