package com.petrodcas.jokesapp.domain.usecases

import com.petrodcas.jokesapp.domain.repositories.JokesRepository
import javax.inject.Inject

class GetJokesUseCase @Inject constructor(private val repository: JokesRepository) {
    suspend operator fun invoke() = repository.getJokes()
}