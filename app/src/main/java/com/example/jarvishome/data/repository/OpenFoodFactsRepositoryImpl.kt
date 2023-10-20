package com.example.jarvishome.data.repository

import com.example.jarvishome.core.network.remote.services.OpenFoodFactsService
import com.example.jarvishome.data.datasource.remote.ProductRemoteDataSource
import com.example.jarvishome.data.model.ProductApi
import com.example.jarvishome.data.model.ProductModel
import com.example.jarvishome.data.repository.interfaces.OpenFoodFactsRepository
import com.example.jarvishome.domain.base.Resource
import com.example.jarvishome.domain.model.Product
import javax.inject.Inject

class OpenFoodFactsRepositoryImpl @Inject constructor(private val remoteDataSource: ProductRemoteDataSource): OpenFoodFactsRepository{

    override suspend fun getProduct(productId: String): Resource<Product> {
        return remoteDataSource.getProduct(productId)
    }
}