package com.example.jarvishome.data.model

import com.example.jarvishome.data.model.api.ResponseProductDTO
import com.google.gson.annotations.SerializedName

data class ProductApi(
    @SerializedName("code") val code: Long?
)
