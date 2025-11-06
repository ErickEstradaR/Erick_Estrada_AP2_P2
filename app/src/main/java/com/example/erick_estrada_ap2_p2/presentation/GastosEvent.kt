package com.example.erick_estrada_ap2_p2.presentation


 sealed interface GastosEvent
{
    data class onFechaChange(val fecha: String) : GastosEvent
    data class onSuplidorChange (val suplidor : String):GastosEvent
    data class onNfcChange(val nfc : String):GastosEvent
    data class onItbisChange(val itbis : Double):GastosEvent
    data class onMontoChange(val monto: Double):GastosEvent

    data object save:GastosEvent
    data object new:GastosEvent
    data object delete:GastosEvent
}