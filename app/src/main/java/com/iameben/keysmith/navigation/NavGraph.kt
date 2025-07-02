package com.iameben.keysmith.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iameben.keysmith.ui.screen.main.MainScreen
import com.iameben.keysmith.ui.screen.main.ThemeViewmodel
import com.iameben.keysmith.ui.screen.splash.SplashScreen
import com.iameben.keysmith.ui.screen.splash.SplashViewmodel


@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    themeViewmodel : ThemeViewmodel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier) {

        composable(Routes.SPLASH) {
            val viewModel : SplashViewmodel = hiltViewModel()
            val isReady = viewModel.isReady.collectAsState()
            SplashScreen(
                isReady = isReady.value,
                onNavigateToMain = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.SPLASH) {inclusive = true}
                    }
                }
            )

        }
        composable(Routes.MAIN) {

            MainScreen(
                themeViewmodel = themeViewmodel
            )
        }
    }
}