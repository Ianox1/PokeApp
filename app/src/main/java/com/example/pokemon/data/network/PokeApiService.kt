package com.example.pokemon.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PokeApiService {
    @GET
    suspend fun getPokemons(@Url url: String): PokemonResponse
    @GET
    suspend fun getPokemonDetails(@Url url: String): PokemonDetails
    @GET("type")
    suspend fun getTypes(): TypeResponse
    @GET
    suspend fun getTypeDetails(@Url url: String): PokemonResponseType
    @GET("type/{type}")
    suspend fun getTypePoke(@Path("type") type: String): PokemonResponseType

}
