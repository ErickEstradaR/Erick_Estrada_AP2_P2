package com.example.erick_estrada_ap2_p2.data.remote.dto

class GastoResponse (
    val gastoId : Int,
    val fecha : String,
    val suplidor : String? ,
    val ncf : String?,
    val itbis : Double,
    val monto: Double,
)