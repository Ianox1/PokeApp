package com.example.pokemon.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
val json = Json {
    ignoreUnknownKeys = true // Ignora claves desconocidas en el JSON
    coerceInputValues = true // Convierte valores null a valores predeterminados
}
private const val BASE_URL =
    "https://pokeapi.co/api/v2/"

val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()
