package com.example.erick_estrada_ap2_p2.data.repository

import com.example.erick_estrada_ap2_p2.data.remote.GastosApiService
import com.example.erick_estrada_ap2_p2.data.toApi
import com.example.erick_estrada_ap2_p2.data.toDomain
import com.example.erick_estrada_ap2_p2.domain.model.Gastos
import com.example.erick_estrada_ap2_p2.domain.repository.GastosRepository
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
        api.saveGasto(gastos.toApi())
    }

    override suspend fun deleteGasto(gastoId: Int) {
    return try {
        api.deleteGasto(gastoId)
    }
    catch (e: Exception){
      }
    }
}