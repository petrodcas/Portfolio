package com.petrodcas.jokesapp.data.mappers

import com.petrodcas.jokesapp.data.api.FlagsApiEntity
import com.petrodcas.jokesapp.data.api.JokeApiEntity
import com.petrodcas.jokesapp.domain.entities.filters.Filter
import com.petrodcas.jokesapp.domain.entities.joke.Joke
import kotlin.reflect.full.memberProperties

object JokesMapper {

    fun jokeApiEntityToDomain(jokeApiEntity: JokeApiEntity): Joke? = when (jokeApiEntity) {
        is JokeApiEntity.CompoundJokeApiEntity -> Joke.Compound(
            firstPart = jokeApiEntity.firstPart,
            secondPart = jokeApiEntity.secondPart,
            category = Filter.Category.valueOf(jokeApiEntity.category.uppercase()),
            flags = flagsApiEntityToListOfDomainFlags(jokeApiEntity.flags)
        )
        is JokeApiEntity.SingleJokeApiEntity -> Joke.Simple(
            text = jokeApiEntity.joke,
            category = Filter.Category.valueOf(jokeApiEntity.category.uppercase()),
            flags = flagsApiEntityToListOfDomainFlags(jokeApiEntity.flags)
        )
        JokeApiEntity.Unknown -> null
    }

    private fun flagsApiEntityToListOfDomainFlags(flagsApiEntity: FlagsApiEntity): List<Filter.Flags> =
        FlagsApiEntity::class.memberProperties.filter { p -> (p.get(flagsApiEntity) as Boolean) }
            .map { p -> Filter.Flags.valueOf(p.name.uppercase()) }

}