package com.example.erick_estrada_ap2_p2.data.remote

import com.example.erick_estrada_ap2_p2.data.Resource
import com.example.erick_estrada_ap2_p2.data.remote.dto.GastoRequest
import com.example.erick_estrada_ap2_p2.data.remote.dto.GastoResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: GastosApiService
) {
    suspend fun save(request: GastoRequest): Resource<GastoResponse> {
        return try {
            val response = api.postGasto(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error("Respuesta vacía del servidor")
                }
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun update(id: Int, request: GastoRequest): Resource<Unit> {
        return try {
            val response = api.putGasto(id, request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun getGasto(id: Int?): Resource<GastoResponse> {
        return try {
            val response = api.getGasto(id)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red al obtener el gasto")
        }
    }

    suspend fun getGastos(): Resource<List<GastoResponse>> {
        return try {
            val response = api.getGastos()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía al obtener los gastos")
            } else {
                Resource.Error("HTTP ${response.code()} al obtener lista de gastos: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red al obtener gastos")
        }
    }
}