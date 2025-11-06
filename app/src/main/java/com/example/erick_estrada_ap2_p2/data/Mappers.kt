package com.example.erick_estrada_ap2_p2.data

import com.example.erick_estrada_ap2_p2.data.remote.GastosDto
import com.example.erick_estrada_ap2_p2.domain.model.Gastos

fun GastosDto.toDomain() = Gastos(
    gastoId = gastoId,
    fecha = fecha,
    suplidor = suplidor,
    nfc = nfc,
    itbis = itbis,
    monto = monto
)

fun Gastos.toDto() = GastosDto(
    gastoId = gastoId,
    fecha = fecha,
    suplidor = suplidor,
    nfc = nfc,
    itbis = itbis,
    monto = monto
)