package com.example.jarvishome.data.repository.interfaces

import com.example.jarvishome.domain.base.Resource
import com.example.jarvishome.domain.model.Product

interface OpenFoodFactsRepository {

    suspend fun getProduct(productId: String): Resource<Product>
}