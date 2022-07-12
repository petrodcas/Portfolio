package com.petrodcas.jokesapp.data.repositories.jokes

import com.petrodcas.jokesapp.domain.entities.filters.Filter
import com.petrodcas.jokesapp.domain.entities.joke.Joke
import javax.inject.Inject

class JokesCachedDataSourceImpl @Inject constructor() : JokesCachedDataSource {

    private val cachedJokes: MutableList<Joke> = mutableListOf()

    override fun addJokes(jokes: List<Joke>, allowDuplicates: Boolean): Int {
        if (jokes.isEmpty()) {
            return 0
        }
        val jokesToBeAdded = if (allowDuplicates) jokes else getNoDuplicates(jokes)
        cachedJokes.addAll(jokesToBeAdded)
        return jokesToBeAdded.size
    }

    override fun getJokes(): List<Joke> = cachedJokes.toList()  //returns a copy

    override fun getJokes(filter: Filter): List<Joke> = cachedJokes.filter { joke ->
        filter.categories.contains(joke.category)
                && !joke.flags.any { f -> filter.blackList.contains(f) }
    }

    override fun clearJokes() {
        cachedJokes.clear()
    }

    override fun isEmpty(): Boolean = cachedJokes.isEmpty()

    private fun getNoDuplicates(jokes: List<Joke>): List<Joke> =
        jokes.filter { j -> !cachedJokes.contains(j) }

}