package com.iameben.keysmith.util

import androidx.navigation.NavController

fun goBack(navController: NavController) {
    navController.popBackStack()
}