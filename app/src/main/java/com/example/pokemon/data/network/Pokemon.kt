package com.example.pokemon.data.network

import androidx.compose.ui.text.font.FontWeight
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Pokemon(
    val name: String,
    val url: String
)
@Serializable
data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
)

@Serializable
data class Sprites(
    @SerialName("back_default")
    val backDefault: String?,

    @SerialName("front_default")
    val frontDefault: String?,

    @SerialName("front_shiny")
    val frontShiny: String?,

    @SerialName("other")
    val other: Other?
)

@Serializable
data class Other(
    @SerialName("showdown")
    val showdown: Showdown?
)

@Serializable
data class Showdown(
    @SerialName("back_default")
    val backDefault: String?,
    @SerialName("front_default")
    val frontDefault: String?
)



@Serializable
data class Type(
    val slot: Int,
    val type: TypeDetail
)

@Serializable
data class TypeDetail(
    val name: String,
    val url: String
)

@Serializable
data class PokemonDetails(
    val id: Int,
    val sprites: Sprites,
    val types: List<Type>,
    val stats: List<Stat>,
    val abilities: List<Ability>,
    val height: Int,
    val weight: Int,
    val species: Species ,
    val cries: Cries
)

@Serializable
data class Cries(
    val latest: String?,
    val legacy: String?
)

@Serializable
data class Species(
    val name: String,
    val url: String
)

@Serializable
data class Ability(
    @SerialName("ability")
    val ability: AbilityDetail,

    @SerialName("is_hidden")
    val isHidden: Boolean,

    val slot: Int
)

@Serializable
data class AbilityDetail(
    val name: String,
    val url: String
)
@Serializable
data class Stat(
    @SerialName("base_stat")
    val baseStat: Int,

    val effort: Int,

    @SerialName("stat")
    val stat: StatDetail
)

@Serializable
data class StatDetail(
    val name: String,
    val url: String
)

@Serializable
data class TypeResponse(
    val results: List<GeneralType>
)
@Serializable
data class GeneralType(
    val name: String,
    val url: String
)
@Serializable
data class PokemonResponseType(
    val pokemon: List<PokemonSlot>,
    val sprites: Sprite
)
@Serializable
data class PokemonSlot(
    val pokemon: PokemonDetailsType,
    val slot: Int
)
@Serializable
data class PokemonDetailsType(
    val name: String,
    val url: String
)

@Serializable
data class Sprite(
    @SerialName("generation-viii")
    val generation: Generation
)

@Serializable
data class Generation(
    @SerialName("sword-shield")
    val EspadaEscudo: Espada
)
@Serializable
data class Espada(
    @SerialName("name_icon")
    val icon: String?
)

@Serializable
data class PokemonFlavorText(
    @SerialName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>
)

@Serializable
data class FlavorTextEntry(
    @SerialName("flavor_text")
    val flavorText: String
)


