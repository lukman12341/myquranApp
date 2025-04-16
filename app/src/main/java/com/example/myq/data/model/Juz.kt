package com.example.myq.data.model

data class JuzResponse(
    val data: DataJuz
)

data class DataJuz(
    val number: Int, // Ubah dari 'nomor' ke 'number'
    val ayahs: List<Ayat> // Ubah dari 'ayah' ke 'ayahs'
)

data class Ayat(
    val number: Int, // Nomor global ayat di Al-Qur'an
    val text: String, // Teks Arab
    val numberInSurah: Int, // Nomor ayat dalam surah
    val surah: InfoSurah // Informasi surah untuk ayat ini
)

data class InfoSurah(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String, // Ubah dari 'jenisWahyu' ke 'revelationType'
    val numberOfAyahs: Int
)

// Kelas untuk menggabungkan teks Arab dan terjemahan (tetap sama)
data class AyatDetail(
    val surahNumber: Int,
    val surahName: String,
    val ayahNumberInSurah: Int,
    val teksArab: String,
    val terjemahan: String
)