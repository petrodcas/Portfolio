package com.petrodcas.jokesapp.data.repositories.jokes

import com.petrodcas.jokesapp.R
import com.petrodcas.jokesapp.data.api.JokeApiResponse
import com.petrodcas.jokesapp.data.api.JokesApi
import com.petrodcas.jokesapp.data.api.toQueryString
import com.petrodcas.jokesapp.data.mappers.JokesMapper
import com.petrodcas.jokesapp.data.repositories.handleServiceCall
import com.petrodcas.jokesapp.domain.common.DomainResult
import com.petrodcas.jokesapp.domain.entities.filters.Filter
import com.petrodcas.jokesapp.domain.entities.joke.Joke
import com.petrodcas.jokesapp.domain.repositories.JokesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class JokesRepositoryImpl @Inject constructor(
    private val cachedJokes: JokesCachedDataSource,
    private val jokesService: JokesApi
) : JokesRepository {

    private var lastCallWasAFilter = false
    private var lastFilters: Filter? = null
    private var lastIndex: Int = -1

    override suspend fun getJokes(): DomainResult<List<Joke>> = handleServiceCall {
        val response = jokesService.getJokes()
        val domainJokes = handleResponse(response)
        if (lastCallWasAFilter) {
            cachedJokes.clearJokes()
            lastIndex = 0
            lastCallWasAFilter = false
        }
        cachedJokes.addJokes(domainJokes, allowDuplicates = false)
        domainJokes
    }

    override suspend fun getJokes(filters: Filter): DomainResult<List<Joke>> = handleServiceCall {
        val response = jokesService.getFilteredJokes(
            jokeCategory = filters.categories.toQueryString(),
            banFlags = filters.blackList.toQueryString()
        )
        val domainJokes = handleResponse(response)
        if (!lastCallWasAFilter) {
            cachedJokes.clearJokes()
            lastIndex = 0
            lastCallWasAFilter = true
        }
        else if (lastFilters != filters) {
            lastFilters = filters
            cachedJokes.clearJokes()
            lastIndex = 0
        }
        cachedJokes.addJokes(domainJokes, allowDuplicates = false)
        cachedJokes.getJokes(filters)
    }

    override suspend fun getNextJoke(): DomainResult<Joke> = withContext(Dispatchers.IO) {
        if (!cachedJokes.isEmpty()) {
            var jokes = cachedJokes.getJokes()
            if (lastIndex >= jokes.size) {
                val newJokesList = refillJokes()
                if (jokes.size >= newJokesList.size) {
                    jokes = jokes.shuffled()
                    cachedJokes.clearJokes()
                    cachedJokes.addJokes(jokes)
                    lastIndex = 0
                }
                else {
                    jokes = newJokesList
                }
            }
            return@withContext DomainResult.Success(jokes[lastIndex++])
        }
        else {
            lastIndex = 0
            val jokes = refillJokes()
            return@withContext if (jokes.isNotEmpty()) {
                DomainResult.Success(jokes[lastIndex++])
            }
            else {
                DomainResult.Error(R.string.no_internet_error)
            }
        }
    }

    private suspend fun refillJokes(): List<Joke> {
        if (lastCallWasAFilter && lastFilters != null) {
            getJokes(lastFilters!!)
        }
        else {
            getJokes()
        }
        return cachedJokes.getJokes()
    }

    private fun handleResponse(response: Response<JokeApiResponse>): List<Joke> {
        if (!response.isSuccessful || response.body() == null || response.body()!!.error) {
            throw Exception()
        }
        val jokesDTO = response.body()!!.jokes
        return jokesDTO.mapNotNull { jokeDTO -> JokesMapper.jokeApiEntityToDomain(jokeDTO) }
    }

}