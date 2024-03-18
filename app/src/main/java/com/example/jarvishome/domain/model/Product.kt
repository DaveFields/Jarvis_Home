package com.example.jarvishome.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    val code: String,
    val ecoscore_score: Int,
    val nutriments: Nutriments,
    val images: SelectedImages
){
    constructor() : this(
        "",
        -1,
        Nutriments(),
        SelectedImages()
    )
}
