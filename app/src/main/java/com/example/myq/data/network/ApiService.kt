package com.example.myq.data.network

import com.example.myq.data.model.AyahResponse
import com.example.myq.data.model.JuzResponse
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

    @GET("v1/juz")
    suspend fun getDaftarJuz(): SurahResponse

    @GET("v1/juz/{nomorJuz}/quran-uthmani")
    suspend fun getDetailJuz(@Path("nomorJuz") nomorJuz: Int): JuzResponse

    @GET("v1/juz/{nomorJuz}/id.indonesian")
    suspend fun getTerjemahanJuz(@Path("nomorJuz") nomorJuz: Int): TranslationResponse

    // New endpoint for audio (using Alafasy recitation)
    @GET("v1/surah/{surahNumber}/ar.alafasy")
    suspend fun getSurahAudio(@Path("surahNumber") surahNumber: Int): AyahResponse

}
