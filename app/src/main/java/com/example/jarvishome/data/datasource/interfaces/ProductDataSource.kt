package com.example.jarvishome.data.datasource.interfaces

import com.example.jarvishome.data.base.BaseDataSource
import com.example.jarvishome.data.model.ProductModel
import com.example.jarvishome.domain.base.Resource
import com.example.jarvishome.domain.model.Product

interface ProductDataSource {
    suspend fun getProduct(productId: String): Resource<Product>
}