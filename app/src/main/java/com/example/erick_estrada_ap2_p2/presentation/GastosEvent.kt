package com.example.erick_estrada_ap2_p2.presentation


 sealed interface GastosEvent
{
    data class onFechaChange(val fecha: String) : GastosEvent
    data class onSuplidorChange (val suplidor : String):GastosEvent
    data class onNcfChange(val ncf : String):GastosEvent
    data class onItbisChange(val itbis : Double):GastosEvent
    data class onMontoChange(val monto: Double):GastosEvent


    object ShowCreateSheet : GastosEvent
    object HideCreateSheet : GastosEvent

    data object crear:GastosEvent
    data object actualizar:GastosEvent
    data object new:GastosEvent

}