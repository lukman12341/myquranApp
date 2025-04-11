package com.example.myq.data.network

import com.example.myq.data.model.AyahResponse
import com.example.myq.data.model.SurahResponse
import com.example.myq.data.model.TranslationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("v1/surah")
    suspend fun getSurahList(): SurahResponse

    @GET("v1/surah/{surahNumber}")
    suspend fun getSurahDetail(@Path("surahNumber") surahNumber: Int): AyahResponse

    @GET("v1/surah/{surahNumber}/id.indonesian")
    suspend fun getSurahTranslation(@Path("surahNumber") surahNumber: Int): TranslationResponse
}
