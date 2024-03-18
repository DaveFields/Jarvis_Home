package com.example.jarvishome.data.model


import com.example.jarvishome.data.model.api.ImagesResponseApi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchProductResponseApi(
    @Json(name = "code") val code: String,
    @Json(name = "product") val product : ProductResponseApi,
    //@Json(name = "status") val status : Boolean,
    //@Json(name = "status_verbose") val status_verbose : String
)
