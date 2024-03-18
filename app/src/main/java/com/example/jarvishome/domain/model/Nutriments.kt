package com.example.jarvishome.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Nutriments (
    val carbohydrates: Double,
    val carbohydrates_unit: String,
    val carbohydrates_100g: Double,
    val energy: Double,
    val energy_unit: String,
    val energy_kcal: Double,
    val fat: Double,
    val fat_unit: String,
    val fat_100g: Double,
    val proteins: Double,
    val proteins_unit: String,
    val proteins_100g: Double,

) {
    // Constructor adicional con valores predeterminados
    constructor() : this(
        -1.0, "",-1.0,
        -1.0,"",-1.0,
        -1.0,"", -1.0,
        -1.0, "", -1.0
    )
}
