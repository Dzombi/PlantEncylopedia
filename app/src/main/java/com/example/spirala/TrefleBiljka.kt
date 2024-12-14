package com.example.spirala

import com.google.gson.annotations.SerializedName

data class TrefleBiljka(
    @SerializedName("data")
    val data: TrefleDetailsData,

    @SerializedName("links")
    val links: Links?
)