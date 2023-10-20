package com.example.jarvishome.core.network.base

import com.example.jarvishome.data.base.Mapper
import com.example.jarvishome.domain.base.FailureError
import com.example.jarvishome.domain.base.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

interface SafeApiCall {

    suspend fun <A,D> safeApiCall(mapper: Mapper<A, D>, apicall:suspend() -> A) : Resource<D> {
      return withContext(Dispatchers.IO){
          try {
              val result = apicall.invoke()
              val model = mapper.map(result)
              Resource.Success(model)
          }
          catch (throwable:Throwable){
              handle(throwable)
          }
      }
    }

    private fun handle (throwable: Throwable) : Resource.Failure{
        return when (throwable) {
            is HttpException -> {
                val errorResponse = ResponseErrorMapper.map(throwable)
                handleExcepion(throwable.code(), errorResponse?.error?.message)
            }
            is UnknownHostException, is IOException -> Resource.Failure(FailureError.Network)
            else ->Resource.Failure(FailureError.Generic)
        }
    }

    private fun handleExcepion(code : Int ,message : String?="" ):Resource.Failure {
        return when(code){
            HttpCodes.UNAUTHORIZED -> Resource.Failure(FailureError.Unathorized)
            HttpCodes.NOT_FOUND -> Resource.Failure(FailureError.NotFound)
            HttpCodes.SERVER_ERROR -> Resource.Failure(FailureError.ServerError)

            else->{
                Resource.Failure(FailureError.Generic, message)
            }
        }
    }
}