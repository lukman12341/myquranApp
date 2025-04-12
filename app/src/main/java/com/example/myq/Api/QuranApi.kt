package com.example.myq.Api

import com.example.myq.data.model.Surah
import com.example.myq.data.model.SurahResponse
import retrofit2.http.GET

interface QuranApi {
    @GET("surah")
    suspend fun getSurahs(): SurahResponse
}
