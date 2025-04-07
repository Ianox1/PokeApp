package com.example.pokemon.ui.screens.mains.resultScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pokemon.data.network.Pokemon
import com.example.pokemon.model.PokeIntent
import com.example.pokemon.model.PokemonViewModel
import com.example.pokemon.ui.screens.mains.PokemonItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    lista: List<Pokemon>,
    modifier: Modifier = Modifier,
    viewModel: PokemonViewModel,
    listState: LazyListState,
    isLoading: Boolean = false
) {

    Column(
        modifier = modifier

    ) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 18.dp, start = 10.dp)
                ) {
                    SearchBar(
                        viewModel = viewModel
                    )
                }
            }
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(lista) { index, pokemon ->
                PokemonItem(pokemon = pokemon, viewModel = viewModel)

                if (index == lista.lastIndex && !isLoading) {
                    viewModel.handleIntent(PokeIntent.LoadMorePokemons)
                }
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(viewModel: PokemonViewModel, modifier: Modifier = Modifier) {
    var searchQuery = viewModel.state.searchState

    TextField(
        value = searchQuery.searchQuery,
        onValueChange = {
            viewModel.handleIntent(PokeIntent.SearchPokemons(it))
        },
        label = { Text("Buscar Pokémon", style = MaterialTheme.typography.bodySmall) }, // Texto más pequeño
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(20.dp)),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodySmall,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color.Gray
            )
        }
    )
}