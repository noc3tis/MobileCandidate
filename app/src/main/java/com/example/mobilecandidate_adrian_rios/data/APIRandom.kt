package com.example.mobilecandidate_adrian_rios.data

import com.example.mobilecandidate_adrian_rios.viewModel.RandomUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ApiRandom {
    @GET("api/")
    suspend fun getUser(
        @Query("results") results: Int = 50,
        @Query("gender") gender: String? = null
    ): Response<RandomUserResponse>
}