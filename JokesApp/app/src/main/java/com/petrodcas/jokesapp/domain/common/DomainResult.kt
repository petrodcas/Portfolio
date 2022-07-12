package com.petrodcas.jokesapp.domain.common

import androidx.annotation.StringRes

sealed class DomainResult<T> {
    data class Success<T>(val data: T): DomainResult<T>()
    data class Error<T>(@StringRes val messageId: Int): DomainResult<T>()
}