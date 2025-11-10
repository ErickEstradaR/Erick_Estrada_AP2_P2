package com.example.erick_estrada_ap2_p2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erick_estrada_ap2_p2.domain.model.Gastos
import com.example.erick_estrada_ap2_p2.domain.useCases.GastosUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
@HiltViewModel
class GastosViewModel @Inject constructor(
    private val useCases: GastosUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(GastosUiState.default())
    private val DEFAULT_ERROR_MESSAGE = "Error desconocido"
    val uiState = _uiState.asStateFlow()

    init {
        getGastos()
    }

    fun onEvent(event: GastosEvent) {
        when (event) {
            is GastosEvent.onFechaChange -> onFechaChange(event.fecha)
            is GastosEvent.onSuplidorChange -> onSuplidorChange(event.suplidor)
            is GastosEvent.onNfcChange -> onNfcChange(event.nfc)
            is GastosEvent.onItbisChange -> onItbisChange(event.itbis)
            is GastosEvent.onMontoChange -> onMontoChange(event.monto)
            GastosEvent.save -> viewModelScope.launch { saveGasto() }
            GastosEvent.new -> nuevo()


            is GastosEvent.delete -> deleteGasto(event.gastoId)
            GastosEvent.ShowCreateSheet -> _uiState.update { it.copy(showCreateSheet = true) }
            GastosEvent.HideCreateSheet -> _uiState.update { it.copy(showCreateSheet = false, gastoId = 0) }
        }
    }

    private fun nuevo() {
        _uiState.update {
            it.copy(
                gastoId = 0,
                fecha = "",
                suplidor = "",
                nfc = "",
                itbis = 0.0,
                monto = 0.0
            )
        }
    }

    private fun getGastos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                useCases.obtenerGastos().collectLatest { listaDeGastos ->
                    _uiState.update {
                        it.copy(
                            gastos = listaDeGastos,
                            isLoading = false
                        )
                    }
                }


            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        userMessage = e.message ?: DEFAULT_ERROR_MESSAGE,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun findGasto(gastoId: Int) {
        viewModelScope.launch {
            if (gastoId > 0) {
                try {
                    val gasto: Gastos? = useCases.obtenerGastoPorId(gastoId)
                    _uiState.update {
                        it.copy(
                            gastoId = gasto?.gastoId ?: 0,
                            fecha = gasto?.fecha.orEmpty(),
                            suplidor = gasto?.suplidor.orEmpty(),
                            nfc = gasto?.nfc.orEmpty(),
                            itbis = gasto?.itbis ?: 0.0,
                            monto = gasto?.monto ?: 0.0,
                            showCreateSheet = true
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(userMessage = e.message ?: DEFAULT_ERROR_MESSAGE) }
                }
            }
        }
    }

    suspend fun saveGasto(): Boolean {
        val currentState = _uiState.value

        if (currentState.fecha.isBlank() || currentState.suplidor.isBlank() || currentState.monto <= 0) {
            _uiState.update { it.copy(userMessage = "Fecha, Suplidor y Monto son requeridos.") }
            return false
        }

        val gasto = Gastos(
            gastoId = currentState.gastoId,
            fecha = currentState.fecha,
            suplidor = currentState.suplidor,
            nfc = currentState.nfc,
            itbis = currentState.itbis,
            monto = currentState.monto
        )

        return try {
            useCases.save(gasto)
            getGastos()
            nuevo()
            _uiState.update { it.copy(showCreateSheet = false) }
            true
        } catch (e: Exception) {
            _uiState.update { it.copy(userMessage = e.message ?: DEFAULT_ERROR_MESSAGE) }
            false
        }
    }


    private fun deleteGasto(gastoId: Int) {
        viewModelScope.launch {
            try {
                if (gastoId > 0) {
                    useCases.delete(gastoId)
                    getGastos()
                    _uiState.update { it.copy(userMessage = "Gasto eliminado") } // Mensaje de éxito
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(userMessage = e.message ?: DEFAULT_ERROR_MESSAGE) }
            }
        }
    }


    private fun onFechaChange(fecha: String) { _uiState.update { it.copy(fecha = fecha) } }
    private fun onSuplidorChange(suplidor: String) { _uiState.update { it.copy(suplidor = suplidor) } }
    private fun onNfcChange(nfc: String) { _uiState.update { it.copy(nfc = nfc) } }
    private fun onItbisChange(itbis: Double) { _uiState.update { it.copy(itbis = itbis) } }
    private fun onMontoChange(monto: Double) { _uiState.update { it.copy(monto = monto) } }
}