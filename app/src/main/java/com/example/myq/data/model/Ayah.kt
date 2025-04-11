package com.example.myq.data.model

data class AyahResponse(
    val data: SurahDetail
)

data class SurahDetail(
    val name: String,
    val number: Int,
    val ayahs: List<Ayah>
)

data class Ayah(
    val number: Int,
    val text: String
)
