package com.example.shoppinglistapp.data.remote

import com.example.shoppinglistapp.data.remote.response.ImageResponse
import com.example.shoppinglistapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = Constants.API_KEY
    ) : Response<ImageResponse>

}