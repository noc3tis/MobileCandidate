package com.example.mobilecandidate_adrian_rios.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilecandidate_adrian_rios.viewModel.UsersViewModel
import com.example.mobilecandidate_adrian_rios.views.SplashScreen


@Composable
fun NavManager(viewModel: UsersViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Splash") {

        composable("Splash") {
            SplashScreen(navController)
        }
    }
}