package com.example.jarvishome.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NutrimentsReponseApi(
    @Json(name = "carbohydrates") val carbohydrates: Double,
    @Json(name = "carbohydrates_unit") val carbohydrates_unit: String,
    @Json(name = "carbohydrates_100g") val carbohydrates_100g: Double,
    @Json(name = "energy") val energy: Double,
    @Json(name = "energy_unit") val energy_unit: String,
    @Json(name = "energy-kcal") val energy_kcal: Double,
    @Json(name = "fat") val fat: Double,
    @Json(name = "fat_100g") val fat_100g: Double,
    @Json(name = "fat_unit") val fat_unit: String,
    @Json(name = "proteins") val proteins: Double,
    @Json(name = "proteins_100g") val proteins_100g: Double,
    @Json(name = "proteins_unit") val proteins_unit: String
    //TODO: More variables
)