package com.example.spirala

import com.google.gson.annotations.SerializedName

data class TrefleBiljke(
    @SerializedName("data")
    val data: List<TrefleData>?,

    @SerializedName("links")
    val links: Links?,

    @SerializedName("meta")
    val meta: Meta
)

data class Meta(
    @SerializedName("total")
    val total: Int
)