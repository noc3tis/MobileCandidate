package com.example.mobilecandidate_adrian_rios.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/*
 * Clase encargada de guardar información relacionada
 * con el estado del OnBoarding.
 *
 * DataStore permite conservar esta información incluso
 * después de cerrar la aplicación.
 */
class StoreBoarding(
    private val context: Context
) {

    companion object {

        /*
         * Se crea un único DataStore asociado al contexto.
         */
        private val Context.dataStore: DataStore<Preferences>
                by preferencesDataStore("settings")

        /*
         * Clave utilizada para saber si el usuario
         * ya terminó el OnBoarding.
         */
        val STORE_BOARDING =
            booleanPreferencesKey("store_boarding")
    }

    /*
     * Obtiene el estado actual del OnBoarding.
     *
     * Si nunca se ha guardado un valor, devuelve false.
     */
    val getBoarding: Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[STORE_BOARDING] ?: false
        }

    /*
     * Guarda si el OnBoarding ya fue completado.
     */
    suspend fun saveBoarding(completed: Boolean) {

        context.dataStore.edit { preferences ->

            preferences[STORE_BOARDING] = completed
        }
    }
}