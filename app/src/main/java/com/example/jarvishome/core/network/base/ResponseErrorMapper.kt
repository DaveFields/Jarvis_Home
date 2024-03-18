package com.example.jarvishome.core.network.base

import com.squareup.moshi.Moshi
import retrofit2.HttpException

class ResponseErrorMapper {

    companion object {
        fun map (throwable: HttpException): ErrorResponse? {
            return try {
                val error = throwable.response()?.errorBody()?.byteStream()?.bufferedReader().use { it?.readText() }
                val moshi = Moshi.Builder()
                    .build()

                // Deserializar usando Moshi
                val adapter = moshi.adapter(ErrorResponse::class.java)
                adapter.fromJson(error)
                /*val serviceError = Gson().fromJson(error, ErrorResponse::class.java)
                serviceError*/
            }catch (exception: Exception){ null}
        }
    }
}