package com.example.cartes_animees.model

data class Series(
    val id: Int,
    val libelle_serie: String,
    val theme: String,
    val description: String,
    val animations: List<Animation>
)
