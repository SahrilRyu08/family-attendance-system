package com.example.family_attendence_app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.family_attendence_app.navigation.AppNavGraph
import com.example.family_attendence_app.ui.theme.FamilyAttendanceTheme

class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Enable edge-to-edge
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//
//        setContent {
//            val darkTheme = isSystemInDarkTheme()
//            val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
//
//            // Update status bar color
//            SideEffect {
//                window.statusBarColor = colorScheme.primary.toArgb()
//                WindowCompat.getInsetsController(window, window.decorView).apply {
//                    isAppearanceLightStatusBars = !darkTheme
//                }
//            }
//
//            MaterialTheme(
//                colorScheme = colorScheme,
//                typography = Typography()
//            ) {
//                AppNavHost()
//            }
//        }
//    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FamilyAttendanceTheme {
                AppNavGraph()
            }
        }
    }
}