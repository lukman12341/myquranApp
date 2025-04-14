package com.example.myq.data.model

data class SurahResponse(
    val data: List<Surah>,
    val code: Int,
    val status: String
)

data class Surah(
    val number: Int,
    val surah: Surah,
    val name: String,
    val juz: Int,
    val englishName: String,
    val englishNameTranslation: String,
    val numberOfAyahs: Int,
    val revelationType: String

)
