package com.example.mobilecandidate_adrian_rios.di

object AppModule {

    private const val BASE_URL = " https://randomuser.me/api/?results=50"

    private val retrofit: Retrofit by lazy {
        .baseUrl
    }
}