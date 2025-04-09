package com.example.pokemon.ui.screens.mains



import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import com.example.pokemon.data.network.PokemonDetails
import com.example.pokemon.model.DialogState
import com.example.pokemon.model.HomeScreenState

import com.example.pokemon.model.PokeUiState
import com.example.pokemon.model.PokemonDetailsState
import com.example.pokemon.model.PokemonState
import com.example.pokemon.model.PokemonViewModel
import com.example.pokemon.ui.PokeTopAppBar
import com.example.pokemon.ui.screens.mains.resultScreen.FilterDialog
import com.example.pokemon.ui.screens.mains.resultScreen.ResultScreen
import com.example.pokemon.ui.screens.pokeDetail.PokemonDetailsScreen
import com.example.pokemon.ui.theme.PokemonTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    pokemonState: PokemonState,
    pokeUiState: PokeUiState,
    pokemonDialogState: DialogState,
    modifier: Modifier = Modifier,
    homeScreenState: HomeScreenState,
    onDimiss: () -> Unit,
    onClickFilter: (String?) -> Unit,
    onClick: ()-> Unit,
    getPokeType: (String) -> Unit,
    cargarData: ()-> Unit,
    pokemonType: (String)-> Unit,
    onBackArrowClick: () -> Unit,
    moveToDetalles: (PokemonDetails) -> Unit,
    onLoad: ()-> Unit,
    pokemonDetails: (String) -> Unit,
    onPokeSearch: (String)-> Unit,
    pokemonDetailsState: PokemonDetailsState,
    onChangeDialog: () -> Unit,
    onChangeFront: () -> Unit ,
    onUpdateColor: (String) -> Unit,
    onChangeCry: () -> Unit,
    getDescription: (String) -> Unit

) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    if (homeScreenState.showFilterDialog) {
        FilterDialog(
            state = homeScreenState,
            onClick = { onClickFilter(it)},
            onDismiss = {onDimiss()},
            pokemonState = pokemonState,
            getPokeType = {getPokeType(it)},
            cargar = {cargarData()},
            pokemonsType = {
                pokemonType(it)
            }
        )
    }

    when (pokeUiState) {
        is PokeUiState.Login -> LoginScreen(modifier = modifier.fillMaxSize())
        is PokeUiState.Success -> {
            if (pokemonState.ventanaLista) {
                ResultScreen(
                    lista = pokeUiState.listaPoke,
                    state = homeScreenState,
                    scrollBehavior = scrollBehavior,
                    onClick = { onClick() },
                    moveToDetalles = {moveToDetalles(it)},
                    onLoad = {onLoad()},
                    pokemonState = pokemonState,
                    pokemonDetails = {pokemonDetails(it)},
                    onPokeSearch = {onPokeSearch(it)}

                )
            } else {
                Scaffold(
                    topBar = {
                        PokeTopAppBar(
                            scrollBehavior = null,
                            showBackArrow = pokemonDetailsState.ventanaDetalles,
                            onBackArrowClick = {onBackArrowClick() }
                        )
                    }
                ) {
                    PokemonDetailsScreen(
                        pokemonDetailsState = pokemonDetailsState,
                        onChangeDialog ={ onChangeDialog()},
                        onChangeFront = {onChangeFront()},
                        onUpdateColor = {onUpdateColor(it)},
                        pokemonDialogState = pokemonDialogState,
                        onChangeCry = {onChangeCry()},
                        getDescription = {getDescription(it)}

                    )
                }
            }
        }
        is PokeUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
        is PokeUiState.Loading -> {
            ResultScreen(
                lista = pokemonState.allPokemons,
                state = homeScreenState,
                scrollBehavior = scrollBehavior,
                isLoading = true,
                onClick = { onClick() },
                moveToDetalles ={moveToDetalles(it)},
                onLoad = {onLoad()},
                pokemonState = pokemonState,
                pokemonDetails = {pokemonDetails(it)},
                onPokeSearch = {onPokeSearch(it)}
            )
        }
    }
}





@Preview(showBackground = true)
@Composable
fun loginPreview(){
    PokemonTheme {
        LoginScreen()
    }
}








