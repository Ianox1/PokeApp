package com.example.pokemon.ui.screens.pokeDetail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.pokemon.model.DialogState
import com.example.pokemon.model.PokemonDetailsState
import com.example.pokemon.model.PokemonDialogModelView
import com.example.pokemon.model.PokemonViewModel
import com.example.pokemon.utils.getColorFromType
import com.example.pokemon.utils.playCry

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonDialog(
    imageUrl: String?,
    backgroundDrawable: Int,
    onDismiss: () -> Unit,
    getUrl: String?,
    pokemonDetailsState: PokemonDetailsState,
    pokemonDialogState: DialogState,
    onChangeCry: () -> Unit,
    getDescription: (String) -> Unit
) {

    var isLatestCryState by remember { mutableStateOf(true) }


    LaunchedEffect(getUrl) {
        getUrl?.let {
            getDescription(it) }
    }
    LaunchedEffect(pokemonDialogState.isLatestCry) {
        isLatestCryState = pokemonDialogState.isLatestCry
        Log.e("Recomposition", "isLatestCry cambió a: ${pokemonDialogState.isLatestCry}")
    }


    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
        ) {
            Image(
                painter = painterResource(backgroundDrawable),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(
                    model = imageUrl,
                    contentDescription = "Imagen del Pokémon",
                    modifier = Modifier
                        .size(300.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    val cries = pokemonDetailsState.currentPokemon?.cries
                                    val cryUrl = if (cries?.legacy.isNullOrEmpty()) {
                                        cries?.latest // Si `legacy` es nulo o vacío, siempre usa `latest`
                                    } else {
                                        Log.e("hola", pokemonDialogState.isLatestCry.toString() )
                                        if (isLatestCryState) cries?.latest else cries?.legacy
                                    }
                                    cryUrl?.let {
                                        playCry(it) // Reproduce el sonido desde la URL
                                        onChangeCry()
                                        Log.e("hola2", pokemonDialogState.isLatestCry.toString() )
                                    }
                                }
                            )
                        },
                    contentScale = ContentScale.Fit
                )

            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .clickable { onDismiss() }
            ) {
                Text(
                    text = "X",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                val description = pokemonDialogState.description
                description?.flavorTextEntries?.get(5)?.let { flavorTextEntry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        colors = CardDefaults.cardColors(
                            containerColor = getColorFromType(
                                pokemonDetailsState.currentPokemon?.types?.get(0)?.type?.name ?: "hola"
                            )
                        )
                    ) {
                        Text(
                            text = flavorTextEntry.flavorText.replace("\n", " "),
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}