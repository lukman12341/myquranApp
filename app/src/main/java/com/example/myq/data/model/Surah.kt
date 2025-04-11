package com.example.myq.data.model

data class SurahResponse(
    val data: List<Surah>
)

data class Surah(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val numberOfAyahs: Int,
    val revelationType: String
)