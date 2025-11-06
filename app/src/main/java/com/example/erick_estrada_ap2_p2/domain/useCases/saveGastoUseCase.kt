package com.example.erick_estrada_ap2_p2.domain.useCases

import com.example.erick_estrada_ap2_p2.domain.model.Gastos
import com.example.erick_estrada_ap2_p2.domain.repository.GastosRepository

class saveGastoUseCase (private val repo : GastosRepository) {
    suspend operator fun invoke(gasto: Gastos) = repo.saveGasto(gasto)
}
