package com.example.pokemon.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemon.data.network.PokemonDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class VentanaState(
    val ventanaLista: Boolean = true,
    val ventanaDetalles: Boolean = false
)


@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _ventanaState = MutableLiveData(VentanaState())
    val ventanaState: LiveData<VentanaState> get() = _ventanaState

    private val _currentPokemon = MutableLiveData<PokemonDetails?>()
    val currentPokemon: LiveData<PokemonDetails?> get() = _currentPokemon

    fun moveToDetalles(pokemon: PokemonDetails) {
        _ventanaState.value = _ventanaState.value?.copy(
            ventanaLista = false,
            ventanaDetalles = true
        )
        _currentPokemon.value = pokemon
    }

    fun moveToLista() {
        _ventanaState.value = _ventanaState.value?.copy(
            ventanaLista = true,
            ventanaDetalles = false
        )
    }

}



