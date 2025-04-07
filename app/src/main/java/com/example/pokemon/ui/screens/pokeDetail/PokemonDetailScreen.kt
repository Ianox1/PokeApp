package com.example.pokemon.ui.screens.pokeDetail

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.pokemon.model.PokemonViewModel
import com.example.pokemon.utils.createGradientBrush
import com.example.pokemon.utils.getColorFromType
import com.example.pokemon.utils.getDrawableFromType

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun PokemonDetailsScreen(
    viewModel: PokemonViewModel,
    modifier: Modifier = Modifier
) {

    var isDialogVisible by remember { mutableStateOf(false) }

    var isShowingFront by remember { mutableStateOf(true) }
    val primaryTypeColor = viewModel.state.currentPokemon?.types?.get(0)?.type?.name?.let {
        getColorFromType(it)
    } ?: Color.Gray // Color por defecto si no hay tipo

    val darkTextTypes = listOf("poison", "dragon", "dark", "fire", "psychic", "ghost")
    val titleTextColor = if (viewModel.state.currentPokemon?.types?.get(0)?.type?.name in darkTextTypes) {
        Color.White
    } else {
        Color.Black
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(
                getDrawableFromType(
                    viewModel.state.currentPokemon?.types?.get(0)?.type?.name ?: "default"
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
            viewModel.state.currentPokemon?.sprites?.other?.showdown?.let { sprites ->
                val imageUrl = if (isShowingFront) sprites.frontDefault else sprites.backDefault

                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(
                            brush = createGradientBrush(viewModel.state.currentPokemon!!.types),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(10.dp)
                        .combinedClickable(
                            onDoubleClick = { isDialogVisible = true },
                            onClick = { isShowingFront = !isShowingFront }
                        )
                ) {
                    GlideImage(
                        model = imageUrl,
                        contentDescription = "Imagen del Pokémon",
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.Center),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            viewModel.state.currentPokemon?.species?.name?.let { name ->
                Text(
                    text = "Nombre:",
                    style = MaterialTheme.typography.titleMedium,
                    color = titleTextColor
                )
                Card(
                    colors = CardDefaults.cardColors(containerColor = primaryTypeColor),
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
                text = "Información Básica:",
                style = MaterialTheme.typography.titleMedium,
                color = titleTextColor
            )
            Card(
                colors = CardDefaults.cardColors(containerColor = primaryTypeColor),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = String.format("#%03d", viewModel.state.currentPokemon?.id),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp),
                    color = Color.White
                )
            }


            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "Altura: ${viewModel.state.currentPokemon?.height} dm",
                    "Peso: ${viewModel.state.currentPokemon?.weight} hg"
                ).forEach { text ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = primaryTypeColor),
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
                color = titleTextColor
            )
            Row(modifier = Modifier.padding(8.dp)) {

                viewModel.state.currentPokemon?.types?.forEach { tipo ->
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
                text = "Habilidades:",
                style = MaterialTheme.typography.titleMedium,
                color = titleTextColor
            )
            viewModel.state.currentPokemon?.abilities?.let { abilities ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = primaryTypeColor),
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
                text = "Estadísticas:",
                style = MaterialTheme.typography.titleMedium,
                color = titleTextColor
            )
            viewModel.state.currentPokemon?.stats?.let { stats ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 50.dp, top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = primaryTypeColor)
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
                                    text = "Base: ${stat.baseStat}, Esfuerzo: ${stat.effort}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
            if (isDialogVisible) {
                PokemonDialog(
                    imageUrl = viewModel.state.currentPokemon?.sprites?.other?.showdown?.frontDefault,
                    backgroundDrawable = getDrawableFromType(
                        viewModel.state.currentPokemon?.types?.get(0)?.type?.name ?: "default"
                    ),
                    onDismiss = { isDialogVisible = false },
                    getUrl = viewModel.state.currentPokemon?.species?.url,
                    viewModel = viewModel
                )
            }
        }
    }
}