package com.example.erick_estrada_ap2_p2.presentation

import com.example.erick_estrada_ap2_p2.domain.model.Gastos

data class GastosEvent(
    val gastoId : Int ,
    val fecha : String,
    val suplidor : String,
    val nfc : String,
    val itbis : Double,
    val monto: Double,
    val gastos : List<Gastos>,
    val ErrorMessage : String
)