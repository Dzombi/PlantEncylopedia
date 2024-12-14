package com.example.spirala

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrefleAPI {
    @GET("plants/search")
    suspend fun searchBiljke(
        @Query("q") query: String,
        @Query("token") token: String
    ): Response<TrefleBiljke>

    @GET("plants/{plantId}")
    suspend fun getBiljkeDetails(
        @Path("plantId") plantId: Int,
        @Query("token") token: String
    ): Response<TrefleBiljka>

    @GET("plants/search")
    suspend fun getBiljkeColor(
        @Query("filter[flower_color]") flowerColor: String,
        @Query("q") query: String,
        @Query("token") token: String,
        @Query("page") page: Int
    ): Response<TrefleBiljke>
}