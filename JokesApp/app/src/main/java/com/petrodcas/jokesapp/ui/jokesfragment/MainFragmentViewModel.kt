package com.petrodcas.jokesapp.ui.jokesfragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petrodcas.jokesapp.domain.common.DomainResult
import com.petrodcas.jokesapp.domain.entities.joke.Joke
import com.petrodcas.jokesapp.domain.usecases.GetFilteredJokesUseCase
import com.petrodcas.jokesapp.domain.usecases.GetJokesUseCase
import com.petrodcas.jokesapp.domain.usecases.GetNextJokeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val getFilteredJokesUseCase: GetFilteredJokesUseCase,
    private val getJokesUseCase: GetJokesUseCase,
    private val getNextJokeUseCase: GetNextJokeUseCase
) : ViewModel() {

    private var isInitialized: Boolean = false

    private val _jokeText = MutableLiveData<String>()
    val jokeText: LiveData<String> get() = _jokeText

    private val _errorEvent = MutableLiveData<Int>()
    val errorEvent: LiveData<Int> get() = _errorEvent

    private val _loadingEvent = MutableLiveData<Boolean>()
    val loadingEvent: LiveData<Boolean> get() = _loadingEvent

    fun getNextJoke() {
        Log.d(":::NEXT_JOKE", "Im called")
        viewModelScope.launch {
            _loadingEvent.postValue(true)
            when (val result = getNextJokeUseCase()) {
                is DomainResult.Error -> {
                    _errorEvent.postValue(result.messageId)
                }
                is DomainResult.Success -> {
                    _jokeText.postValue(
                        when (result.data) {
                            is Joke.Simple -> result.data.text
                            is Joke.Compound -> result.data.let { joke ->
                                "Q:\t\t${joke.firstPart}\nA:\t\t${joke.secondPart}"
                            }
                        }
                    )
                }
            }
            _loadingEvent.postValue(false)
        }
    }

    fun initializeJoke() {
        if (!isInitialized) {
            isInitialized = true
            getNextJoke()
        }
    }
}