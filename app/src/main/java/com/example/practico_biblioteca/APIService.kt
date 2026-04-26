package com.example.practico_biblioteca

import com.example.practico_biblioteca.models.Genero
import com.example.practico_biblioteca.models.Libro
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Interfaz de la API — tu compañero configura RetrofitInstance apuntando a:
// https://apilibreria.jmacboy.com/api
interface APIService {

    // --- Libros ---
    @GET("libros")
    suspend fun getLibros(): List<Libro>

    @GET("libros/{id}")
    suspend fun getLibro(@Path("id") id: Int): Libro

    @POST("libros")
    suspend fun createLibro(@Body libro: Libro): Libro

    @PUT("libros/{id}")
    suspend fun updateLibro(@Path("id") id: Int, @Body libro: Libro): Libro

    @DELETE("libros/{id}")
    suspend fun deleteLibro(@Path("id") id: Int)

    // --- Géneros ---
    @GET("generos")
    suspend fun getGeneros(): List<Genero>
}