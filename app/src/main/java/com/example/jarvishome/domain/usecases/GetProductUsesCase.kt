package com.example.jarvishome.domain.usecases

import com.example.jarvishome.data.repository.OpenFoodFactsRepositoryImpl
import com.example.jarvishome.domain.base.Resource
import com.example.jarvishome.domain.model.Product
import javax.inject.Inject

class GetProductUsesCase @Inject constructor(
    private val repository : OpenFoodFactsRepositoryImpl
) {
    suspend operator fun invoke(productId : String): Resource<Product> {
        return when (val response = repository.getProduct(productId)) {
            is Resource.Success -> {
                //userLocalDataSource.saveUser(response.value)
                response
            }
            is Resource.Failure -> response
        }
    }
}