package com.example.pokemon.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.pokemon.data.network.PokemonDetails
import com.example.pokemon.utils.getColorFromType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class PokemonDetailsState(
    val isDialogVisible: Boolean = false,
    val isShowingFront: Boolean = true,
    val primaryTypeColor: Color = Color.Gray,
    val titleTextColor: Color = Color.Black,
    var currentPokemon: PokemonDetails? = null,
    val ventanaDetalles: Boolean = false
)


sealed class PokemonDetailsIntent {
    object ToggleDialogVisibility : PokemonDetailsIntent()
    object TogglePokemonView : PokemonDetailsIntent()
    object MoveToLista: PokemonDetailsIntent()
}


@HiltViewModel
class PokemonDetailsViewModel @Inject constructor() : ViewModel() {

    private var sharedViewModel: SharedViewModel? = null

    private val darkTextTypes = listOf("poison", "dragon", "dark", "fire", "psychic", "ghost")

    private var _state by mutableStateOf(PokemonDetailsState())
    val state: PokemonDetailsState
        get() = _state

    fun setSharedViewModel(sharedViewModel: SharedViewModel) {
        this.sharedViewModel = sharedViewModel

        this.sharedViewModel?.ventanaState?.observeForever { ventanaState ->
            updateVentanaDetalles(ventanaState.ventanaDetalles)
        }

        this.sharedViewModel?.currentPokemon?.observeForever { pokemon ->
            updateCurrentPokemon(pokemon)
        }
    }

    private fun updateVentanaDetalles(ventanaDetalles: Boolean) {
        _state = _state.copy(ventanaDetalles = ventanaDetalles)
    }

    private fun updateCurrentPokemon(currentPokemon: PokemonDetails?) {
        _state = _state.copy(currentPokemon = currentPokemon)
    }

    fun handleIntent(intent: PokemonDetailsIntent) {
        when (intent) {
            is PokemonDetailsIntent.ToggleDialogVisibility -> toggleDialogVisibility()
            is PokemonDetailsIntent.TogglePokemonView -> togglePokemonView()
            is PokemonDetailsIntent.MoveToLista -> moveToLista()
        }
    }

    private fun toggleDialogVisibility() {
        _state = _state.copy(isDialogVisible = !_state.isDialogVisible)
    }

    private fun togglePokemonView() {
        _state = _state.copy(isShowingFront = !_state.isShowingFront)
    }

    fun updateTypeColors(primaryTypeName: String?) {
        val primaryColor = primaryTypeName?.let { getColorFromType(it) } ?: Color.Gray
        val textColor = if (primaryTypeName in darkTextTypes) Color.White else Color.Black

        _state = _state.copy(
            primaryTypeColor = primaryColor,
            titleTextColor = textColor
        )
    }

    private fun moveToLista() {
        sharedViewModel?.moveToLista()
    }

    override fun onCleared() {
        super.onCleared()
        sharedViewModel?.ventanaState?.removeObserver { }
        sharedViewModel?.currentPokemon?.removeObserver { }
    }
}

