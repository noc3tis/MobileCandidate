package com.example.mobilecandidate_adrian_rios.di

import com.example.mobilecandidate_adrian_rios.data.ApiRandom
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {

    private const val BASE_URL = "https://randomuser.me/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: ApiRandom by lazy {
        retrofit.create(ApiRandom::class.java)
    }
}