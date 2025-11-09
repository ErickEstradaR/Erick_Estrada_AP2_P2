package com.example.erick_estrada_ap2_p2.presentation

import com.example.erick_estrada_ap2_p2.domain.model.Gasto


sealed interface GastosEvent
{
    data object cargar : GastosEvent
    data class onFechaChange(val fecha: String) : GastosEvent
    data class onSuplidorChange (val suplidor : String):GastosEvent
    data class onNcfChange(val ncf : String):GastosEvent
    data class onItbisChange(val itbis : Double):GastosEvent
    data class onMontoChange(val monto: Double):GastosEvent
    data class obtener(val id: Int?): GastosEvent
    object userMessageShown : GastosEvent

    object showCreateSheet : GastosEvent
    object hideCreateSheet : GastosEvent

    data class crear(val gasto: Gasto): GastosEvent
    data class actualizar(val gasto: Gasto) : GastosEvent

    data object new:GastosEvent

}