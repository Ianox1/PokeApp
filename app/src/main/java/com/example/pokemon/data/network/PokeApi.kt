package com.example.pokemon.data.network

object PokeApi {
    val retrofitService : PokeApiService by lazy {
        retrofit.create(PokeApiService::class.java)
    }
}