package com.example.jarvishome.data.model.api

import com.example.jarvishome.data.model.ProductModel

data class ResponseFullProductApiDTO(
    val code : String,
    val product : ResponseProductDTO,
    val status : Boolean,
    val status_verbose : String
)
fun ResponseFullProductApiDTO.toEntity() = ProductModel(
    code,
    product,
    status,
    status_verbose
)