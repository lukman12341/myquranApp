package com.example.myq.data.model

data class Ayah(
    val number: Int,
    val text: String,
    val audio: String? = null // URL for ayah audio
)

data class SurahDetail(
    val name: String,
    val number: Int,
    val ayahs: List<Ayah>,
    val audio: String? = null // URL for full surah audio
)

data class AyahResponse(
    val data: SurahDetail
)