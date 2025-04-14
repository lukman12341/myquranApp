package com.example.myq.data.repository

import com.example.myq.data.model.Surah
import com.example.myq.data.network.ApiClient

class SurahRepository {
    suspend fun getAllSurah(): List<Surah> {
        // Panggil API atau sumber data lain untuk mengambil data Surah
        val response = ApiClient.apiService.getSurahList() // Misalnya ini mengambil daftar Surah dari API
        return response.data // Sesuaikan dengan struktur respons dari API
    }
}
