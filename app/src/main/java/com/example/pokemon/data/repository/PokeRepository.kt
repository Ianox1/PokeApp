package com.example.pokemon.data.repository

import com.example.pokemon.data.network.PokeApiService
import com.example.pokemon.data.network.PokemonDetails
import com.example.pokemon.data.network.PokemonFlavorText
import com.example.pokemon.data.network.PokemonResponse
import com.example.pokemon.data.network.PokemonResponseType
import com.example.pokemon.data.network.TypeResponse

interface PokeRepository {
    suspend fun getPokemons(url: String): PokemonResponse
    suspend fun getPokemonDetails(url: String): PokemonDetails
    suspend fun getTypes(): TypeResponse
    suspend fun getTypeDetails(url: String): PokemonResponseType
    suspend fun getTypePoke(type: String): PokemonResponseType
    suspend fun getDescription(url: String): PokemonFlavorText
}

class PokeRepositoryImpl(private val pokeApiService: PokeApiService) : PokeRepository {
    override suspend fun getPokemons(url: String): PokemonResponse {
        return pokeApiService.getPokemons(url)
    }

    override suspend fun getPokemonDetails(url: String): PokemonDetails {
        return pokeApiService.getPokemonDetails(url)
    }

    override suspend fun getTypes(): TypeResponse {
        return pokeApiService.getTypes()
    }

    override suspend fun getTypeDetails(url: String): PokemonResponseType {
        return pokeApiService.getTypeDetails(url)
    }

    override suspend fun getTypePoke(type: String): PokemonResponseType {
        return pokeApiService.getTypePoke(type)
    }

    override suspend fun getDescription(url: String): PokemonFlavorText {
        return pokeApiService.getDescription(url)
    }
}
