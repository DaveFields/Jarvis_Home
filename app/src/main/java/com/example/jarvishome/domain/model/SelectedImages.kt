package com.example.jarvishome.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SelectedImages(
    val front: ImageType,
    val nutrition: ImageType
) {
    constructor() : this(
        ImageType(ImageSize(""),ImageSize(""),ImageSize("")),
        ImageType(ImageSize(""),ImageSize(""),ImageSize(""))
    )
}
@JsonClass(generateAdapter = true)
data class ImageType(
    val display: ImageSize,
    val small: ImageSize,
    val thumb: ImageSize
)
@JsonClass(generateAdapter = true)

data class ImageSize(
    val es: String
)
