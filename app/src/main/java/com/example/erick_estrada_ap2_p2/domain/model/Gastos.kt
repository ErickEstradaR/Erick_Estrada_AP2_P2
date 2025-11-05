package com.example.erick_estrada_ap2_p2.domain.model

data class Gastos (
    val gastoId : Int,
    val fecha : String,
    val suplidor : String,
    val nfc : String,
    val itbis : Double,
    val monto: Double,
)