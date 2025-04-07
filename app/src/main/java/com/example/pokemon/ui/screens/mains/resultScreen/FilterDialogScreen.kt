package com.example.pokemon.ui.screens.mains.resultScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.pokemon.data.network.GeneralType
import com.example.pokemon.model.PokeIntent
import com.example.pokemon.model.PokemonViewModel

@Composable
fun FilterDialog(
    showDialog: MutableState<Boolean>,
    selectedFilter: MutableState<String?>,
    pokemonTypes: List<GeneralType>,
    viewModel: PokemonViewModel
) {
    Dialog(onDismissRequest = { showDialog.value = false }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Botón para cerrar el diálogo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { showDialog.value = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Título del diálogo
                Text(
                    text = "Filtrar por tipo:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 10.dp)
                        .offset(y = (-40).dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // Íconos en una cuadrícula de 3 columnas
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-30).dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(pokemonTypes) { typeDetail ->
                        val typeDetails = viewModel.state.typeDetailsMap[typeDetail.url]
                        if (typeDetails != null) {
                            if (typeDetails.sprites.generation.EspadaEscudo.icon != null) {
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth()
                                ) {
                                    AsyncImage(
                                        model = typeDetails.sprites.generation.EspadaEscudo.icon,
                                        contentDescription = typeDetail.name,
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(20.dp)
                                            .clickable {
                                                selectedFilter.value =
                                                    if (selectedFilter.value == typeDetail.name) null else typeDetail.name
                                                if (selectedFilter.value == typeDetail.name) {
                                                    viewModel.handleIntent(
                                                        PokeIntent.GetPokemonResponseType(
                                                            typeDetail.name
                                                        )
                                                    )
                                                } else {
                                                    viewModel.handleIntent(PokeIntent.CargarDatos)
                                                }
                                            },
                                        contentScale = ContentScale.FillBounds
                                    )

                                    if (selectedFilter.value == typeDetail.name) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Seleccionado",
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .size(16.dp),
                                            tint = Color.Green
                                        )
                                    }

                                }
                            }
                        }else {
                            LaunchedEffect(typeDetail.url) {
                                viewModel.handleIntent(PokeIntent.GetPokemonTypeDetails(typeDetail.url))
                            }
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(20.dp)
                                    .background(Color.Gray)
                            )

                        }
                    }
                }
            }
        }
    }
}