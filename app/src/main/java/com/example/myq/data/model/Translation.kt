package com.example.myq.data.model

data class TranslationResponse(
    val data: SurahTranslation
)

data class SurahTranslation(
    val ayahs: List<AyahTranslation>
)

data class AyahTranslation(
    val number: Int,
    val text: String
)

