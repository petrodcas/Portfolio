package com.petrodcas.jokesapp.data.repositories

import com.petrodcas.jokesapp.R
import com.petrodcas.jokesapp.domain.common.DomainResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

suspend fun <T> handleServiceCall(call: suspend () -> T): DomainResult<T>{
    return withContext(Dispatchers.IO) {
        try {
            DomainResult.Success(call())
        }
        catch (ex: UnknownHostException) {
            DomainResult.Error(R.string.no_internet_error)
        }
        catch (ex: Exception) {
            DomainResult.Error(R.string.unhandled_exception_error)
        }
    }
}