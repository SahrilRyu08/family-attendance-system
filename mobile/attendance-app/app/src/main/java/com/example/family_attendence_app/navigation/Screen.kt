package com.example.family_attendence_app.navigation

sealed class Screen(val route: String) {
    data object Home         : Screen("home")
    data object ManualInput  : Screen("manual_input")
    data object Report       : Screen("report")
    data object Confirmation : Screen("confirmation/{nama}") {
        fun createRoute(nama: String) = "confirmation/$nama"
    }
}