package com.example.erick_estrada_ap2_p2.presentation

import com.example.erick_estrada_ap2_p2.domain.model.Gastos
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GastosScreen(
    viewModel: GastosViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    GastosListBody(state, viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastosListBody(
    state: GastosUiState,
    onEvent: (GastosEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(GastosEvent.ShowCreateSheet) },
                modifier = Modifier.testTag("fab_add_gasto")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Gasto"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("loading_gastos")
                )
            } else {
                if (state.gastos.isEmpty()) {
                    Text(
                        text = "No hay Gastos registrados",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("empty_message_gastos"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.gastos,
                            key = { it.gastoId }
                        ) { gasto ->
                            GastoItem(
                                gasto = gasto,
                                onDelete = {
                                    onEvent(GastosEvent.delete(gasto.gastoId)) // Envía el evento de borrado con ID
                                }
                            )
                        }
                    }
                }
            }
        }

        if (state.showCreateSheet) {
            ModalBottomSheet(
                onDismissRequest = { onEvent(GastosEvent.HideCreateSheet) },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding()
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (state.gastoId > 0) "Editar Gasto" else "Nuevo Gasto",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    OutlinedTextField(
                        value = state.fecha,
                        onValueChange = { onEvent(GastosEvent.onFechaChange(it)) },
                        label = { Text("Fecha (ej: YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth().testTag("input_fecha"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.suplidor,
                        onValueChange = { onEvent(GastosEvent.onSuplidorChange(it)) },
                        label = { Text("Suplidor") },
                        modifier = Modifier.fillMaxWidth().testTag("input_suplidor"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.nfc,
                        onValueChange = { onEvent(GastosEvent.onNfcChange(it)) },
                        label = { Text("NFC") },
                        modifier = Modifier.fillMaxWidth().testTag("input_nfc"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.itbis.toString(),
                        onValueChange = { onEvent(GastosEvent.onItbisChange(it.toDoubleOrNull() ?: 0.0)) },
                        label = { Text("ITBIS") },
                        modifier = Modifier.fillMaxWidth().testTag("input_itbis"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.monto.toString(),
                        onValueChange = { onEvent(GastosEvent.onMontoChange(it.toDoubleOrNull() ?: 0.0)) },
                        label = { Text("Monto") },
                        modifier = Modifier.fillMaxWidth().testTag("input_monto"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onEvent(GastosEvent.HideCreateSheet) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = { onEvent(GastosEvent.save) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_save_gasto"),
                            // Validación para habilitar el botón
                            enabled = state.suplidor.isNotBlank() && state.fecha.isNotBlank() && state.monto > 0
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GastoItem(
    gasto: Gastos,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("gasto_item_${gasto.gastoId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = gasto.suplidor,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Monto: $${gasto.monto}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Fecha: ${gasto.fecha}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("btn_delete_${gasto.gastoId}")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Gasto"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GastoListBodyPreview() {
    MaterialTheme {
        val state = GastosUiState(
            isLoading = false,
            gastos = listOf(
                Gastos(
                    gastoId = 1,
                    fecha = "2023-10-27",
                    suplidor = "Ferretería Ochoa",
                    nfc = "B01000881",
                    itbis = 180.0,
                    monto = 1000.0
                ),
                Gastos(
                    gastoId = 2,
                    fecha = "2023-10-28",
                    suplidor = "Supermercado Bravo",
                    nfc = "B01000992",
                    itbis = 360.0,
                    monto = 2000.0
                )
            )
        )
        GastosListBody(state) {}
    }
}