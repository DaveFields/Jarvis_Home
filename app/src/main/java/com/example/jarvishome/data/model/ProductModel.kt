package com.example.jarvishome.data.model

import com.example.jarvishome.data.model.api.ResponseFullProductApiDTO
import com.example.jarvishome.data.model.api.ResponseProductDTO

class ProductModel (code:String, product:ResponseProductDTO,  status:Boolean, status_verbose:String) {

    fun toProductModel(convert: ResponseFullProductApiDTO) =
        ProductModel(
            code = convert.code,
            product = convert.product,
            status = convert.status,
            status_verbose= convert.status_verbose
        )

}