package com.petrodcas.jokesapp.data.repositories.jokes

import com.petrodcas.jokesapp.data.api.JokeApiResponse
import com.petrodcas.jokesapp.domain.entities.filters.Filter
import retrofit2.Response

interface JokesRemoteDataSource {

    suspend fun getJokes(): Response<JokeApiResponse>

    suspend fun getJokes(filters: Filter): Response<JokeApiResponse>
}