package com.example.practico_biblioteca.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practico_biblioteca.models.Libro
import com.example.practico_biblioteca.ui.state.UiState
import com.example.practico_biblioteca.viewmodels.LibroViewModel

// Pantalla reutilizada para CREAR y EDITAR
// Si libroId != null => modo edición
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibroFormScreen(
    libroId: Int? = null,
    onBack: () -> Unit,
    viewModel: LibroViewModel = viewModel()
) {
    val modoEdicion = libroId != null
    val titulo = if (modoEdicion) "Editar libro" else "Crear libro"

    // Cargar datos previos si es edición
    LaunchedEffect(libroId) {
        viewModel.cargarGeneros()
        if (modoEdicion) viewModel.cargarDetalle(libroId!!)
    }

    val detalleState by viewModel.detalleState.collectAsState()
    val generosState by viewModel.generosState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    // Campos del formulario
    var tituloField by remember { mutableStateOf("") }
    var autorField by remember { mutableStateOf("") }
    var editorialField by remember { mutableStateOf("") }
    var imagenField by remember { mutableStateOf("") }
    var sinopsisField by remember { mutableStateOf("") }
    var isbnField by remember { mutableStateOf("") }
    var calificacionField by remember { mutableStateOf("") }
    var generosSeleccionados by remember { mutableStateOf<Set<Int>>(emptySet()) }

    // Errores de validación
    var errorTitulo by remember { mutableStateOf<String?>(null) }
    var errorAutor by remember { mutableStateOf<String?>(null) }
    var errorEditorial by remember { mutableStateOf<String?>(null) }
    var errorImagen by remember { mutableStateOf<String?>(null) }
    var errorIsbn by remember { mutableStateOf<String?>(null) }
    var errorCalificacion by remember { mutableStateOf<String?>(null) }

    // Pre-cargar datos en modo edición
    LaunchedEffect(detalleState) {
        if (modoEdicion && detalleState is UiState.Success) {
            val libro = (detalleState as UiState.Success).data
            tituloField = libro.titulo
            autorField = libro.autor
            editorialField = libro.editorial
            imagenField = libro.imagen
            sinopsisField = libro.sinopsis
            isbnField = libro.isbn
            calificacionField = libro.calificacion.toString()
            generosSeleccionados = libro.generos.toSet()
        }
    }

    // Navegar atrás al éxito
    LaunchedEffect(formState) {
        if (formState is UiState.Success) {
            viewModel.resetFormState()
            onBack()
        }
    }

    fun validar(): Boolean {
        var valido = true
        errorTitulo = if (tituloField.isBlank()) { valido = false; "Campo obligatorio" } else null
        errorAutor = if (autorField.isBlank()) { valido = false; "Campo obligatorio" } else null
        errorEditorial = if (editorialField.isBlank()) { valido = false; "Campo obligatorio" } else null
        errorIsbn = when {
            isbnField.isBlank() -> { valido = false; "Campo obligatorio" }
            !isbnField.matches(Regex("^(97[89])?\\d{9}[\\dXx]\$")) -> { valido = false; "Formato ISBN inválido" }
            else -> null
        }
        errorImagen = when {
            imagenField.isBlank() -> { valido = false; "Campo obligatorio" }
            !imagenField.startsWith("http://") && !imagenField.startsWith("https://") -> { valido = false; "URL inválida" }
            else -> null
        }
        errorCalificacion = when {
            calificacionField.isBlank() -> { valido = false; "Campo obligatorio" }
            calificacionField.toDoubleOrNull() == null -> { valido = false; "Debe ser un número" }
            else -> null
        }
        return valido
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tituloField,
                onValueChange = { tituloField = it; errorTitulo = null },
                label = { Text("Título *") },
                isError = errorTitulo != null,
                supportingText = { errorTitulo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = autorField,
                onValueChange = { autorField = it; errorAutor = null },
                label = { Text("Autor *") },
                isError = errorAutor != null,
                supportingText = { errorAutor?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = editorialField,
                onValueChange = { editorialField = it; errorEditorial = null },
                label = { Text("Editorial *") },
                isError = errorEditorial != null,
                supportingText = { errorEditorial?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = isbnField,
                onValueChange = { isbnField = it; errorIsbn = null },
                label = { Text("ISBN *") },
                isError = errorIsbn != null,
                supportingText = { errorIsbn?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = imagenField,
                onValueChange = { imagenField = it; errorImagen = null },
                label = { Text("URL de imagen *") },
                isError = errorImagen != null,
                supportingText = { errorImagen?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = sinopsisField,
                onValueChange = { sinopsisField = it },
                label = { Text("Sinopsis") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            OutlinedTextField(
                value = calificacionField,
                onValueChange = { calificacionField = it; errorCalificacion = null },
                label = { Text("Calificación *") },
                isError = errorCalificacion != null,
                supportingText = { errorCalificacion?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selección de géneros
            Text(text = "Géneros", style = MaterialTheme.typography.titleSmall)
            when (val gs = generosState) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.size(24.dp))
                is UiState.Error -> Text(gs.message, color = MaterialTheme.colorScheme.error)
                is UiState.Success -> {
                    gs.data.forEach { genero ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = genero.id in generosSeleccionados,
                                onCheckedChange = { checked ->
                                    generosSeleccionados = if (checked)
                                        generosSeleccionados + genero.id
                                    else
                                        generosSeleccionados - genero.id
                                }
                            )
                            Text(text = genero.nombre)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Feedback de error al enviar
            if (formState is UiState.Error) {
                Text(
                    text = (formState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (validar()) {
                        val libro = Libro(
                            id = libroId ?: 0,
                            titulo = tituloField.trim(),
                            autor = autorField.trim(),
                            editorial = editorialField.trim(),
                            imagen = imagenField.trim(),
                            sinopsis = sinopsisField.trim(),
                            isbn = isbnField.trim(),
                            calificacion = calificacionField.toDoubleOrNull() ?: 0.0,
                            generos = generosSeleccionados.toList()
                        )
                        if (modoEdicion) viewModel.editarLibro(libroId!!, libro)
                        else viewModel.crearLibro(libro)
                    }
                },
                enabled = formState !is UiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (formState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text(if (modoEdicion) "Guardar cambios" else "Crear libro")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

