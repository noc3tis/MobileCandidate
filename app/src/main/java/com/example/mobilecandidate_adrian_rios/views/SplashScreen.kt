package com.example.mobilecandidate_adrian_rios.views

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.mobilecandidate_adrian_rios.data.StoreBoarding
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController){
    val context = LocalContext.current
    val dataStore = StoreBoarding(context)



    val isOnBoardingFinished by dataStore.getBoarding.collectAsState(initial = false)

    LaunchedEffect(key1 = true) {
        delay(2000)
    }
}