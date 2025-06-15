package com.iameben.keysmith

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.iameben.keysmith.navigation.NavGraph
import com.iameben.keysmith.ui.theme.KeySmithTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KeySmithTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController
                )
            }
        }

    }
}
