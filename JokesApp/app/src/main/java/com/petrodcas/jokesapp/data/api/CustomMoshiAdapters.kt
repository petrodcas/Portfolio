package com.petrodcas.jokesapp.data.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import okio.IOException

/**
 * Polymorphic Moshi adapter for [JokeApiEntity]
 */
val jokeApiEntityMoshiAdapter: PolymorphicJsonAdapterFactory<JokeApiEntity> =
    PolymorphicJsonAdapterFactory.of(JokeApiEntity::class.java, "type")
        .withSubtype(JokeApiEntity.SingleJokeApiEntity::class.java, "single")
        .withSubtype(JokeApiEntity.CompoundJokeApiEntity::class.java, "twopart")
        .withDefaultValue(JokeApiEntity.Unknown)


/**
 * There is some trouble with the API response: The JSON is not consistent between a "1 joke request"
 * and a "multiple jokes request". Its structure changes by omitting the "amount" tag as well as
 * treating the joke as a set of consecutive tags instead of an unique object.
 *
 * This situation leads to defining a custom moshi adapter just in case a "1 joke request" is
 * needed in the future.
 *
 */
class JokeApiResponseAdapter {

    private fun readError(reader: JsonReader): Boolean {
        if (reader.peek() == JsonReader.Token.NAME) {
            if (reader.nextName() == "error") {
                return reader.nextBoolean()
            } else {
                throw IOException()
            }
        }
        throw IOException("Next token is not a name")
    }

    private fun readAmount(reader: JsonReader): Int {
        if (reader.peek() == JsonReader.Token.NAME) {
            val notConsumingReader = reader.peekJson()
            return if (notConsumingReader.nextName() == "amount") {
                reader.skipName()
                reader.nextInt()
            }
            else {
                0
            }
        }
        throw IOException("Next token is not a name")
    }

    private fun readFlags(reader: JsonReader): FlagsApiEntity {
        val flags = FlagsApiEntity(
            nsfw = false,
            religious = false,
            political = false,
            racist = false,
            sexist = false,
            explicit = false
        )
        reader.beginObject()
        while (reader.hasNext()) {
            when(reader.nextName()) {
                "nsfw" -> flags.nsfw = reader.nextBoolean()
                "religious" -> flags.religious = reader.nextBoolean()
                "political" -> flags.political = reader.nextBoolean()
                "racist" -> flags.racist = reader.nextBoolean()
                "sexist" -> flags.sexist = reader.nextBoolean()
                "explicit" -> flags.explicit = reader.nextBoolean()
            }
        }
        reader.endObject()
        return flags
    }

    private fun readJoke(reader: JsonReader): JokeApiEntity? {
        lateinit var category: String
        lateinit var type: String
        lateinit var firstPart: String
        lateinit var secondPart: String
        lateinit var flags: FlagsApiEntity
        var id = 0
        var safe = false
        lateinit var lang: String

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "category" -> category = reader.nextString()
                "type" -> type = reader.nextString()
                "joke", "setup" -> firstPart = reader.nextString()
                "delivery" -> secondPart = reader.nextString()
                "flags" -> {
                    //process flags
                    flags = readFlags(reader)
                }
                "id" -> id = reader.nextInt()
                "safe" -> safe = reader.nextBoolean()
                "lang" -> lang = reader.nextString()
            }
        }

        return if (type == "single") {
            JokeApiEntity.SingleJokeApiEntity(
                category = category,
                type = type,
                flags = flags,
                joke = firstPart,
                id = id,
                safe = safe,
                lang = lang
            )
        }
        else {
            JokeApiEntity.CompoundJokeApiEntity(
                category = category,
                type = type,
                flags = flags,
                firstPart = firstPart,
                secondPart = secondPart,
                id = id,
                safe = safe,
                lang = lang
            )
        }
    }

    @FromJson
    fun fromJson(reader: JsonReader, delegate: JsonAdapter<JokeApiEntity>): JokeApiResponse? {

        var error: Boolean = false
        var amount: Int = 1
        var jokes: MutableList<JokeApiEntity> = mutableListOf()

        if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
            reader.beginObject()
        }
        else {
            return null
        }

        error = readError(reader)
        amount = readAmount(reader)

        if (amount == 0) {
            //It's just one joke
            //delegate.fromJson(reader)?.let {jokes.add(it)}
            readJoke(reader)?.let { jokes.add(it) }
            amount = jokes.size
        }
        else {
            //several jokes
            reader.skipName()
            reader.beginArray()
            while (reader.hasNext()) {
                delegate.fromJson(reader)?.let { jokes.add(it) }
            }
            reader.endArray()
        }

        if (reader.peek() == JsonReader.Token.END_OBJECT) {
            reader.endObject()
        }
        else {
            return null
        }

        return JokeApiResponse(error, amount, jokes)
    }
}