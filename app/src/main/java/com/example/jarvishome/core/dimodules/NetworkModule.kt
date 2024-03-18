package com.example.jarvishome.core.dimodules

import com.example.jarvishome.core.network.remote.services.OpenFoodFactsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

// @Module informs Dagger that this class is a Dagger Module
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitService():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/api/v2/")//.net (PRE)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(getClient())
            .build()
    }

    private fun getClient(): OkHttpClient
    {
        val interceptor = HttpLoggingInterceptor().apply {
            this .level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder().apply {
            this .addInterceptor(interceptor)
        }.build()
    }

    @Singleton
    @Provides
    fun provideOpenFoodFactsClient(retrofit: Retrofit): OpenFoodFactsService {
        return retrofit.create(OpenFoodFactsService::class.java)
    }
}