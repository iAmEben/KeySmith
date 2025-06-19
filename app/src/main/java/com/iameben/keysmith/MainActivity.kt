package com.iameben.keysmith

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.iameben.keysmith.navigation.NavGraph
import com.iameben.keysmith.ui.screen.main.ThemeViewmodel
import com.iameben.keysmith.ui.theme.KeySmithTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewmodel: ThemeViewmodel = hiltViewModel()
            val isDark by themeViewmodel.isDarkTheme.collectAsState()
            val isSystemInDarkTheme = isSystemInDarkTheme()

            LaunchedEffect(Unit) {
                themeViewmodel.initializeTheme(isSystemInDarkTheme)
            }

            KeySmithTheme(darkTheme = isDark) {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    themeViewmodel = themeViewmodel
                )
            }
        }

    }
}
