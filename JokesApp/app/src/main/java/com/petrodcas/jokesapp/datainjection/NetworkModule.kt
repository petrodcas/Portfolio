package com.petrodcas.jokesapp.datainjection

import com.petrodcas.jokesapp.data.api.JokeApiResponseAdapter
import com.petrodcas.jokesapp.data.api.JokesApi
import com.petrodcas.jokesapp.data.api.jokeApiEntityMoshiAdapter
import com.petrodcas.jokesapp.data.repositories.jokes.*
import com.petrodcas.jokesapp.domain.repositories.JokesRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Singleton
    @Binds
    abstract fun provideJokesRemoteDataSource(impl: JokesRemoteDataSourceImpl): JokesRemoteDataSource

    @Singleton
    @Binds
    abstract fun provideJokesCachedDataSource(impl: JokesCachedDataSourceImpl): JokesCachedDataSource

    @Singleton
    @Binds
    abstract fun provideJokesRepository(impl: JokesRepositoryImpl): JokesRepository

    companion object {
        @Singleton
        @Provides
        fun provideJokesApi(): JokesApi =
            Retrofit.Builder()
                .baseUrl(JokesApi.Constants.URL.JOKE_API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
                .build()
                .create(JokesApi::class.java)

        /**
         * Returns a [Moshi] instance.
         *
         * @returns A [Moshi] instance.
         */
        private fun getMoshi(): Moshi =
            Moshi.Builder()
                .add(jokeApiEntityMoshiAdapter)
                .add(JokeApiResponseAdapter())
                .addLast(KotlinJsonAdapterFactory())  // Needed to handle moshi auto-created adapters in Kotlin
                .build()
    }
}