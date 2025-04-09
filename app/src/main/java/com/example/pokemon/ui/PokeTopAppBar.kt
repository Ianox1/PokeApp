package com.example.pokemon.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.pokemon.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior?,
    showBackArrow: Boolean,
    onBackArrowClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val customFont = FontFamily(
        Font(R.font.backso)
    )
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
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
                if (onBackArrowClick != null) {
                    IconButton(onClick = onBackArrowClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver a la lista",
                            tint = Color.White
                        )
                    }
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