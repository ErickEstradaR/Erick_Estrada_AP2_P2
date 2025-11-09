package com.example.erick_estrada_ap2_p2.data

import com.example.erick_estrada_ap2_p2.data.remote.GastosDto
import com.example.erick_estrada_ap2_p2.data.remote.dto.GastoResponse
import com.example.erick_estrada_ap2_p2.domain.model.Gasto

fun GastoResponse.toDomain() = Gasto(
    gastoId = gastoId,
    fecha = fecha,
    suplidor = suplidor,
    ncf = ncf,
    itbis = itbis,
    monto = monto,
)