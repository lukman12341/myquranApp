package com.example.myq.data.model

data class JuzResponse(
    val data: DataJuz
)

data class DataJuz(
    val nomor: Int,
    val ayah: List<Ayah>, // Perbaiki dengan mendefinisikan class Ayat
    val surah: Map<String, InfoSurah>
)

data class Ayat(
    val nomor: Int,    // Nomor ayat dalam surah
    val text: String,  // Teks Arab
    val terjemahan: String? // Terjemahan (opsional, bisa null)
)

data class InfoSurah(
    val nomor: Int,
    val nama: String,
    val namaInggris: String,
    val terjemahanNamaInggris: String,
    val jenisWahyu: String
)
