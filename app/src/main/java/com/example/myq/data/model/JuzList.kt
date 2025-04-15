package com.example.myq.data.model


data class Juz(
    val juzNumber: Int,
    val startSurah: String,
    val startAyah: Int,
    val endSurah: String,
    val endAyah: Int
)

val juzList = listOf(
    Juz(1, "Al-Fatihah", 1, "Al-Baqarah", 141),
    Juz(2, "Al-Baqarah", 142, "Al-Baqarah", 252),
    Juz(3, "Al-Baqarah", 253, "Ali 'Imran", 91),
    Juz(4, "Ali 'Imran", 92, "An-Nisa'", 23),
    Juz(5, "An-Nisa'", 24, "An-Nisa'", 147),
    Juz(6, "An-Nisa'", 148, "Al-Ma'idah", 82),
    Juz(7, "Al-Ma'idah", 83, "Al-An'am", 110),
    Juz(8, "Al-An'am", 111, "Al-A'raf", 87),
    Juz(9, "Al-A'raf", 88, "Al-Anfal", 40),
    Juz(10, "Al-Anfal", 41, "At-Taubah", 93),
    Juz(11, "At-Taubah", 94, "Hud", 5),
    Juz(12, "Hud", 6, "Yusuf", 52),
    Juz(13, "Yusuf", 53, "Ibrahim", 52),
    Juz(14, "Al-Hijr", 1, "An-Nahl", 128),
    Juz(15, "Al-Isra", 1, "Al-Kahf", 74),
    Juz(16, "Al-Kahf", 75, "Maryam", 98),
    Juz(17, "Taha", 1, "Al-Hajj", 78),
    Juz(18, "Al-Mu'minun", 1, "Al-Furqan", 20),
    Juz(19, "Al-Furqan", 21, "An-Naml", 55),
    Juz(20, "An-Naml", 56, "Al-Ankabut", 45),
    Juz(21, "Al-Ankabut", 46, "Al-Ahzab", 30),
    Juz(22, "Al-Ahzab", 31, "Ya-Sin", 27),
    Juz(23, "Ya-Sin", 28, "Az-Zumar", 31),
    Juz(24, "Az-Zumar", 32, "Fussilat", 46),
    Juz(25, "Fussilat", 47, "Al-Jathiya", 37),
    Juz(26, "Al-Ahqaf", 1, "Az-Zariyat", 30),
    Juz(27, "Az-Zariyat", 31, "Al-Hadid", 29),
    Juz(28, "Al-Mujadila", 1, "At-Tahrim", 12),
    Juz(29, "Al-Mulk", 1, "Al-Mursalat", 50),
    Juz(30, "An-Naba", 1, "An-Nas", 6)
)