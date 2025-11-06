package com.example.erick_estrada_ap2_p2.domain.repository

import com.example.erick_estrada_ap2_p2.domain.model.Gastos
import kotlinx.coroutines.flow.Flow

interface GastosRepository {
    suspend fun getAllGastos ():List<Gastos>
    suspend fun getGastoById(gastoId: Int): Gastos?
    suspend fun saveGasto(gastos: Gastos)
    suspend fun deleteGasto(gastoId: Int)

    fun getAllFlow(): Flow<List<Gastos>>
}