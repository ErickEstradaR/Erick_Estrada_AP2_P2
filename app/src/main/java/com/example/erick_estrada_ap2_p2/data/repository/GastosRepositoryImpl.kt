package com.example.erick_estrada_ap2_p2.data.repository

import com.example.erick_estrada_ap2_p2.data.Resource
import com.example.erick_estrada_ap2_p2.data.remote.RemoteDataSource
import com.example.erick_estrada_ap2_p2.data.remote.dto.GastoRequest
import com.example.erick_estrada_ap2_p2.data.toDomain
import com.example.erick_estrada_ap2_p2.domain.model.Gasto
import com.example.erick_estrada_ap2_p2.domain.repository.GastosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GastosRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): GastosRepository {

    override fun getGastos(): Flow<Resource<List<Gasto>>> = flow {

        emit(Resource.Loading())

        when (val resource = remoteDataSource.getGastos()) {
            is Resource.Success -> {
                val gastos = resource.data?.map { dto -> dto.toDomain() } ?: emptyList()
                emit(Resource.Success(gastos))
            }
            is Resource.Error -> {
                emit(Resource.Error(resource.message ?: "Error desconocido"))
            }
            is Resource.Loading -> {}
        }
    }

    override fun getGasto(id: Int): Flow<Resource<Gasto>> = flow {
        emit(Resource.Loading())

        when (val resource = remoteDataSource.getGasto(id)) {
            is Resource.Success -> {
                val dto = resource.data
                if (dto != null) {
                    val gasto = dto.toDomain()
                    emit(Resource.Success(gasto))
                } else {
                    emit(Resource.Error("Respuesta exitosa pero con datos nulos"))
                }
            }
            is Resource.Error -> {
                emit(Resource.Error(resource.message ?: "Error desconocido"))
            }
            is Resource.Loading -> {}
        }
    }

    override suspend fun postGasto(req: GastoRequest): Resource<Unit> {
        return when (val resource = remoteDataSource.save(req)) {
            is Resource.Success -> Resource.Success(Unit)
            is Resource.Error -> Resource.Error(resource.message)
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun putGasto(id: Int, req: GastoRequest): Resource<Unit> {
        return remoteDataSource.update(id, req)
    }
}