package com.example.pokemon.ui

import android.annotation.SuppressLint

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokemon.model.HomeScreenIntent
import com.example.pokemon.model.HomeViewModel
import com.example.pokemon.model.PokeIntent
import com.example.pokemon.model.PokeUiState
import com.example.pokemon.model.PokemonDetailsIntent
import com.example.pokemon.model.PokemonDetailsViewModel
import com.example.pokemon.model.PokemonDialogModelView
import com.example.pokemon.model.PokemonViewModel
import com.example.pokemon.model.SharedViewModel
import com.example.pokemon.ui.screens.mains.HomeScreen
import com.example.pokemon.ui.screens.mains.LoginScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun PokeApp(innerPadding: PaddingValues) {
    val sharedViewModel: SharedViewModel = hiltViewModel()
    val pokeViewModel: PokemonViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val detailsViewModel: PokemonDetailsViewModel = hiltViewModel()
    val dialogModelView: PokemonDialogModelView = hiltViewModel()

    pokeViewModel.setSharedViewModel(sharedViewModel)
    detailsViewModel.setSharedViewModel(sharedViewModel)




    Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            when (pokeViewModel.pokeUiState) {
                is PokeUiState.Login -> {
                    LoginScreen()
                }
                else -> {
                        HomeScreen(
                            pokeUiState = pokeViewModel.pokeUiState,
                            homeScreenState = homeViewModel.state,
                            pokemonState = pokeViewModel.state,
                            pokemonDetailsState = detailsViewModel.state,
                            pokemonDialogState = dialogModelView.dialogState,
                            onDimiss = {homeViewModel.handleHomeScreenIntent(HomeScreenIntent.DismissFilterDialog)},
                            onClickFilter = { homeViewModel.handleHomeScreenIntent(HomeScreenIntent.SetSelectedFilter(it)) },
                            onClick = {homeViewModel.handleHomeScreenIntent(HomeScreenIntent.ShowFilterDialog)},
                            getPokeType ={ pokeViewModel.handleIntent(PokeIntent.GetPokemonTypeDetails(it)) } ,
                            cargarData = {pokeViewModel.handleIntent(PokeIntent.CargarDatos)},
                            pokemonType = {pokeViewModel.handleIntent(PokeIntent.GetPokemonResponseType(it))},
                            onBackArrowClick = {detailsViewModel.handleIntent(PokemonDetailsIntent.MoveToLista)},
                            moveToDetalles = {pokeViewModel.handleIntent(PokeIntent.moveToDetalles(it))},
                            onLoad = {pokeViewModel.handleIntent(PokeIntent.LoadMorePokemons)},
                            pokemonDetails = {pokeViewModel.handleIntent(PokeIntent.GetPokemonDetails(it))},
                            onPokeSearch = {pokeViewModel.handleIntent(PokeIntent.SearchPokemons(it))},
                            onChangeDialog = {detailsViewModel.handleIntent(PokemonDetailsIntent.ToggleDialogVisibility)},
                            onChangeFront = {detailsViewModel.handleIntent(PokemonDetailsIntent.TogglePokemonView)},
                            onUpdateColor = {detailsViewModel.updateTypeColors(it)},
                            onChangeCry = {dialogModelView.toggleLatestCry()},
                            getDescription = {dialogModelView.fetchDialogPokemonDescription(it)}
                        )

                }
            }
        }

}




