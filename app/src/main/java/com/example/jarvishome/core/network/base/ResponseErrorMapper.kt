package com.example.jarvishome.core.network.base

import com.google.gson.Gson
import retrofit2.HttpException

class ResponseErrorMapper {

    companion object {
        fun map (throwable: HttpException): ErrorResponse? {
            return try {
                val error = throwable.response()?.errorBody()?.byteStream()?.bufferedReader().use { it?.readText() }
                val serviceError = Gson().fromJson(error, ErrorResponse::class.java)
                serviceError
            }catch (exception: Exception){ null}
        }
    }
}