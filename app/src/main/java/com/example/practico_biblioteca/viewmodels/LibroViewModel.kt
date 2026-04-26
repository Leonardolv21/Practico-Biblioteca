package com.example.practico_biblioteca.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practico_biblioteca.models.Genero
import com.example.practico_biblioteca.models.Libro
import com.example.practico_biblioteca.repositories.LibroRepository
import com.example.practico_biblioteca.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibroViewModel : ViewModel() {

    private val repository = LibroRepository()

    // --- Lista de libros ---
    private val _librosState = MutableStateFlow<UiState<List<Libro>>>(UiState.Loading)
    val librosState: StateFlow<UiState<List<Libro>>> = _librosState

    // --- Detalle de un libro ---
    private val _detalleState = MutableStateFlow<UiState<Libro>>(UiState.Loading)
    val detalleState: StateFlow<UiState<Libro>> = _detalleState

    // --- Géneros disponibles ---
    private val _generosState = MutableStateFlow<UiState<List<Genero>>>(UiState.Loading)
    val generosState: StateFlow<UiState<List<Genero>>> = _generosState
    // --- Estado formulario crear Genero---
    private val _generoFormState = MutableStateFlow<UiState<Unit>?>(null)
    val generoFormState: StateFlow<UiState<Unit>?> = _generoFormState
    // --- Estado eliminar Genero ---
    private val _deleteGeneroState = MutableStateFlow<UiState<Unit>?>(null)
    val deleteGeneroState: StateFlow<UiState<Unit>?> = _deleteGeneroState

    // --- Estado formulario crear/editar ---
    private val _formState = MutableStateFlow<UiState<Unit>?>(null)
    val formState: StateFlow<UiState<Unit>?> = _formState

    // --- Estado eliminar ---
    private val _deleteState = MutableStateFlow<UiState<Unit>?>(null)
    val deleteState: StateFlow<UiState<Unit>?> = _deleteState

    private var isSubmitting = false

    init {
        cargarLibros()
    }

    fun cargarLibros() {
        viewModelScope.launch {
            _librosState.value = UiState.Loading
            val libros = repository.getLibros()
            if (libros.isNotEmpty()) {
                _librosState.value = UiState.Success(libros)
            } else {
                _librosState.value = UiState.Error("No se pudieron cargar los libros")
            }
        }
    }

    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            _detalleState.value = UiState.Loading
            val libro = repository.getLibro(id)
            if (libro != null) {
                _detalleState.value = UiState.Success(libro)
            } else {
                _detalleState.value = UiState.Error("No se encontró el libro")
            }
        }
    }

    fun cargarGeneros() {
        viewModelScope.launch {
            _generosState.value = UiState.Loading
            val generos = repository.getGeneros()
            if (generos.isNotEmpty()) {
                _generosState.value = UiState.Success(generos)
            } else {
                _generosState.value = UiState.Error("No se pudieron cargar los géneros")
            }
        }
    }

    fun crearLibro(libro: Libro) {
        if (isSubmitting) return
        isSubmitting = true
        viewModelScope.launch {
            _formState.value = UiState.Loading
            val success = repository.createLibro(libro)
            if (success) {
                _formState.value = UiState.Success(Unit)
                cargarLibros()
            } else {
                _formState.value = UiState.Error("Error al crear el libro")
            }
            isSubmitting = false
        }
    }

    fun editarLibro(id: Int, libro: Libro) {
        if (isSubmitting) return
        isSubmitting = true
        viewModelScope.launch {
            _formState.value = UiState.Loading
            val success = repository.updateLibro(id, libro)
            if (success) {
                _formState.value = UiState.Success(Unit)
                cargarLibros()
                cargarDetalle(id)
            } else {
                _formState.value = UiState.Error("Error al editar el libro")
            }
            isSubmitting = false
        }
    }

    fun eliminarLibro(id: Int) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            val success = repository.deleteLibro(id)
            if (success) {
                _deleteState.value = UiState.Success(Unit)
                cargarLibros()
            } else {
                _deleteState.value = UiState.Error("Error al eliminar el libro")
            }
        }
    }

    fun crearGenero(nombre: String) {
        if (isSubmitting) return
        isSubmitting = true
        viewModelScope.launch {
            _generoFormState.value = UiState.Loading
            val success = repository.createGenero(nombre)
            if (success) {
                _generoFormState.value = UiState.Success(Unit)
                cargarGeneros()
            } else {
                _generoFormState.value = UiState.Error("Error al crear el género")
            }
            isSubmitting = false
        }
    }

    fun eliminarGenero(id: Int) {
        viewModelScope.launch {
            _deleteGeneroState.value = UiState.Loading
            val success = repository.deleteGenero(id)
            if (success) {
                _deleteGeneroState.value = UiState.Success(Unit)
                cargarGeneros()
            } else {
                _deleteGeneroState.value = UiState.Error("Error al eliminar el género")
            }
        }
    }

    fun resetFormState() {
        _formState.value = null
    }

    fun resetDeleteState() {
        _deleteState.value = null
    }

    fun resetGeneroFormState() {
        _generoFormState.value = null
    }

    fun resetDeleteGeneroState() {
        _deleteGeneroState.value = null
    }

}