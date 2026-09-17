package com.example.mobilecandidate_adrian_rios.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobilecandidate_adrian_rios.data.StoreBoarding
import kotlinx.coroutines.launch

/*
 * Pantalla de introducción de la aplicación.
 */
@Composable
fun OnBoardingView(navController: NavController) {

    val context = LocalContext.current

    /*
     * Scope utilizado para ejecutar la operación suspendida
     * de DataStore.
     */
    val scope = rememberCoroutineScope()

    val dataStore = StoreBoarding(context)

    /*
     * Información de las páginas del OnBoarding.
     */
    val pages = listOf(

        Pair(
            "Bienvenido",
            "Conoce a gente de todo el mundo."
        ),

        Pair(
            "Detalles",
            "Consulta su información para poder contactarlos fácilmente."
        )
    )

    /*
     * Página actualmente mostrada.
     */
    var currentPage by remember {
        mutableStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Spacer(
            modifier = Modifier.weight(1f)
        )

        /*
         * Título de la página.
         */
        Text(
            text = pages[currentPage].first,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2)
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        /*
         * Descripción.
         */
        Text(
            text = pages[currentPage].second,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        /*
         * Indicadores de página.
         */
        Row {

            repeat(pages.size) { index ->

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .background(

                            color =
                                if (index == currentPage)
                                    Color(0xFF1976D2)
                                else
                                    Color.LightGray,

                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        /*
         * Botón para avanzar.
         */
        Button(

            onClick = {

                if (currentPage < pages.size - 1) {

                    /*
                     * Avanza a la siguiente página.
                     */
                    currentPage++

                } else {

                    /*
                     * El usuario terminó el OnBoarding.
                     */
                    scope.launch {

                        dataStore.saveBoarding(true)

                        /*
                         * Se navega a Home y se elimina
                         * el OnBoarding del historial.
                         */
                        navController.navigate("Home") {

                            popUpTo("OnBoarding") {
                                inclusive = true
                            }
                        }
                    }
                }
            },

            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(12.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2)
            )
        ) {

            if (currentPage < pages.size - 1) {

                Text("Siguiente")

            } else {

                Text("Comenzar")
            }
        }
    }
}