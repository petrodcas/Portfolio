package com.petrodcas.jokesapp.domain.usecases

import com.petrodcas.jokesapp.domain.entities.filters.Filter
import com.petrodcas.jokesapp.domain.repositories.JokesRepository
import javax.inject.Inject

class GetFilteredJokesUseCase @Inject constructor(private val repository: JokesRepository) {
    suspend operator fun invoke(filters: Filter) = repository.getJokes(filters)
}