package com.example.erick_estrada_ap2_p2.presentation

sealed interface GastosUiState {
    data class onGastoChange(val gastoId : Int)
    data class onFechaChange(val fecha: String)
    data class onSuplidorChange (val suplidor : String)
    data class onNfcChange(val nfc : String)
    data class onItbisChange(val itbis : Double)
    data class onMontoChange(val monto: Double)

    data object save
    data object new
    data object delete
}




