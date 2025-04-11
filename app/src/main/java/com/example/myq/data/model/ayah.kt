package com.example.myq.data.model

data class AyahResponse(
    val data: SurahDetail
)

data class SurahDetail(
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val numberOfAyahs: Int,
    val revelationType: String,
    val ayahs: List<Ayah>
)

data class Ayah(
    val number: Int,
    val text: String,
    val numberInSurah: Int,
    val juz: Int,
    val audio: String

)