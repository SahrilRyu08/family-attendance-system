package com.example.family_attendence_app.navigation

sealed class Screen(val route: String) {
    object EventList : Screen("event_list")
    object CheckIn : Screen("check_in/{eventId}") {
        fun createRoute(eventId: Long) = "check_in/$eventId"
    }
    object Report : Screen("report/{eventId}") {
        fun createRoute(eventId: Long) = "report/$eventId"
    }
}