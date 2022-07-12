package com.petrodcas.jokesapp.domain.entities.filters

import androidx.annotation.StringRes
import com.petrodcas.jokesapp.R

data class Filter(
    val categories: List<Category>,
    val blackList: List<Flags>
) {
    enum class Category(@StringRes val textId: Int) {
        DARK(R.string.category_dark),
        PROGRAMMING(R.string.category_programming),
        MISC(R.string.category_misc),
        PUN(R.string.category_pun),
        SPOOKY(R.string.category_spooky),
        CHRISTMAS(R.string.category_christmas)
    }

    enum class Flags(@StringRes val textId: Int){
        NSFW(R.string.flag_nsfw),
        RELIGIOUS(R.string.flag_religious),
        POLITICAL(R.string.flag_political),
        RACIST(R.string.flag_racist),
        SEXIST(R.string.flag_sexist),
        EXPLICIT(R.string.flag_explicit)
    }
}