package com.example.erick_estrada_ap2_p2.domain.repository

import com.example.erick_estrada_ap2_p2.data.remote.GastosApi
import com.example.erick_estrada_ap2_p2.domain.model.Gastos
import kotlinx.coroutines.flow.Flow

interface GastosRepository {
    suspend fun getAllGastos ():List<GastosApi>
    suspend fun getGastoById(gastoId: Int): GastosApi?
    suspend fun saveGasto(gastos: Gastos): GastosApi
    suspend fun deleteGasto(gastoId: Int): GastosApi
    fun getAllFlow (): Flow<List<GastosApi>>
}