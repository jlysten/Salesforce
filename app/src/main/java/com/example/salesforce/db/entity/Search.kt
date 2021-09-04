package com.example.salesforce.db.entity

data class Search(
    val Poster: String? = null,
    val Title: String? = null,
    val Type: String? = null,
    val Year: String? = null,
    val imdbID: String? = null,
    val inFavorites: Boolean? = null
)