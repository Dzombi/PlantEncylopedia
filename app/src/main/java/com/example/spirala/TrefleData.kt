package com.example.spirala

import com.google.gson.annotations.SerializedName

data class TrefleData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("common_name")
    val commonName: String,

    @SerializedName("scientific_name")
    val scientificName: String,

    @SerializedName("family")
    val family: String,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("links")
    val links: Links?
)


data class Links(
    @SerializedName("self")
    val self: String?,

    @SerializedName("first")
    val first: String?,

    @SerializedName("last")
    val last: String?,

    @SerializedName("next")
    val next: String?
)
