package com.example.pokemon.ui.screens.mains.resultScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokemon.R
import com.example.pokemon.data.network.Pokemon
import com.example.pokemon.data.network.PokemonDetails
import com.example.pokemon.model.PokeIntent
import com.example.pokemon.model.PokemonState
import com.example.pokemon.utils.createGradientBrush
import com.example.pokemon.utils.getColorFromType

@Composable
fun PokemonItem(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
    moveToDetalles: (PokemonDetails) -> Unit,
    pokemonState: PokemonState,
    pokemonDetails: (String) -> Unit,
) {
    val pokemonDetails = pokemonState.pokemonDetailsMap[pokemon.url]
    var color: Color
    LaunchedEffect(pokemon.url) {
        if (pokemonDetails == null) {
            pokemonDetails(pokemon.url)
        }
    }

    Box(
        modifier = modifier
            .padding(8.dp)
            .background(
                brush = createGradientBrush(pokemonDetails?.types),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                if (pokemonDetails != null) {
                    moveToDetalles(pokemonDetails)
                }
            }
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    if (pokemonDetails != null) {
                        pokemonDetails.sprites.frontDefault?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = stringResource(R.string.imagen_pokemon),
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        if (pokemonDetails.types.any { it.type.name == stringResource(R.string.dark) }) {
                            color = Color.White
                        } else {
                            color = Color.Black
                        }


                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = pokemon.name.capitalize(),
                                style = MaterialTheme.typography.titleLarge,
                                color = color
                            )
                            Row(modifier = Modifier.padding(top = 15.dp)) {
                                pokemonDetails.types.forEach { tipo ->
                                    if (tipo.type.name == stringResource(R.string.dark)){
                                        color = Color.White
                                    }else{
                                        color = Color.Black
                                    }
                                    Card(
                                        modifier = Modifier
                                            .width(100.dp)
                                            .border(2.dp, color, RoundedCornerShape(6.dp)) ,
                                        colors = CardDefaults.cardColors(
                                            containerColor = getColorFromType(tipo.type.name)
                                        )
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp),
                                            textAlign = TextAlign.Center,
                                            text = tipo.type.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = color
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.load_detail),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    if (pokemonDetails != null) {
                        Text(
                            text = String.format("#%03d", pokemonDetails.id),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}