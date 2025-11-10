package com.example.erick_estrada_ap2_p2.domain.useCases

import com.example.erick_estrada_ap2_p2.domain.repository.GastosRepository
import javax.inject.Inject

class getGastoUseCase @Inject constructor(private val repo : GastosRepository) {
    operator fun invoke (id: Int?) = repo.getGasto(id)
}