package com.example.erick_estrada_ap2_p2.data

import com.example.erick_estrada_ap2_p2.data.remote.GastosApi
import com.example.erick_estrada_ap2_p2.domain.model.Gastos

fun GastosApi.toDomain() = Gastos(
    gastoId = gastoId,
    fecha = fecha,
    suplidor = suplidor,
    nfc = nfc,
    itbis = itbis,
    monto = monto
)

fun Gastos.toApi() = GastosApi(
    gastoId = gastoId,
    fecha = fecha,
    suplidor = suplidor,
    nfc = nfc,
    itbis = itbis,
    monto = monto
)