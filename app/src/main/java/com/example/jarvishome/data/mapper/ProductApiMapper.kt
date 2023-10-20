package com.example.jarvishome.data.mapper

import com.example.jarvishome.data.base.Mapper
import com.example.jarvishome.data.model.ProductApi
import com.example.jarvishome.domain.model.Product

class ProductApiMapper : Mapper<ProductApi, Product> {
        override fun map(model: ProductApi): Product {

            return Product(
                model.code?: 0
            )
        }
}