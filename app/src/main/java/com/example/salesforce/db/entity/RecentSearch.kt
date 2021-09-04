package com.example.salesforce.db.entity

data class RecentSearch(
    val Response: String? = null,
    val Search: List<Search>? = null,
    val totalResults: String? = null
)