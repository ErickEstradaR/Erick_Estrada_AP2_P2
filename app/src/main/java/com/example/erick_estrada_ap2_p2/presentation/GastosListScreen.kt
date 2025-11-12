import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.erick_estrada_ap2_p2.domain.model.Gasto
import com.example.erick_estrada_ap2_p2.presentation.GastoViewModel
import com.example.erick_estrada_ap2_p2.presentation.GastosEvent
import com.example.erick_estrada_ap2_p2.presentation.GastosUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastosScreen(
    viewModel: GastoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.onEvent(GastosEvent.userMessageShown)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Gastos") },
                )

        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(GastosEvent.new) }) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Gasto")
            }
        }
    ) { padding ->

        GastosContent(
            modifier = Modifier.padding(padding),
            isLoading = state.isLoading,
            gastos = state.gastos,
            onGastoClick = { gasto ->
                viewModel.onEvent(GastosEvent.obtener(gasto.gastoId))
                viewModel.onEvent(GastosEvent.showCreateSheet)
            }
        )

        if (state.showCreateSheet) {
            CreateGastoSheet(
                state = state,
                onDismiss = { viewModel.onEvent(GastosEvent.hideCreateSheet) },
                onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
private fun GastosContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    gastos: List<Gasto>,
    onGastoClick: (Gasto) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (gastos.isEmpty()) {
            Text("No hay gastos registrados.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(gastos, key = { it.gastoId!! }) { gasto ->
                    GastoItem(
                        gasto = gasto,
                        onClick = { onGastoClick(gasto) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GastoItem(
    gasto: Gasto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        ListItem(
            headlineContent = { Text(gasto.suplidor.toString(), style = MaterialTheme.typography.titleMedium) },
            supportingContent = { Text("NCF: ${gasto.ncf} - Fecha: ${gasto.fecha}") },
            trailingContent = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Monto $${gasto.monto}",
                    )
                    Text(
                        text = "ITBIS $${gasto.itbis}",
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateGastoSheet(
    state: GastosUiState,
    onDismiss: () -> Unit,
    onEvent: (GastosEvent) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                 text = if (state.gastoId == null) "Nuevo Gasto" else "Editar Gasto",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = state.fecha,
                onValueChange = { onEvent(GastosEvent.onFechaChange(it)) },
                label = { Text("Fecha (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.fechaError != null,
                supportingText = {
                    if (state.fechaError != null) {
                        Text(text = state.fechaError)
                    }
                }

            )
            OutlinedTextField(
                value = state.suplidor.toString(),
                onValueChange = { onEvent(GastosEvent.onSuplidorChange(it)) },
                label = { Text("Suplidor") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.ncf.toString(),
                onValueChange = { onEvent(GastosEvent.onNcfChange(it)) },
                label = { Text("NCF") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.itbis.toString(),
                onValueChange = { onEvent(GastosEvent.onItbisChange(it.toDoubleOrNull() ?: 0.0)) },
                label = { Text("ITBIS") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = state.itbisError != null,
                supportingText = {
                    if (state.itbisError != null) {
                        Text(text = state.itbisError)
                    }
                }
            )
            OutlinedTextField(
                value = state.monto.toString(),
                onValueChange = { onEvent(GastosEvent.onMontoChange(it.toDoubleOrNull() ?: 0.0)) },
                label = { Text("Monto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = state.montoError != null,
                supportingText = {
                    if (state.montoError != null) {
                        Text(text = state.montoError)
                    }
                }
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                     val gasto = Gasto(
                        gastoId = state.gastoId ?: 0,
                        fecha = state.fecha,
                        suplidor = state.suplidor,
                        ncf = state.ncf,
                        itbis = state.itbis,
                        monto = state.monto
                    )
                    if (state.gastoId == null) {
                        onEvent(GastosEvent.crear(gasto))
                    } else {
                        onEvent(GastosEvent.actualizar(gasto))
                    }
                }
            ) {
                Text("Guardar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GastoItemPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.padding(8.dp)) {
            GastoItem(
                gasto = gastoFalso,
                onClick = {}
            )
        }
    }
}

val gastoFalso = Gasto(
    gastoId = 1,
    fecha = "2025-11-09",
    suplidor = "Supermercado Bravo",
    ncf = "B0100000123",
    itbis = 360.0,
    monto = 2000.0
)

