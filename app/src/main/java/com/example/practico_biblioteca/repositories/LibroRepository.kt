package com.example.practico_biblioteca.repositories

import com.example.practico_biblioteca.RetrofitInstance
import com.example.practico_biblioteca.models.Libro
import com.example.practico_biblioteca.models.Genero

class LibroRepository {

    suspend fun getLibros(): List<Libro> {
        try {
            val retrofitInstance = RetrofitInstance.api
            return retrofitInstance.getLibros()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun getLibro(id: Int): Libro? {
        try {
            val retrofitInstance = RetrofitInstance.api
            return retrofitInstance.getLibro(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun getGeneros(): List<Genero> {
        try {
            val retrofitInstance = RetrofitInstance.api
            return retrofitInstance.getGeneros()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun createLibro(libro: Libro): Boolean {
        try {
            val retrofitInstance = RetrofitInstance.api
            retrofitInstance.createLibro(libro)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    suspend fun updateLibro(id: Int, libro: Libro): Boolean {
        try {
            val retrofitInstance = RetrofitInstance.api
            retrofitInstance.updateLibro(id, libro)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    suspend fun deleteLibro(id: Int): Boolean {
        try {
            val retrofitInstance = RetrofitInstance.api
            retrofitInstance.deleteLibro(id)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}