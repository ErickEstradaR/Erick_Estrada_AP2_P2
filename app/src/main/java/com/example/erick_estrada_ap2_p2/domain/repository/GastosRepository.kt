package com.example.erick_estrada_ap2_p2.domain.repository

import com.example.erick_estrada_ap2_p2.data.Resource
import com.example.erick_estrada_ap2_p2.data.remote.dto.GastoRequest
import com.example.erick_estrada_ap2_p2.domain.model.Gasto
import kotlinx.coroutines.flow.Flow

interface GastosRepository {

    fun getGastos(): Flow<Resource<List<Gasto>>>
    fun getGasto(id: Int?): Flow<Resource<Gasto>>
    suspend fun postGasto(req: GastoRequest): Resource<Unit>
    suspend fun putGasto(id: Int, req: GastoRequest): Resource<Unit>
}