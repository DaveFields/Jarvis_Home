package com.example.jarvishome.core.network.remote.services

import com.example.jarvishome.data.model.SearchProductResponseApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsService {
    @GET("product/{productId}")
    suspend fun getProductBarcode(@Path("productId") productId:String, @Query("fields") body: String
    ): SearchProductResponseApi
}