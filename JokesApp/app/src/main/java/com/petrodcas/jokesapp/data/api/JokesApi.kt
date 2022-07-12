package com.petrodcas.jokesapp.data.api


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import androidx.annotation.IntRange
import com.petrodcas.jokesapp.domain.entities.filters.Filter

interface JokesApi {

    /** Contains JokesApi common constants */
    object Constants {

        const val TYPE_TAG: String = "type"
        const val TYPE_SINGLE_VALUE: String = "single"
        const val TYPE_COMPOUND_VALUE: String = "twopart"
        const val AMOUNT_TAG: String = "amount"
        const val AMOUNT_MIN_VALUE: Int = 1
        const val AMOUNT_MAX_VALUE: Int = 10

        // Anotations only can have const values, so domain.entities.filter.Filter.Flags can't
        // be used due to being an enum.
        object BlacklistFlags {
            const val NSFW = "nsfw"
            const val RELIGIOUS = "religious"
            const val POLITICAL = "political"
            const val RACIST = "racist"
            const val SEXIST = "sexist"
            const val EXPLICIT = "explicit"
        }

        /** Contains URL specific constants */
        object URL {
            const val JOKE_API_BASE_URL: String = "https://v2.jokeapi.dev/joke/"
            const val CATEGORY_ANY_VALUE: String = "Any"
            const val BLACKLIST_TAG: String = "blacklistFlags"

        }

        /**
         * Contains Json specific constants
         */
        object JsonTags {
            const val ERROR_TAG: String = "error"
            const val JOKES_ARRAY_TAG: String = "jokes"
            const val CATEGORY_TAG: String = "category"
            const val BLACKLIST_TAG: String = "flags"
            const val SINGLE_JOKE_CONTENT_TAG = "joke"
            const val COMPOUND_JOKE_FIRST_CONTENT_TAG = "setup"
            const val COMPOUND_JOKE_SECOND_CONTENT_TAG = "delivery"
            const val ID_TAG = "id"
            const val IS_SAFE_TAG = "safe"
            const val LANGUAGE_TAG = "lang"
        }
    }

    @GET("${Constants.URL.CATEGORY_ANY_VALUE}?${Constants.AMOUNT_TAG}=${Constants.AMOUNT_MAX_VALUE}")
    suspend fun getJokes(): Response<JokeApiResponse>

    @GET("{category}")
    suspend fun getFilteredJokes(
        @Path("category") jokeCategory: String = Constants.URL.CATEGORY_ANY_VALUE,
        @Query(Constants.TYPE_TAG) type: String? = null,
        @Query(Constants.URL.BLACKLIST_TAG) banFlags: String? = null,
        @Query(Constants.AMOUNT_TAG) @IntRange(from= Constants.AMOUNT_MIN_VALUE.toLong(), to= Constants.AMOUNT_MAX_VALUE.toLong()) quantity: Int = Constants.AMOUNT_MAX_VALUE
    ): Response<JokeApiResponse>

}