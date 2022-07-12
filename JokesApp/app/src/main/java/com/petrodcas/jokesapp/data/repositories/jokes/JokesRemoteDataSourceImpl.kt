package com.petrodcas.jokesapp.data.repositories.jokes

import com.petrodcas.jokesapp.data.api.JokeApiResponse
import com.petrodcas.jokesapp.data.api.JokesApi
import com.petrodcas.jokesapp.domain.entities.filters.Filter
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class JokesRemoteDataSourceImpl @Inject constructor(private val service: JokesApi) :
    JokesRemoteDataSource {

    override suspend fun getJokes(): Response<JokeApiResponse> = service.getJokes()

    override suspend fun getJokes(filters: Filter): Response<JokeApiResponse> =
        service.getFilteredJokes()
}