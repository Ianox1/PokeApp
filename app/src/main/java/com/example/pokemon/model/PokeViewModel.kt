package com.example.pokemon.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.data.network.GeneralType
import com.example.pokemon.data.network.PokeApi
import com.example.pokemon.data.network.Pokemon
import com.example.pokemon.data.network.PokemonDetails
import com.example.pokemon.data.network.PokemonFlavorText
import com.example.pokemon.data.network.PokemonResponseType
import com.example.pokemon.data.repository.PokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


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
    val ventanaLista: Boolean = false
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
    data class moveToDetalles(val pokemonDetails: PokemonDetails) : PokeIntent
}


@HiltViewModel
class PokemonViewModel @Inject constructor() : ViewModel() {

    private var sharedViewModel: SharedViewModel? = null
    private var repository: PokeRepository? = null

    private var _state by mutableStateOf(PokemonState())
    val state: PokemonState
        get() = _state

    var pokeUiState: PokeUiState by mutableStateOf(PokeUiState.Login)
        private set

    fun setSharedViewModel(sharedViewModel: SharedViewModel) {
        this.sharedViewModel = sharedViewModel

        // Observamos los cambios en ventanaLista desde el SharedViewModel
        this.sharedViewModel?.ventanaState?.observeForever { ventanaState ->
            _state = _state.copy(
                ventanaLista = ventanaState.ventanaLista
            )
        }
    }

    fun setRepository(repository: PokeRepository) {
        this.repository = repository
    }

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
            is PokeIntent.moveToDetalles -> moveToDetalles(intent.pokemonDetails)
        }
    }

    fun getPokeData() {
        viewModelScope.launch {
            pokeUiState = PokeUiState.Login
            delay(3000)
            repository?.let {
                pokeUiState = try {
                    val listResult = it.getPokemons(_state.nextUrl).results
                    val response = it.getPokemons(_state.nextUrl)
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
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            pokeUiState = PokeUiState.Loading
            repository?.let {
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
    }

    private fun loadMorePokemons() {
        if (_state.canLoadMore && _state.nextUrl.isNotEmpty()) { // Verifica si hay ruta en nextUrl
            viewModelScope.launch {
                pokeUiState = PokeUiState.Loading
                repository?.let {
                    try {
                        val response = it.getPokemons(_state.nextUrl)
                        val newPokemons = response.results
                        _state = _state.copy(
                            allPokemons = _state.allPokemons + newPokemons,
                            allPokemonsType = _state.allPokemonsType + newPokemons,
                            nextUrl = response.next ?: "" // Asegura que nextUrl nunca sea null
                        )
                        pokeUiState = PokeUiState.Success(_state.allPokemons)
                    } catch (e: IOException) {
                        pokeUiState = PokeUiState.Error // Corrige para asignar un estado de error
                    }
                }
            }
        }
    }

    private fun loadPokemonTypes() {
        viewModelScope.launch {
            repository?.let {
                try {
                    val typeResponse = it.getTypes()
                    _state = _state.copy(
                        pokemonTypes = typeResponse.results
                    )
                } catch (e: IOException) {
                    PokeUiState.Error
                }
            }
        }
        Log.e("hola",state.pokemonTypes.size.toString())
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
            repository?.let {
                val details = try {
                    it.getPokemonDetails(url)
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
    }

    private fun getPokemonTypeDetails(typeUrl: String) {
        viewModelScope.launch {
            repository?.let {
                try {
                    val response = it.getTypeDetails(typeUrl)
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
    }

    private fun getPokemonResponseType(typeName: String) {
        viewModelScope.launch {
            repository?.let {
                try {
                    val response = it.getTypePoke(typeName)
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
    }

    fun moveToDetalles(pokemon: PokemonDetails) {
        sharedViewModel?.moveToDetalles(pokemon)
    }
}
