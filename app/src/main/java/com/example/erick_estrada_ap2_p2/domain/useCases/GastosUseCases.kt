package com.example.erick_estrada_ap2_p2.domain.useCases

class GastosUseCases (
    val save : saveGastoUseCase,
    val obtenerGastos : getGastosUseCase,
    val obtenerGastoPorId : getGastoUseCase
)