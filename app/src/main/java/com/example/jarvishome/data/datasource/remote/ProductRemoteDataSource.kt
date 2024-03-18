package com.example.jarvishome.data.datasource.remote

import android.util.Log
import com.example.jarvishome.core.network.remote.services.OpenFoodFactsService
import com.example.jarvishome.data.base.BaseDataSource
import com.example.jarvishome.data.datasource.interfaces.ProductDataSource
import com.example.jarvishome.data.mapper.ProductApiMapper
import com.example.jarvishome.domain.base.Resource
import com.example.jarvishome.domain.model.Product
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val productService: OpenFoodFactsService
): BaseDataSource(), ProductDataSource
{

    //TODO: Cambiar esto a modelo de datos de alguna forma, investigar
    private var body = "nutriments,nutriscore_data,images,selected_images,ecoscore_score,ingredients,ingredients_analysis_tags,ingredients_text,nutrient_levels,traces_hierarchy"

    override suspend fun getProduct(productId: String): Resource<Product> = safeApiCall(
        ProductApiMapper())
    { productService.getProductBarcode(productId, body) }

}