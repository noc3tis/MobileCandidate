package com.example.mobilecandidate_adrian_rios.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilecandidate_adrian_rios.viewModel.UsersViewModel
import com.example.mobilecandidate_adrian_rios.views.FavoritesView
import com.example.mobilecandidate_adrian_rios.views.HomeView
import com.example.mobilecandidate_adrian_rios.views.OnBoardingView
import com.example.mobilecandidate_adrian_rios.views.SplashScreen
import com.example.mobilecandidate_adrian_rios.views.UserDetailView

/*
 * Administrador de navegación de la aplicación.
 *
 * Se utiliza Navigation Compose para cambiar entre:
 *
 * Splash
 * OnBoarding
 * Home
 * Favorites
 * UserDetail
 */
@Composable
fun NavManager(viewModel: UsersViewModel) {

    /*
     * Controlador encargado de manejar la navegación.
     */
    val navController = rememberNavController()

    NavHost(
        navController = navController,

        /*
         * La aplicación comienza mostrando el Splash.
         */
        startDestination = "Splash"
    ) {

        /*
         * Pantalla inicial.
         */
        composable("Splash") {

            SplashScreen(navController)
        }

        /*
         * Introducción de la aplicación.
         */
        composable("OnBoarding") {

            OnBoardingView(navController)
        }

        /*
         * Pantalla principal.
         *
         * Se utiliza el mismo ViewModel.
         */
        composable("Home") {

            HomeView(
                navController,
                viewModel
            )
        }

        /*
         * Pantalla de favoritos.
         */
        composable("Favorites") {

            FavoritesView(
                navController,
                viewModel
            )
        }

        /*
         * Pantalla de información detallada.
         */
        composable("UserDetail") {

            val usuario =
                viewModel.usuarioSeleccionado

            if (usuario != null) {

                UserDetailView(
                    navController = navController,
                    usuario = usuario
                )

            } else {

                /*
                 * Protección por si se intenta entrar
                 * al detalle sin haber seleccionado un usuario.
                 */
                navController.popBackStack()
            }
        }
    }
}