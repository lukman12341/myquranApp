package com.example.myq.data.network

import com.example.myq.data.model.AyahResponse
import com.example.myq.data.model.JuzResponse
import com.example.myq.data.model.SurahResponse
import com.example.myq.data.model.TranslationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // ✅ Ambil daftar seluruh surah (https://api.alquran.cloud/v1/surah)
    @GET("v1/surah")
    suspend fun getSurahList(): SurahResponse

    // ✅ Ambil detail surah berupa ayat-ayat (https://api.alquran.cloud/v1/surah/1)
    @GET("v1/surah/{surahNumber}")
    suspend fun getSurahDetail(@Path("surahNumber") surahNumber: Int): AyahResponse

    // ✅ Ambil terjemahan surah (https://api.alquran.cloud/v1/surah/1/id.indonesian)
    @GET("v1/surah/{surahNumber}/id.indonesian")
    suspend fun getSurahTranslation(@Path("surahNumber") surahNumber: Int): TranslationResponse

    @GET("v1/juz")
    suspend fun getDaftarJuz(): SurahResponse // Menggunakan SurahResponse karena strukturnya mirip

    // Ambil detail Juz dengan teks Arab
    @GET("v1/juz/{nomorJuz}/quran-uthmani")
    suspend fun getDetailJuz(@Path("nomorJuz") nomorJuz: Int): JuzResponse

    // Ambil terjemahan Juz
    @GET("v1/juz/{nomorJuz}/id.indonesian")
    suspend fun getTerjemahanJuz(@Path("nomorJuz") nomorJuz: Int): TranslationResponse
}
