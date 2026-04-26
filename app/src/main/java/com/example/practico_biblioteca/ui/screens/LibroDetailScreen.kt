package com.example.practico_biblioteca.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.practico_biblioteca.ui.state.UiState
import com.example.practico_biblioteca.viewmodels.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibroDetailScreen(
    libroId: Int,
    onBack: () -> Unit,
    onEditarClick: (Int) -> Unit,
    onEliminarExito: () -> Unit,
    viewModel: LibroViewModel = viewModel()
) {
    LaunchedEffect(libroId) {
        viewModel.cargarDetalle(libroId)
    }

    val state by viewModel.detalleState.collectAsState()

    // Eliminar libro state
    val deleteState by viewModel.deleteState.collectAsState()
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    LaunchedEffect(deleteState) {
        if (deleteState is UiState.Success) {
            onEliminarExito()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del libro") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditarClick(libroId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { mostrarDialogoEliminar = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (mostrarDialogoEliminar) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoEliminar = false },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que deseas eliminar este libro?") },
                    confirmButton = {
                        TextButton(onClick = {
                            mostrarDialogoEliminar = false
                            viewModel.eliminarLibro(libroId)
                        }) {
                            Text("Eliminar", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDialogoEliminar = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (deleteState is UiState.Error) {
                Text(
                    text = (deleteState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
            when (val s = state) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Error -> {
                    Text(
                        text = s.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Success -> {
                    val libro = s.data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        AsyncImage(
                            model = libro.imagen,
                            contentDescription = libro.titulo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = libro.titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        InfoRow(label = "Autor", value = libro.autor)
                        InfoRow(label = "Editorial", value = libro.editorial)
                        InfoRow(label = "ISBN", value = libro.isbn)
                        InfoRow(label = "Calificación", value = libro.calificacion.toString())
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Sinopsis", fontWeight = FontWeight.SemiBold)
                        Text(text = libro.sinopsis, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = "$label: ", fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
}

