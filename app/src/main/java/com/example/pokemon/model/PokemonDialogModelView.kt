package com.example.pokemon.model

import android.util.Log
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.data.network.PokeApi
import com.example.pokemon.data.network.PokemonFlavorText
import com.example.pokemon.data.repository.PokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DialogState(
    val description: PokemonFlavorText? = null,
    val isLatestCry: Boolean = true
)

@HiltViewModel
class PokemonDialogModelView @Inject constructor(): ViewModel() {

    private var _dialogState by mutableStateOf(DialogState())
    val dialogState: DialogState
        get() = _dialogState

    private var repository: PokeRepository? = null

    fun setRepository(repository: PokeRepository) {
        this.repository = repository
    }

    fun fetchDialogPokemonDescription(url: String) {
        viewModelScope.launch {
            repository?.let {
                try {
                    val description = it.getDescription(url)
                    _dialogState = _dialogState.copy(description = description)
                } catch (e: Exception) {
                    e.printStackTrace()
                    _dialogState = _dialogState.copy(description = null)
                }
            }
        }
    }

    fun toggleLatestCry() {
        Log.e("hola3", dialogState.isLatestCry.toString())
        _dialogState = _dialogState.copy(isLatestCry = !_dialogState.isLatestCry)
        Log.e("hola4", dialogState.isLatestCry.toString())
    }
}

