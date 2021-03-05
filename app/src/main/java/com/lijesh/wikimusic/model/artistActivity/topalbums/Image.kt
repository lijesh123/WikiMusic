package com.lijesh.wikimusic.model.artistActivity.topalbums

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("#text")
    val text: String,
    val size: String
)