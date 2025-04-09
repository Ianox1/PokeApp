package com.example.pokemon.ui.screens.mains.resultScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pokemon.R
import com.example.pokemon.data.network.Pokemon
import com.example.pokemon.data.network.PokemonDetails
import com.example.pokemon.model.HomeScreenState
import com.example.pokemon.model.PokemonState
import com.example.pokemon.ui.PokeTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    lista: List<Pokemon>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior,
    state: HomeScreenState,
    onClick: () -> Unit,
    moveToDetalles: (PokemonDetails) -> Unit,
    onLoad : ()-> Unit,
    pokemonState: PokemonState,
    pokemonDetails: (String) -> Unit,
    onPokeSearch: (String)-> Unit
) {
    Scaffold(
        topBar =
        {
            PokeTopAppBar(
                scrollBehavior = scrollBehavior,
                showBackArrow = false,
                onBackArrowClick = null
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(20.dp, end = 5.dp, bottom = 10.dp)
                    .size(80.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(10.dp)),
                onClick = { onClick() },
                content = {
                    Column {
                        Image(
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.CenterHorizontally),
                            painter = painterResource(R.drawable.pokeball),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = modifier.padding(top = paddingValues.calculateTopPadding())) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    SearchBar(pokemonState = pokemonState, onPokeSearch = {onPokeSearch(it)})
                }
                LazyColumn(
                    state = state.listState,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    itemsIndexed(lista) { index, pokemon ->
                        PokemonItem(pokemon = pokemon, moveToDetalles = {moveToDetalles(it)},
                            pokemonState = pokemonState, pokemonDetails ={pokemonDetails(it)} )

                        if (index == lista.lastIndex && !isLoading) {
                            onLoad()
                        }
                    }

                    if (isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    )
}



@Composable
fun SearchBar(modifier: Modifier = Modifier,
              pokemonState: PokemonState,
              onPokeSearch: (String) -> Unit,
              ) {
    var searchQuery = pokemonState.searchState

    TextField(
        value = searchQuery.searchQuery,
        onValueChange = {
            onPokeSearch(it)
        },
        label = { Text(stringResource(R.string.banner_search), style = MaterialTheme.typography.bodySmall) },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(20.dp)),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodySmall,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.buscar),
                tint = Color.Gray
            )
        }
    )
}
