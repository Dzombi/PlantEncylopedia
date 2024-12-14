package com.example.spirala

import com.google.gson.annotations.SerializedName

data class TrefleDetailsData(

    @SerializedName("id")
    val id: Int,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("common_name")
    val commonName: String,

    @SerializedName("scientific_name")
    val scientificName: String,

    @SerializedName("family")
    val family: Family,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("main_species")
    val mainSpecies: MainSpecies?,

    @SerializedName("links")
    val links: Links?
)

data class Family(
    @SerializedName("name")
    val name: String
)

data class MainSpecies(
    @SerializedName("edible")
    val edible: Boolean?,

    @SerializedName("growth")
    val growth: Growth,

    @SerializedName("specifications")
    val specfications: Specifications
)

data class Specifications(

    @SerializedName("toxicity")
    val toxicity: String,
)

data class Growth(
    @SerializedName("light")
    val light: Int,

    @SerializedName("atmospheric_humidity")
    val atmosphericHumidity: Int,

    @SerializedName("soil_texture")
    val soilTexture: List<String>,
)
