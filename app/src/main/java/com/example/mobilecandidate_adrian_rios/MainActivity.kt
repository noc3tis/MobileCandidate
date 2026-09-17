package com.example.mobilecandidate_adrian_rios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.mobilecandidate_adrian_rios.di.AppModule
import com.example.mobilecandidate_adrian_rios.navigation.NavManager
import com.example.mobilecandidate_adrian_rios.viewModel.UsersViewModel

class MainActivity : ComponentActivity() {

    /*
     * El ViewModel se obtiene mediante el sistema de ViewModels de Android.
     *
     */
    private val viewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
         * Inicializamos la base de datos antes de utilizar el ViewModel,
         * ya que el ViewModel necesita acceder a Room desde su inicialización.
         */
        AppModule.initialize(applicationContext)

        setContent {
            /*
             * El mismo ViewModel se comparte entre las diferentes pantallas.
             * Esto permite compartir:
             * - Lista de usuarios
             * - Favoritos
             * - Usuario seleccionado
             * - Estado de carga
             * - Errores
             */
            NavManager(viewModel)
        }
    }
}