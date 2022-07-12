package com.petrodcas.jokesapp.data.api

import com.petrodcas.jokesapp.R
import com.squareup.moshi.*
import com.petrodcas.jokesapp.data.api.JokesApi.Constants

data class JokeApiResponse(
    @Json(name = Constants.JsonTags.ERROR_TAG) var error: Boolean,
    @Json(name = Constants.AMOUNT_TAG) var amount: Int,
    @Json(name = Constants.JsonTags.JOKES_ARRAY_TAG) var jokes: List<JokeApiEntity>
)


sealed class JokeApiEntity {
    data class SingleJokeApiEntity(
        @Json(name = Constants.JsonTags.CATEGORY_TAG) val category: String,
        @Json(name = Constants.TYPE_TAG) val type: String,
        @Json(name = Constants.JsonTags.SINGLE_JOKE_CONTENT_TAG) val joke: String,
        @Json(name = Constants.JsonTags.BLACKLIST_TAG) val flags: FlagsApiEntity,
        @Json(name = Constants.JsonTags.ID_TAG) val id: Int,
        @Json(name = Constants.JsonTags.IS_SAFE_TAG) val safe: Boolean,
        @Json(name = Constants.JsonTags.LANGUAGE_TAG) val lang: String
    ) : JokeApiEntity()

    data class CompoundJokeApiEntity(
        @Json(name = Constants.JsonTags.CATEGORY_TAG) val category: String,
        @Json(name = Constants.TYPE_TAG) val type: String,
        @Json(name = Constants.JsonTags.COMPOUND_JOKE_FIRST_CONTENT_TAG) val firstPart: String,
        @Json(name = Constants.JsonTags.COMPOUND_JOKE_SECOND_CONTENT_TAG) val secondPart: String,
        @Json(name = Constants.JsonTags.BLACKLIST_TAG) val flags: FlagsApiEntity,
        @Json(name = Constants.JsonTags.ID_TAG) val id: Int,
        @Json(name = Constants.JsonTags.IS_SAFE_TAG) val safe: Boolean,
        @Json(name = Constants.JsonTags.LANGUAGE_TAG) val lang: String
    ) : JokeApiEntity()

    object Unknown : JokeApiEntity()
}

data class FlagsApiEntity(
    @Json(name = Constants.BlacklistFlags.NSFW) var nsfw: Boolean,
    @Json(name = Constants.BlacklistFlags.RELIGIOUS) var religious: Boolean,
    @Json(name = Constants.BlacklistFlags.POLITICAL) var political: Boolean,
    @Json(name = Constants.BlacklistFlags.RACIST) var racist: Boolean,
    @Json(name = Constants.BlacklistFlags.SEXIST) var sexist: Boolean,
    @Json(name = Constants.BlacklistFlags.EXPLICIT) var explicit: Boolean
)
