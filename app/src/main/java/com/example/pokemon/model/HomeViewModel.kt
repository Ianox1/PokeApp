package com.example.pokemon.model


import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class HomeScreenState(
    val selectedFilter: String? = null,
    val showFilterDialog: Boolean = false,
    val listState: LazyListState = LazyListState()
)


sealed class HomeScreenIntent {
    object ShowFilterDialog : HomeScreenIntent()
    object DismissFilterDialog : HomeScreenIntent()
    data class SetSelectedFilter(val filter: String?) : HomeScreenIntent()
}

class HomeViewModel : ViewModel() {

    private var _homeScreenState by mutableStateOf(HomeScreenState())
    val state: HomeScreenState
        get() = _homeScreenState

    fun handleHomeScreenIntent(intent: HomeScreenIntent) {
        when (intent) {
            is HomeScreenIntent.ShowFilterDialog -> {
                _homeScreenState = _homeScreenState.copy(showFilterDialog = true)
            }
            is HomeScreenIntent.DismissFilterDialog -> {
                _homeScreenState = _homeScreenState.copy(showFilterDialog = false)
            }
            is HomeScreenIntent.SetSelectedFilter -> {
                _homeScreenState = _homeScreenState.copy(selectedFilter = intent.filter)
            }
        }
    }
}
