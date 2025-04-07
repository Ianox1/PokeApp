package com.example.pokemon.utils

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.pokemon.R
import com.example.pokemon.data.network.Type

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
            "fighting" -> Color(0xFFb83425)
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
        "fighting" -> Color(0xFFb83425)
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

fun playCry(cryUrl: String) {
    Log.e("cry",cryUrl)
    try {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(cryUrl)
        mediaPlayer.prepare()
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.reset()
            mediaPlayer.release()
        }
        mediaPlayer.setOnErrorListener { _, _, _ ->
            mediaPlayer.reset()
            mediaPlayer.release()
            true
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}