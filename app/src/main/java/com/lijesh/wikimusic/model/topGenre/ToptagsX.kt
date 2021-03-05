package com.lijesh.wikimusic.model.topGenre

import com.google.gson.annotations.SerializedName

data class ToptagsX(
    @SerializedName("@attr")
        val attr: Attr,
    val tag: List<tag>
)
