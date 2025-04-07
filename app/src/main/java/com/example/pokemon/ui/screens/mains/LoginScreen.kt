package com.example.pokemon.ui.screens.mains

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemon.R

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