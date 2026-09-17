package com.example.mobilecandidate_adrian_rios.data

import com.example.mobilecandidate_adrian_rios.viewModel.RandomUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/*
 * Interfaz que define las peticiones que realizará Retrofit
 * hacia la API pública RandomUser.
 */
interface ApiRandom {

    /*
     * Endpoint:
     * https://randomuser.me/api/
     *
     * results -> cantidad de usuarios solicitados.
     * page    -> página de resultados.
     * seed    -> permite mantener una secuencia consistente.
     * gender  -> permite solicitar hombres o mujeres.
     */
    @GET("api/")
    suspend fun getUser(
        @Query("results") results: Int = 50,
        @Query("page") page: Int = 1,
        @Query("seed") seed: String,
        @Query("gender") gender: String? = null
    ): Response<RandomUserResponse>
}