package com.example.erick_estrada_ap2_p2.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erick_estrada_ap2_p2.data.Resource
import com.example.erick_estrada_ap2_p2.data.remote.dto.GastoRequest
import com.example.erick_estrada_ap2_p2.domain.model.Gasto
import com.example.erick_estrada_ap2_p2.domain.useCases.GastosUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class GastoViewModel @Inject constructor(
    private val useCases: GastosUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(GastosUiState(isLoading = true))
    val state: StateFlow<GastosUiState> = _state.asStateFlow()

    init {
        obtenerGastos()
    }

    fun obtenerGastos() {
        viewModelScope.launch {
            useCases.obtenerGastos().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            gastos = resource.data ?: emptyList(),
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            userMessage = resource.message ?: "Error desconocido",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: GastosEvent) {
        when (event) {
            is GastosEvent.crear -> validarYGuardar(event.gasto,true)
            is GastosEvent.actualizar -> validarYGuardar(event.gasto,false)
            is GastosEvent.obtener -> getGasto(event.id)
            is GastosEvent.cargar -> obtenerGastos()
            is GastosEvent.showCreateSheet -> {
                _state.value = _state.value.copy(showCreateSheet = true)
            }
            is GastosEvent.hideCreateSheet -> {
                _state.value = _state.value.copy(hideCreateSheet = false)
            }
            is GastosEvent.onFechaChange -> {
                _state.value = _state.value.copy(fecha = event.fecha)
            }
            is GastosEvent.onSuplidorChange -> {
                _state.value = _state.value.copy(suplidor = event.suplidor)
            }
            is GastosEvent.onNcfChange -> {
                _state.value = _state.value.copy(ncf = event.ncf)
            }
            is GastosEvent.onItbisChange -> {
                _state.value = _state.value.copy(itbis = event.itbis)
            }
            is GastosEvent.onMontoChange -> {
                _state.value = _state.value.copy(monto = event.monto)
            }
            is GastosEvent.userMessageShown -> clearMessage()
            GastosEvent.new -> {
                clearForm()
                _state.value = _state.value.copy(showCreateSheet = true)
            }
        }
    }

    private fun validarYGuardar(gasto: Gasto, esNuevo: Boolean) {

        val montoError = if (gasto.monto <= 0.0) "El monto debe ser > 0" else null
        val fechaError = if (gasto.fecha.isBlank()) "La fecha es requerida" else null
        val itbisError = if (gasto.itbis < 0.0) "El ITBIS no puede ser negativo" else null


        _state.update { it.copy(
            montoError = montoError,
            fechaError = fechaError,
            itbisError = itbisError,
        )}

        if (montoError != null || fechaError != null || itbisError != null) {
            return
        }

        if (esNuevo) {
            crearGasto(gasto)
        } else {
            updateGasto(gasto)
        }
    }

    private fun crearGasto(gasto: Gasto) {
        viewModelScope.launch {
            val gastoReq = GastoRequest(
                fecha = gasto.fecha,
                suplidor = gasto.suplidor,
                ncf = gasto.ncf,
                itbis = gasto.itbis,
                monto = gasto.monto
            )
            val result = useCases.save(id = null, gastoReq)

            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            userMessage = "Gasto creado",
                            showCreateSheet = false
                        )
                    }
                    clearForm()
                    obtenerGastos()
                }
                is Resource.Error -> {
                    Log.e("GastoViewModel", "Resultado: Resource.Error - Mensaje: ${result.message}")
                    _state.update {
                        it.copy(userMessage = "Error al crear el gasto")
                    }
                }
                else -> {
                }
            }
        }
    }
    private fun updateGasto(gasto: Gasto) {
        viewModelScope.launch {
            val gastoReq = GastoRequest(
                gasto.fecha, gasto.suplidor,
                gasto.ncf, gasto.itbis, gasto.monto
            )

            when (useCases.save(gasto.gastoId, gastoReq)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            userMessage = "Gasto actualizado exitosamente",
                            showCreateSheet = false
                        )
                    }
                    clearForm()
                    obtenerGastos()
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(userMessage = "Error al actualizar el gasto")
                    }
                }
                else -> {}
            }
        }
    }

    private fun getGasto(id: Int?) {
        viewModelScope.launch {
            useCases.obtenerGastoPorId(id).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let { gasto ->
                            _state.value = _state.value.copy(
                                gastoId = gasto.gastoId,
                                fecha = gasto.fecha,
                                suplidor = gasto.suplidor,
                                ncf = gasto.ncf,
                                itbis = gasto.itbis,
                                monto = gasto.monto
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(userMessage = resource.message ?: "Error al cargar el gasto")
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun clearForm() {
        _state.update {
            it.copy(
                gastoId = null,
                fecha = "",
                suplidor = "",
                ncf = "",
                itbis = 0.0,
                monto = 0.0
            )
        }
    }

    private fun clearMessage() {
        _state.update { it.copy(userMessage = null) }
    }
}