package com.example.mobilecandidate_adrian_rios.di

import android.content.Context
import androidx.room.Room
import com.example.mobilecandidate_adrian_rios.data.ApiRandom
import com.example.mobilecandidate_adrian_rios.data.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
 * Módulo sencillo encargado de proporcionar las dependencias
 * principales de la aplicación.
 *
 * En este proyecto se utiliza como una alternativa sencilla
 * a implementar un framework de inyección como Hilt.
 */
object AppModule {

    /*
     * URL base de RandomUser.
     */
    private const val BASE_URL =
        "https://randomuser.me/"

    /*
     * Retrofit se crea una sola vez.
     *
     * by lazy evita inicializarlo hasta que realmente sea necesario.
     */
    private val retrofit: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    /*
     * Implementación de la interfaz ApiRandom generada por Retrofit.
     */
    val instance: ApiRandom by lazy {

        retrofit.create(ApiRandom::class.java)
    }

    /*
     * Instancia de la base de datos Room.
     */
    private lateinit var database: AppDatabase

    /*
     * Inicializa Room.
     *
     * applicationContext evita mantener una referencia a una Activity.
     */
    fun initialize(context: Context) {

        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "users_database"
        ).build()
    }

    /*
     * DAO utilizado para guardar y eliminar favoritos.
     */
    val userDao
        get() = database.userDao()
}