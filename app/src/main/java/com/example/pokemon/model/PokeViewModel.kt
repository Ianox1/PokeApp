package com.example.pokemon.model

import android.net.http.HttpException
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.data.network.GeneralType
import com.example.pokemon.data.network.PokeApi
import com.example.pokemon.data.network.Pokemon
import com.example.pokemon.data.network.PokemonDetails
import com.example.pokemon.data.network.PokemonResponseType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException


data class SearchState(
    val searchQuery: String = ""
)

data class PokemonState(
    val allPokemons: List<Pokemon> = emptyList(),
    val allPokemonsType: List<Pokemon> = emptyList(),
    val pokemonTypes: List<GeneralType> = emptyList(),
    val pokemonDetailsMap: Map<String, PokemonDetails?> = emptyMap(),
    val typeDetailsMap: Map<String, PokemonResponseType?> = emptyMap(),
    val nextUrl: String = "https://pokeapi.co/api/v2/pokemon",
    var canLoadMore: Boolean = true,
    val searchState: SearchState = SearchState(),
    var currentPokemon: PokemonDetails? = null,
    var ventanaLista: Boolean = true,
    var ventanaDetalles: Boolean = false
)

sealed interface PokeUiState {
    object Login : PokeUiState
    object Loading : PokeUiState
    object Error : PokeUiState
    data class Success(val listaPoke: List<Pokemon>) : PokeUiState
}

sealed interface PokeIntent {
    object LoadMorePokemons : PokeIntent
    object LoadPokemonTypes : PokeIntent
    data class SearchPokemons(val query: String) : PokeIntent
    data class GetPokemonDetails(val url: String) : PokeIntent
    data class GetPokemonTypeDetails(val typeUrl: String) : PokeIntent
    data class GetPokemonResponseType(val typeName: String) : PokeIntent
    object CargarDatos : PokeIntent
}

class PokemonViewModel : ViewModel() {

    private var _state by mutableStateOf(PokemonState())
    val state: PokemonState
        get() = _state

    var pokeUiState: PokeUiState by mutableStateOf(PokeUiState.Login)
        private set

    init {
        getPokeData()
    }

    fun handleIntent(intent: PokeIntent) {
        when (intent) {
            is PokeIntent.LoadMorePokemons -> loadMorePokemons()
            is PokeIntent.LoadPokemonTypes -> loadPokemonTypes()
            is PokeIntent.SearchPokemons -> updateSearchQuery(intent.query)
            is PokeIntent.GetPokemonDetails -> getPokemonDetails(intent.url)
            is PokeIntent.GetPokemonTypeDetails -> getPokemonTypeDetails(intent.typeUrl)
            is PokeIntent.GetPokemonResponseType -> getPokemonResponseType(intent.typeName)
            is PokeIntent.CargarDatos -> cargarDatos()
        }
    }

    fun getPokeData() {
        viewModelScope.launch {
            pokeUiState = PokeUiState.Login // Estado inicial "Login" mientras se espera
            delay(3000) // Simula un retraso como en el código original
            pokeUiState = try {
                val listResult = PokeApi.retrofitService.getPokemons(_state.nextUrl).results
                val response = PokeApi.retrofitService.getPokemons(_state.nextUrl)
                _state = _state.copy(
                    allPokemons = listResult,
                    allPokemonsType = listResult,
                    nextUrl = response.next.toString() // Actualiza `nextUrl`
                )
                PokeUiState.Success(listResult) // Estado de éxito con los datos obtenidos
            } catch (e: IOException) {
                PokeUiState.Error // Maneja un error de red
            }
        }
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            pokeUiState = PokeUiState.Loading
            try {
                pokeUiState = PokeUiState.Success(_state.allPokemons) // Restaura la lista completa
                _state = _state.copy(allPokemonsType = _state.allPokemons)
                searchPokemons(_state.searchState.searchQuery)
                _state.canLoadMore = true
            } catch (e: Exception) {
                pokeUiState = PokeUiState.Error
            }
        }
    }

    private fun loadMorePokemons() {
        if (_state.canLoadMore) {
            viewModelScope.launch {
                pokeUiState = PokeUiState.Loading
                try {
                    val response = PokeApi.retrofitService.getPokemons(_state.nextUrl)
                    val newPokemons = response.results
                    _state = _state.copy(
                        allPokemons = _state.allPokemons + newPokemons,
                        allPokemonsType = _state.allPokemonsType + newPokemons,
                        nextUrl = response.next.toString()
                    )
                    pokeUiState = PokeUiState.Success(_state.allPokemons)
                } catch (e: IOException) {
                    PokeUiState.Error
                }
            }
        }
    }

    private fun loadPokemonTypes() {
        viewModelScope.launch {
            try {
                val typeResponse = PokeApi.retrofitService.getTypes()
                _state = _state.copy(
                    pokemonTypes = typeResponse.results
                )
            } catch (e: IOException) {
                PokeUiState.Error
            }
        }
    }

    private fun updateSearchQuery(query: String) {
        _state = _state.copy(
            searchState = _state.searchState.copy(searchQuery = query)
        )
        searchPokemons(query)
    }

    private fun searchPokemons(query: String) {
        val filteredPokemons = if (query.isEmpty()) {
            _state.canLoadMore = true
            _state.allPokemonsType
        } else {
            _state.canLoadMore = false
            _state.allPokemonsType.filter { it.name.contains(query, ignoreCase = true) }
        }
        pokeUiState = PokeUiState.Success(filteredPokemons)
    }

    private fun getPokemonDetails(url: String) {
        viewModelScope.launch {
            val details = try {
                PokeApi.retrofitService.getPokemonDetails(url)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            val updatedDetailsMap = _state.pokemonDetailsMap.toMutableMap()
            updatedDetailsMap[url] = details
            _state = _state.copy(
                pokemonDetailsMap = updatedDetailsMap
            )
        }
    }

    private fun getPokemonTypeDetails(typeUrl: String) {
        viewModelScope.launch {
            try {
                val response = PokeApi.retrofitService.getTypeDetails(typeUrl)
                val updatedTypeDetailsMap = _state.typeDetailsMap.toMutableMap()
                updatedTypeDetailsMap[typeUrl] = response
                _state = _state.copy(
                    typeDetailsMap = updatedTypeDetailsMap
                )
            } catch (e: IOException) {
                pokeUiState = PokeUiState.Error
            }
        }
    }

    private fun getPokemonResponseType(typeName: String) {
        viewModelScope.launch {
            pokeUiState = PokeUiState.Loading
            try {
                val response = PokeApi.retrofitService.getTypePoke(typeName)
                val pokemons = response.pokemon.map { Pokemon(it.pokemon.name, it.pokemon.url) }
                _state = _state.copy(
                    allPokemonsType = pokemons
                )
                pokeUiState = PokeUiState.Success(pokemons)
                searchPokemons(_state.searchState.searchQuery)
                _state.canLoadMore = false
            } catch (e: IOException) {
                pokeUiState = PokeUiState.Error
            }
        }
    }

    fun moveToLista() {
        _state = _state.copy(
            ventanaLista = true,
            ventanaDetalles = false
        )
    }


    fun moveToDetalles(selectedPokemon: PokemonDetails) {
        _state = _state.copy(
            ventanaLista = false,
            ventanaDetalles = true,
            currentPokemon = selectedPokemon
        )
    }
}
