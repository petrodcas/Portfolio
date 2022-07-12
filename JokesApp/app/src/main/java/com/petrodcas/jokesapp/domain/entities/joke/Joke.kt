package com.petrodcas.jokesapp.domain.entities.joke

import com.petrodcas.jokesapp.domain.entities.filters.Filter


sealed class Joke(
    open val category: Filter.Category,
    open val flags: List<Filter.Flags>
) {
    data class Simple (
        val text: String,
        override val category: Filter.Category,
        override val flags: List<Filter.Flags>,
    ): Joke(category, flags)

    data class Compound (
        val firstPart: String,
        val secondPart: String,
        override val category: Filter.Category,
        override val flags: List<Filter.Flags>
    ): Joke(category, flags)
}