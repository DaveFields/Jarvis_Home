package com.example.jarvishome.data.model.api

data class IngredientsDTO(
    val id : String,
    val percent_estimate: Float,
    val percent_max: Float,
    val percent_min: Int,
    val text: String,
    val vegan: Boolean,
    val vegetarian: Boolean
)
