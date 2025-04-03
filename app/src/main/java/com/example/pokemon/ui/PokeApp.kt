package com.example.pokemon.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokemon.R
import com.example.pokemon.model.PokeIntent
import com.example.pokemon.model.PokeUiState
import com.example.pokemon.model.PokemonViewModel
import com.example.pokemon.ui.screens.FilterDialog
import com.example.pokemon.ui.screens.HomeScreen
import com.example.pokemon.ui.screens.LoginScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeApp(innerPadding: PaddingValues) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val pokeViewModel: PokemonViewModel = viewModel()
    val showFilterDialog = remember { mutableStateOf(false) }
    val selectedFilter = remember { mutableStateOf<String?>(null) }

    Scaffold(
        // Elimina el comportamiento de desplazamiento si ventanaDetalles es true
        modifier = if (pokeViewModel.state.ventanaDetalles) Modifier else Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (pokeViewModel.pokeUiState !is PokeUiState.Login) {
                PokeTopAppBar(
                    // Pasa un comportamiento de scroll solo si ventanaDetalles es false
                    scrollBehavior = if (pokeViewModel.state.ventanaDetalles) null else scrollBehavior,
                    showBackArrow = pokeViewModel.state.ventanaDetalles,
                    onBackArrowClick = { pokeViewModel.moveToLista() }
                )
            }
        },
        floatingActionButton = {
            if (pokeViewModel.pokeUiState !is PokeUiState.Login && !pokeViewModel.state.ventanaDetalles) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(20.dp, end = 5.dp, bottom = 10.dp)
                        .size(80.dp)
                        .border(2.dp, Color.Black, RoundedCornerShape(10.dp)),
                    onClick = { showFilterDialog.value = true },
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
            }
        }
    ) {
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
                        contentPadding = it,
                        viewModel = pokeViewModel
                    )
                    if (showFilterDialog.value) {
                        pokeViewModel.handleIntent(PokeIntent.LoadPokemonTypes)
                        FilterDialog(
                            showDialog = showFilterDialog,
                            selectedFilter = selectedFilter,
                            pokemonTypes = pokeViewModel.state.pokemonTypes,
                            viewModel = pokeViewModel
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior?,
    showBackArrow: Boolean,
    onBackArrowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val customFont = FontFamily(
        Font(R.font.backso)
    )
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior, // Puede ser null para evitar el comportamiento scrollable
        title = {
            Text(
                text = "PokeApp",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Red,
                fontFamily = customFont
            )
        },
        navigationIcon = {
            if (showBackArrow) {
                IconButton(onClick = onBackArrowClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver a la lista",
                        tint = Color.White
                    )
                }
            }
        },
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black,
            scrolledContainerColor = Color.Black,
            titleContentColor = Color.Red
        )
    )
}


