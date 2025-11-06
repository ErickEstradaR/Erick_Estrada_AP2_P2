package com.example.erick_estrada_ap2_p2.domain.useCases

import com.example.erick_estrada_ap2_p2.domain.model.Gastos
import com.example.erick_estrada_ap2_p2.domain.repository.GastosRepository
import kotlinx.coroutines.flow.Flow

class getGastosUseCase(  private val repository: GastosRepository
) {
    operator fun invoke(): Flow<List<Gastos>> {
        return repository.getAllFlow()
    }
}