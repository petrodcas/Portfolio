package com.petrodcas.jokesapp.domain.repositories

import com.petrodcas.jokesapp.domain.common.DomainResult
import com.petrodcas.jokesapp.domain.entities.filters.Filter
import com.petrodcas.jokesapp.domain.entities.joke.Joke

interface JokesRepository {

    suspend fun getJokes(): DomainResult<List<Joke>>

    suspend fun getJokes(filters: Filter): DomainResult<List<Joke>>

    suspend fun getNextJoke(): DomainResult<Joke>
}