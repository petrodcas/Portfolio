package com.petrodcas.jokesapp.data.repositories.jokes

import com.petrodcas.jokesapp.domain.entities.filters.Filter
import com.petrodcas.jokesapp.domain.entities.joke.Joke

interface JokesCachedDataSource {

    /**
     * Adds jokes to cache.
     *
     * @param jokes A [List] of [Joke] to be added.
     * @param allowDuplicates if false, elements from [jokes] will be filtered so
     * cache only has one instance of them. If true, then every element from [jokes] will be
     * added whether it's already existent on cache or not.
     *
     * @return The number of elements from [jokes] added to cache.
     */
    fun addJokes(jokes: List<Joke>, allowDuplicates: Boolean = false): Int

    /**
     * Returns a copy of the cached jokes.
     *
     * @return A copy of the stored [Joke].
     */
    fun getJokes(): List<Joke>

    /**
     * Returns a filtered copy of the cached jokes.
     *
     * @return A filtered copy of the cached Jokes.
     */
    fun getJokes(filter: Filter): List<Joke>

    /**
     * Clears the whole cache.
     */
    fun clearJokes()

    /**
     * Checks whether the cache is empty or not.
     *
     * @return true if it's empty or false otherwise.
     */
    fun isEmpty(): Boolean
}