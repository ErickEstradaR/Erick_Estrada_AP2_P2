package com.example.erick_estrada_ap2_p2.domain.useCases

import com.example.erick_estrada_ap2_p2.data.Resource
import com.example.erick_estrada_ap2_p2.data.remote.dto.GastoRequest
import com.example.erick_estrada_ap2_p2.data.remote.dto.GastoResponse
import com.example.erick_estrada_ap2_p2.domain.model.Gasto
import com.example.erick_estrada_ap2_p2.domain.repository.GastosRepository
import javax.inject.Inject

class saveGastoUseCase @Inject constructor(
    private val repository: GastosRepository
) {
    suspend operator fun invoke(id: Int?, request: GastoRequest): Resource<Unit> {
        return if (id == null) {
            repository.postGasto(request)
        } else {
            repository.putGasto(id, request)
        }
    }
}
