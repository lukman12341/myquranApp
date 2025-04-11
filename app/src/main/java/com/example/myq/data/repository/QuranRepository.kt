package com.example.myq.data.repository

import com.example.myq.data.model.AyahResponse
import com.example.myq.data.model.SurahResponse
import com.example.myq.data.network.QuranApiService
import com.example.myq.data.repository.QuranRepository

class QuranRepository(private val apiService: QuranApiService) {

    suspend fun getSurahList(): SurahResponse {
        return apiService.getSurahList()
    }

    suspend fun getSurahDetail(number: Int): AyahResponse {
        return apiService.getSurahDetail(number)
    }
}