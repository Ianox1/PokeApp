package com.example.pokemon.ui.screens.pokeDetail

import android.provider.Settings.Global.getString
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.pokemon.R
import com.example.pokemon.model.DialogState
import com.example.pokemon.model.PokemonDetailsState
import com.example.pokemon.model.PokemonDialogModelView
import com.example.pokemon.model.PokemonState
import com.example.pokemon.model.PokemonViewModel
import com.example.pokemon.utils.createGradientBrush
import com.example.pokemon.utils.getColorFromType
import com.example.pokemon.utils.getDrawableFromType

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun PokemonDetailsScreen(
    modifier: Modifier = Modifier,
    pokemonDetailsState: PokemonDetailsState,
    onChangeDialog: () -> Unit,
    onChangeFront: () -> Unit,
    onUpdateColor: (String) -> Unit,
    pokemonDialogState: DialogState,
    onChangeCry: () -> Unit ,
    getDescription: (String) -> Unit
) {
    pokemonDetailsState.currentPokemon?.types?.get(0)?.type?.name?.let {
        onUpdateColor(it)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(
                getDrawableFromType(
                    pokemonDetailsState.currentPokemon?.types?.get(0)?.type?.name ?: stringResource(
                        R.string.defecto)
                )
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 100.dp, end = 20.dp, start = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            pokemonDetailsState.currentPokemon?.sprites?.other?.showdown?.let { sprites ->
                val imageUrl = if (pokemonDetailsState.isShowingFront) sprites.frontDefault else sprites.backDefault

                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(
                            brush = createGradientBrush(pokemonDetailsState.currentPokemon!!.types),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(10.dp)
                        .combinedClickable(
                            onDoubleClick = { onChangeDialog() },
                            onClick = { onChangeFront() }
                        )
                ) {
                    GlideImage(
                        model = imageUrl,
                        contentDescription = stringResource(R.string.imagen_pokemon),
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.Center),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            pokemonDetailsState.currentPokemon?.species?.name?.let { name ->
                Text(
                    text = "Nombre:",
                    style = MaterialTheme.typography.titleMedium,
                    color = pokemonDetailsState.titleTextColor
                )
                Card(
                    colors = CardDefaults.cardColors(containerColor = pokemonDetailsState.primaryTypeColor),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = name.capitalize(),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = Color.White
                    )
                }
            }

            Text(
                text = stringResource(R.string.info),
                style = MaterialTheme.typography.titleMedium,
                color = pokemonDetailsState.titleTextColor
            )
            Card(
                colors = CardDefaults.cardColors(containerColor = pokemonDetailsState.primaryTypeColor),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = String.format("#%03d", pokemonDetailsState.currentPokemon?.id),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp),
                    color = Color.White
                )
            }


            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val altura = stringResource(R.string.altura,
                    pokemonDetailsState.currentPokemon?.height.toString()
                )
                val peso = stringResource(R.string.peso,
                    pokemonDetailsState.currentPokemon?.weight.toString()
                )

                listOf(
                    altura,
                    peso
                ).forEach { text ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = pokemonDetailsState.primaryTypeColor),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp).fillMaxWidth(),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tipos:",
                style = MaterialTheme.typography.titleMedium,
                color = pokemonDetailsState.titleTextColor
            )
            Row(modifier = Modifier.padding(8.dp)) {

                pokemonDetailsState.currentPokemon?.types?.forEach { tipo ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = getColorFromType(tipo.type.name)),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = tipo.type.name.capitalize(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 15.dp, vertical= 5.dp),
                            color = Color.White
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.habilidades),
                style = MaterialTheme.typography.titleMedium,
                color = pokemonDetailsState.titleTextColor
            )
            pokemonDetailsState.currentPokemon?.abilities?.let { abilities ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = pokemonDetailsState.primaryTypeColor),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        abilities.forEach { habilidad ->
                            Text(
                                text = habilidad.ability.name.capitalize(),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Text(
                text = stringResource(R.string.estadisticas),
                style = MaterialTheme.typography.titleMedium,
                color = pokemonDetailsState.titleTextColor
            )
           pokemonDetailsState.currentPokemon?.stats?.let { stats ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 50.dp, top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = pokemonDetailsState.primaryTypeColor)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        stats.forEach { stat ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stat.stat.name.capitalize(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                                Text(

                                    text = stringResource(R.string.stat_format,stat.baseStat,stat.effort),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
            if (pokemonDetailsState.isDialogVisible) {
                PokemonDialog(
                    imageUrl = pokemonDetailsState.currentPokemon?.sprites?.other?.showdown?.frontShiny,
                    backgroundDrawable = getDrawableFromType(
                        pokemonDetailsState.currentPokemon?.types?.get(0)?.type?.name ?: stringResource(R.string.defecto)
                    ),
                    onDismiss = { onChangeDialog() },
                    getUrl = pokemonDetailsState.currentPokemon?.species?.url,
                    pokemonDetailsState = pokemonDetailsState,
                    pokemonDialogState = pokemonDialogState,
                    onChangeCry = {onChangeCry()},
                    getDescription = {getDescription(it)}
                )
            }
        }
    }
}