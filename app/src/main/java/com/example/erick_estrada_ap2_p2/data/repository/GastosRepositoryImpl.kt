package com.example.erick_estrada_ap2_p2.data.repository

import com.example.erick_estrada_ap2_p2.data.remote.GastosApiService
import com.example.erick_estrada_ap2_p2.data.toDomain
import com.example.erick_estrada_ap2_p2.data.toDto
import com.example.erick_estrada_ap2_p2.domain.model.Gastos
import com.example.erick_estrada_ap2_p2.domain.repository.GastosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GastosRepositoryImpl  @Inject constructor(
    private val api: GastosApiService
) : GastosRepository {
    override suspend fun getAllGastos(): List<Gastos> {
        return api.getGastos().map { it.toDomain() }
    }

    override suspend fun getGastoById(gastoId: Int): Gastos? {
        val gasto = try {
            api.getGasto(gastoId)
        } catch (e: Exception) {
            null
        }
        return gasto?.toDomain()
    }

    override suspend fun saveGasto(gastos: Gastos) {
        api.saveGasto(gastos.toDto())
    }

    override suspend fun deleteGasto(gastoId: Int) {
    return try {
        api.deleteGasto(gastoId)
    }
    catch (e: Exception){
      }
    }

    override fun getAllFlow(): Flow<List<Gastos>> = flow {
        try {
            val tecnicosApi = api.getGastos()
            emit(tecnicosApi.map { it.toDomain() })
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

}