package com.hasantalhakazmacan.diary_calisma

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val date: String,
    val isFavorite: Boolean
)
