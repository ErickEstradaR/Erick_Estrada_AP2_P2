package com.example.erick_estrada_ap2_p2.presentation

import com.example.erick_estrada_ap2_p2.domain.model.Gasto

data class GastosUiState (
    val isLoading: Boolean = false,
    val gastos: List<Gasto> = emptyList(),
    val userMessage: String? = null,
    val showCreateSheet: Boolean = false,
    val gastoId : Int = 0,
    val fecha: String = "",
    val suplidor:String = "",
    val nfc: String = "",
    val itbis:Double =0.0,
    val monto:Double = 0.0
    )




