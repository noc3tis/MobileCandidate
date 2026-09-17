package com.example.mobilecandidate_adrian_rios.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilecandidate_adrian_rios.data.StoreBoarding
import kotlinx.coroutines.delay

/*
 * Pantalla Splash que se muestra al iniciar la aplicación.
 */
@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current

    /*
     * Acceso al DataStore que indica si el OnBoarding
     * ya fue completado anteriormente.
     */
    val dataStore = StoreBoarding(context)

    val isOnBoardingFinished by
    dataStore.getBoarding.collectAsState(
        initial = false
    )

    /*
     * La navegación se realiza una sola vez.
     */
    LaunchedEffect(Unit) {

        /*
         * Pequeño tiempo de presentación del Splash.
         */
        delay(2000)

        if (isOnBoardingFinished) {

            /*
             * Si el usuario ya vio el OnBoarding,
             * se dirige directamente a Home.
             */
            navController.navigate("Home") {

                popUpTo("Splash") {
                    inclusive = true
                }
            }

        } else {

            /*
             * Primera ejecución:
             * se muestra el OnBoarding.
             */
            navController.navigate("OnBoarding") {

                popUpTo("Splash") {
                    inclusive = true
                }
            }
        }
    }

    /*
     * Contenido visual del Splash.
     */
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = Icons.Filled.Face,
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp),
            tint = Color(0xFF1976D2)
        )
    }
}