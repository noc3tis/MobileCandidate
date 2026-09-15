package com.example.mobilecandidate_adrian_rios.views

import android.window.SplashScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilecandidate_adrian_rios.data.StoreBoarding
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController){
    val context = LocalContext.current
    val dataStore = StoreBoarding(context)



    val isOnBoardingFinished by dataStore.getBoarding.collectAsState(initial = false)

    LaunchedEffect(key1 = true) {
        delay(2000)

        if(isOnBoardingFinished){
            navController.navigate("Home"){
                popUpTo("Splash") { inclusive = true }
            }
        } else {
            navController.navigate("OnBoarding"){
                popUpTo("Splash") { inclusive = true }
                }
            }
        }

        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                tint =  Color(0xFF1976D2)
            )
        }

}