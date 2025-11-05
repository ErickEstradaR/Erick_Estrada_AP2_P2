package com.example.erick_estrada_ap2_p2.domain.useCases

import com.example.erick_estrada_ap2_p2.domain.repository.GastosRepository

class getGastosUseCase(private val repo : GastosRepository){
    suspend operator fun invoke () = repo.getAllGastos()
}