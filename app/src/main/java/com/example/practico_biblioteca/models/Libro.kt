package com.example.practico_biblioteca.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

data class Libro(
    val id: Int,
    @SerializedName(value = "nombre", alternate = ["titulo"])
    val titulo: String,
    val autor: String,
    val editorial: String,
    val imagen: String,
    val sinopsis: String,
    val isbn: String,
    val calificacion: Double,
    @JsonAdapter(GeneroIdsAdapter::class)
    val generos: List<Int> = emptyList()
)
