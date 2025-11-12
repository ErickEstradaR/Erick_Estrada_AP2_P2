package com.example.erick_estrada_ap2_p2.domain.useCases

import com.example.erick_estrada_ap2_p2.data.Resource
import com.example.erick_estrada_ap2_p2.domain.model.Gasto
import com.example.erick_estrada_ap2_p2.domain.repository.GastosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class getGastosUseCase @Inject constructor(
    private val repository: GastosRepository
) {
    operator fun invoke(): Flow<Resource<List<Gasto>>> = repository.getGastos()
}