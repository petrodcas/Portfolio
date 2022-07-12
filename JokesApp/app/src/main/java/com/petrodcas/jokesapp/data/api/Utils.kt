package com.petrodcas.jokesapp.data.api

fun <T> List<T>.toQueryString(): String = this.joinToString(separator = ",", transform = {it.toString()})
