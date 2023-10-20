package com.example.jarvishome.domain.base

sealed class FailureError {
    object Unathorized : FailureError()
    object Network : FailureError()
    object Timeout : FailureError()
    object NotFound : FailureError()
    object ServerError : FailureError()

    object Generic : FailureError()
}