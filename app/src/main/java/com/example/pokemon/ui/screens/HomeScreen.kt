package com.example.pokemon.ui.screens




import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.pokemon.R
import com.example.pokemon.data.network.GeneralType
import com.example.pokemon.data.network.Pokemon
import com.example.pokemon.data.network.Type
import com.example.pokemon.model.PokeIntent
import com.example.pokemon.model.PokeUiState
import com.example.pokemon.model.PokemonViewModel
import com.example.pokemon.ui.theme.PokemonTheme

@Composable
fun HomeScreen(
    viewModel: PokemonViewModel,
    pokeUiState: PokeUiState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val listState = rememberLazyListState()
    when (pokeUiState) {
        is PokeUiState.Login -> LoginScreen(modifier = modifier.fillMaxSize())
        is PokeUiState.Success ->
            if (viewModel.state.ventanaLista){
                ResultScreen(
                    pokeUiState.listaPoke,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(contentPadding),
                    viewModel = viewModel,
                    listState = listState

                )
            }else{
                PokemonDetailsScreen(viewModel)
            }

        is PokeUiState.Error -> ErrorScreen( modifier = modifier.fillMaxSize())
        is PokeUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }


    }
}



@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }
    val customFont = FontFamily(
        Font(R.font.backso)
    )
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 3000)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center) // Centra el contenido
        ) {
            Image(
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.pokeball),
                contentDescription = null
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(23.dp),
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                textAlign = TextAlign.Center,
                text = "CARGANDO...",
                color = Color.White
            )

            LinearProgressIndicator(
                progress = progress.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp), // Margen horizontal para un diseño más limpio
                color = Color.Red // Color de la barra
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(60.dp),
                fontFamily =  customFont,
                textAlign = TextAlign.Center,
                fontSize = 50.sp,
                text = "PokeApp",
                color = Color.Red
            )

        }
    }
}






@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = "Error de carga", modifier = Modifier.padding(16.dp))
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    lista: List<Pokemon>,
    modifier: Modifier = Modifier,
    viewModel: PokemonViewModel,
    listState: LazyListState
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .offset(y = (-20).dp)
    ) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 15.dp, start = 10.dp)
                ) {
                    SearchBar(viewModel = viewModel)
                }
            }
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(lista) { index, pokemon ->
                PokemonItem(pokemon = pokemon, viewModel = viewModel)

                if (index == lista.lastIndex) {
                    viewModel.handleIntent(PokeIntent.LoadMorePokemons)
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




@Composable
fun PokemonItem(
    pokemon: Pokemon,
    viewModel: PokemonViewModel,
    modifier: Modifier = Modifier
) {
    val pokemonDetails = viewModel.state.pokemonDetailsMap[pokemon.url]
    var color: Color
    LaunchedEffect(pokemon.url) {
        if (pokemonDetails == null) {
            viewModel.handleIntent(PokeIntent.GetPokemonDetails(pokemon.url))
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
                    viewModel.moveToDetalles(pokemonDetails)
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
                        pokemonDetails.sprites?.frontDefault?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Imagen del Pokémon",
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        if (pokemonDetails.types.any { it.type.name == "dark" }) {
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
                                    if (tipo.type.name == "dark"){
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
                            text = "Cargando detalles...",
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonDetailsScreen(
    viewModel: PokemonViewModel,
    modifier: Modifier = Modifier
) {
    var isShowingFront by remember { mutableStateOf(true) }
    val primaryTypeColor = viewModel.state.currentPokemon?.types?.get(0)?.type?.name?.let {
        getColorFromType(it)
    } ?: Color.Gray // Color por defecto si no hay tipo

    val titleTextColor = if (viewModel.state.currentPokemon?.types?.get(0)?.type?.name == "poison"|| viewModel.state.currentPokemon?.types?.get(0)?.type?.name == "dark" || viewModel.state.currentPokemon?.types?.get(0)?.type?.name == "fire" || viewModel.state.currentPokemon?.types?.get(0)?.type?.name == "psychic" ) {
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
                        .clickable { isShowingFront = !isShowingFront }
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

fun createGradientBrush(types: List<Type>?): Brush {
    val colors = types?.map { type ->
        when (type.type.name) {
            "fire" -> Color(0xFFf17f30)
            "water" -> Color(0xFF6892ef)
            "grass" -> Color(0xFF6dc73c)
            "electric" -> Color(0xFFe8d954)
            "psychic" -> Color(0xFFe05f84)
            "rock" -> Color(0xFFafa229)
            "ground" -> Color(0xFFA0522D)
            "ice" -> Color(0xFF91dcda)
            "dragon" -> Color(0xFF8A2BE2)
            "dark" -> Color.Black
            "fairy" -> Color(0xFFFFB6C1)
            "poison" -> Color(0xFFa23da3)
            "flying" -> Color(0xFF8fa9de)
            "normal" -> Color(0xFFa6a979)
            "bug" -> Color(0xFFa8b81d)
            "fighting" ->Color(0xFFb83425)
            "ghost" -> Color(0xFF71589a)
            "steel" -> Color(0xFFb6b1cd)
            else -> Color.LightGray
        }
    } ?: listOf(Color.LightGray)


    return Brush.horizontalGradient(
        if (colors.size > 1) colors else listOf(colors[0], colors[0])
    )
}
fun getColorFromType(typeName: String): Color {
    return when (typeName) {
        "fire" -> Color(0xFFf17f30)
        "water" -> Color(0xFF6892ef)
        "grass" -> Color(0xFF6dc73c)
        "electric" -> Color(0xFFe8d954)
        "psychic" -> Color(0xFFe05f84)
        "rock" -> Color(0xFFafa229)
        "ground" -> Color(0xFFA0522D)
        "ice" -> Color(0xFF91dcda)
        "dragon" -> Color(0xFF8A2BE2)
        "dark" -> Color.Black
        "fairy" -> Color(0xFFFFB6C1)
        "poison" -> Color(0xFFa23da3)
        "flying" -> Color(0xFF8fa9de)
        "normal" -> Color(0xFFa6a979)
        "bug" -> Color(0xFFa8b81d)
        "fighting" ->Color(0xFFb83425)
        "ghost" -> Color(0xFF71589a)
        "steel" -> Color(0xFFb6b1cd)
        else -> Color.LightGray // Color predeterminado para tipos desconocidos
    }
}

fun getDrawableFromType(typeName: String): Int {
    return when (typeName) {
        "fire" -> R.drawable.fire_icon
        "water" -> R.drawable.water_icon
        "grass" -> R.drawable.grass_icon
        "electric" -> R.drawable.electric_icon
        "psychic" -> R.drawable.psychic_icon
        "rock" -> R.drawable.rock_icon
        "ground" -> R.drawable.ground_icon
        "ice" -> R.drawable.ice_icon
        "dragon" -> R.drawable.dragon_icon
        "dark" -> R.drawable.dark_icon
        "fairy" -> R.drawable.fairy_icon
        "poison" -> R.drawable.poison_icon
        "flying" -> R.drawable.flying_icon
        "normal" -> R.drawable.normal_icon
        "bug" -> R.drawable.bug_icon
        "fighting" -> R.drawable.fighting_icon
        "ghost" -> R.drawable.ghost_icon
        "steel" -> R.drawable.steel_icon
        else -> R.drawable.ic_connection_error // Imagen predeterminada para tipos desconocidos
    }
}






