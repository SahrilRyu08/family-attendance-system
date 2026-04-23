package com.example.family_attendence_app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.family_attendence_app.ui.screen.CheckInScreen
import com.example.family_attendence_app.ui.screen.EventScreen
import com.example.family_attendence_app.ui.screen.ManualInputScreen
import com.example.family_attendence_app.ui.screen.ReportScreen
import com.example.family_attendence_app.ui.viewmodel.CheckInViewModel
import com.example.family_attendence_app.ui.viewmodel.ReportViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.EventList.route
    ) {
        // ✅ Screen 1: Event List
        composable(Screen.EventList.route) {
            EventScreen(
                onEventSelected = { eventId ->
                    navController.navigate(Screen.CheckIn.createRoute(eventId))
                }
            )
        }

        // ✅ Screen 2: Check-In (dengan autocomplete)
        composable(
            route = Screen.CheckIn.route,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: return@composable
            val viewModel: CheckInViewModel = viewModel()

            ManualInputScreen(
                viewModel = viewModel,
                onNavigateToReport = {
                    navController.navigate(Screen.Report.createRoute(eventId)) {
                        popUpTo(Screen.CheckIn.createRoute(eventId)) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ✅ Screen 3: Report
        composable(
            route = Screen.Report.route,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: return@composable
            val viewModel: ReportViewModel = viewModel()

            ReportScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCheckInClick = {
                    navController.navigate(Screen.CheckIn.createRoute(eventId)) {
                        popUpTo(Screen.Report.createRoute(eventId)) { inclusive = true }
                    }
                }
            )
        }
    }
}
