package com.example.jarvishome.data.model.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesResponseApi(
    val front: ImageTypeDTO,
    val nutrition: ImageTypeDTO
)
@JsonClass(generateAdapter = true)
data class ImageTypeDTO(
    val display: ImageSizeDTO,
    val small: ImageSizeDTO,
    val thumb: ImageSizeDTO
)
@JsonClass(generateAdapter = true)

data class ImageSizeDTO(
    val es: String
)