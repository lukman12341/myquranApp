package com.example.myq.data.network

import com.example.myq.data.model.AyahResponse
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
}
