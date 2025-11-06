package com.example.erick_estrada_ap2_p2.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GastosApiService {
    @GET("api/Gastos")
    suspend fun getGastos(): List<GastosDto>

    @GET("api/Gastos/{id}")
    suspend fun getGasto(@Path("id") id: Int): GastosDto

    @POST("api/Gastos")
    suspend fun saveGasto(@Body gasto : GastosDto) : GastosDto

    @DELETE("api/Gastos/{id}")
    suspend fun deleteGasto(@Path("id") id: Int?)
}