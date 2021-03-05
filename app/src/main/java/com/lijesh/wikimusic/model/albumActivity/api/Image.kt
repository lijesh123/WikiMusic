package com.lijesh.wikimusic.model.albumActivity.api

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("#text")
    val text: String,
    val size: String
)