package com.example.practico_biblioteca.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practico_biblioteca.ui.state.UiState
import com.example.practico_biblioteca.viewmodels.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneroFormScreen(
    onBack: () -> Unit,
    viewModel: LibroViewModel = viewModel()
){
    var nombreField by remember { mutableStateOf("") }
    var errorNombre by remember { mutableStateOf<String?>(null) }

    val generoFormState by viewModel.generoFormState.collectAsState()

    // Volver atrás y recargar géneros al éxito
    LaunchedEffect(generoFormState) {
        if (generoFormState is UiState.Success) {
            viewModel.resetGeneroFormState()
            onBack()
        }
    }

    fun validar(): Boolean {
        errorNombre = if (nombreField.isBlank()) "Campo obligatorio" else null
        return errorNombre == null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear género") },
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
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nombreField,
                onValueChange = { nombreField = it; errorNombre = null },
                label = { Text("Nombre *") },
                isError = errorNombre != null,
                supportingText = { errorNombre?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (generoFormState is UiState.Error) {
                Text(
                    text = (generoFormState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { if (validar()) viewModel.crearGenero(nombreField.trim()) },
                enabled = generoFormState !is UiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (generoFormState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Crear género")
                }
            }
        }
    }
}