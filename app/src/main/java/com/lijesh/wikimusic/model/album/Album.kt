package com.lijesh.wikimusic.model.album

data class Album(
    val artist: String,
    val image: List<Image>,
    val mbid: String,
    val name: String,
    val streamable: String,
    val url: String
)