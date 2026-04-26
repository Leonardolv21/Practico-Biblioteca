package com.example.practico_biblioteca.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.practico_biblioteca.models.Libro
import com.example.practico_biblioteca.ui.state.UiState
import com.example.practico_biblioteca.viewmodels.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibroListScreen(
    onLibroClick: (Int) -> Unit,
    onCrearClick: () -> Unit,
    viewModel: LibroViewModel = viewModel()
) {
    val state by viewModel.librosState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Biblioteca") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCrearClick) {
                Icon(Icons.Default.Add, contentDescription = "Crear libro")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val s = state) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = s.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.cargarLibros() }) {
                            Text("Reintentar")
                        }
                    }
                }
                is UiState.Success -> {
                    val libros = s.data
                    if (libros.isEmpty()) {
                        Text(
                            text = "No hay libros disponibles",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(libros) { libro ->
                                LibroItem(libro = libro, onClick = { onLibroClick(libro.id) })
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibroItem(libro: Libro, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = libro.imagen,
            contentDescription = libro.titulo,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = libro.titulo, fontWeight = FontWeight.Bold)
            Text(text = libro.autor, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

