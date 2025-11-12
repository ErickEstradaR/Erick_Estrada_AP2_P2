package com.example.erick_estrada_ap2_p2.domain.useCases

import com.example.erick_estrada_ap2_p2.data.remote.dto.GastoRequest



class validarGastoUseCase {
    operator fun invoke(gasto: GastoRequest): Result<Unit> {
        if (gasto.monto <= 0.0 ) {
            return Result.failure(IllegalArgumentException("El monto debe ser mayor que cero."))
        }
        if (gasto.fecha.isBlank()) {
            return Result.failure(IllegalArgumentException("La fecha no puede estar vacía."))
        }

        return Result.success(Unit)
    }
}