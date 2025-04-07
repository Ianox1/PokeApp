package com.example.pokemon.ui.screens.mains



import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.rememberLazyListState

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection

import com.example.pokemon.model.PokeUiState
import com.example.pokemon.model.PokemonViewModel
import com.example.pokemon.ui.screens.mains.resultScreen.ResultScreen
import com.example.pokemon.ui.screens.pokeDetail.PokemonDetailsScreen
import com.example.pokemon.ui.theme.PokemonTheme


@Composable
fun HomeScreen(
    viewModel: PokemonViewModel,
    pokeUiState: PokeUiState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val listState = rememberLazyListState()
    when (pokeUiState) {
        is PokeUiState.Login -> LoginScreen(modifier = modifier.fillMaxSize())
        is PokeUiState.Success ->
            if (viewModel.state.ventanaLista) {
                ResultScreen(
                    pokeUiState.listaPoke,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                            end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
                            bottom = contentPadding.calculateBottomPadding(),
                            top = contentPadding.calculateTopPadding()
                        ),
                    viewModel = viewModel,
                    listState = listState,
                    isLoading = false
                )
            } else {
                PokemonDetailsScreen(viewModel)
            }

        is PokeUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
        is PokeUiState.Loading ->
            ResultScreen(
                lista = viewModel.state.allPokemons,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                viewModel = viewModel,
                listState = listState,
                isLoading = true
            )
    }

}


@Preview(showBackground = true)
@Composable
fun loginPreview(){
    PokemonTheme {
        LoginScreen()
    }
}








