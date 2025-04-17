# MyQuran App

Aplikasi **MyQuran** adalah aplikasi Android yang dirancang untuk membaca, mempelajari, dan mendengarkan Al-Qur'an. Aplikasi ini menggunakan teknologi modern seperti Jetpack Compose untuk antarmuka pengguna dan Retrofit untuk komunikasi API.

## Fitur Utama

1. **Daftar Surah**: Menampilkan daftar surah lengkap dengan nama Arab, nama Inggris, dan terjemahan.
2. **Detail Surah**: Menampilkan ayat-ayat dalam surah tertentu, termasuk teks Arab, terjemahan, dan kontrol audio.
3. **Daftar Juz**: Menampilkan daftar Juz Al-Qur'an dengan informasi surah dan ayat awal serta akhir.
4. **Detail Juz**: Menampilkan ayat-ayat dalam Juz tertentu, termasuk teks Arab dan terjemahan.
5. **Audio Playback**: Memutar audio untuk setiap surah atau ayat menggunakan API Al-Qur'an Cloud.
6. **Pencarian Surah**: Fitur pencarian untuk menemukan surah berdasarkan nama atau terjemahan.
7. **Pengaturan Tema**: Mendukung mode terang dan gelap.
8. **Pengaturan Ukuran Font**: Mengatur ukuran font untuk teks Arab dan terjemahan.
9. **Splash Screen**: Tampilan awal dengan animasi sebelum masuk ke aplikasi utama.

## Teknologi yang Digunakan

- **Jetpack Compose**: Untuk membangun antarmuka pengguna modern.
- **Retrofit**: Untuk komunikasi dengan API Al-Qur'an Cloud.
- **Kotlin Coroutines**: Untuk operasi asynchronous.
- **MediaPlayer**: Untuk memutar audio ayat dan surah.
- **State Management**: Menggunakan `StateFlow` untuk mengelola data secara reaktif.

## Struktur Proyek

### 1. **Model**
   - `SurahResponse`, `Surah`, `TranslationResponse`, `AyahTranslation`: Model data untuk respons API.

### 2. **Network**
   - `ApiClient`: Konfigurasi Retrofit untuk komunikasi API.
   - `ApiService`: Interface untuk endpoint API Al-Qur'an Cloud.

### 3. **Repository**
   - `SurahRepository`: Mengelola data surah dari API.

### 4. **ViewModel**
   - `SurahViewModel`: Mengelola data surah, ayat, terjemahan, dan kontrol audio.
   - `JuzViewModel`: Mengelola data Juz dan detailnya.
   - `BookmarkViewModel`: (Placeholder) Untuk fitur bookmark di masa depan.

### 5. **UI**
   - **Navigasi**: `NavGraph` untuk mengatur navigasi antar layar.
   - **Layar Utama**: `HomeScreen` dengan tab untuk Surah, Juz, dan Bookmark.
   - **Daftar Surah**: `SurahListScreen` untuk menampilkan daftar surah.
   - **Detail Surah**: `SurahDetailScreen` untuk menampilkan ayat-ayat dalam surah tertentu.
   - **Daftar Juz**: `JuzListScreen` untuk menampilkan daftar Juz.
   - **Detail Juz**: `JuzDetailScreen` untuk menampilkan ayat-ayat dalam Juz tertentu.
   - **Pengaturan**: `SettingsDialog` untuk mengatur tema dan bahasa.

### 6. **Tema**
   - `ThemeState`: Mengelola tema terang dan gelap.
   - `FontSettings`: Mengelola ukuran font untuk teks Arab dan terjemahan.

### 7. **Worker**
   - `ReminderWorker`: (Placeholder) Untuk fitur pengingat di masa depan.

## API yang Digunakan

Aplikasi ini menggunakan [API Al-Qur'an Cloud](https://alquran.cloud/) untuk mendapatkan data surah, ayat, terjemahan, dan audio.
## Dependencies

Proyek ini menggunakan dependensi berikut:

### Android Jetpack Compose
- `androidx.compose.ui:ui`
- `androidx.compose.ui:ui-tooling-preview`
- `androidx.compose.material3:material3`
- `androidx.compose.material:material-icons-extended`

### Lifecycle
- `androidx.lifecycle:lifecycle-runtime-ktx:2.6.2`
- `androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2`

### Navigation
- `androidx.navigation:navigation-compose:2.7.5`

### Networking
- `com.squareup.retrofit2:retrofit:2.9.0`
- `com.squareup.retrofit2:converter-gson:2.9.0`

### Coroutines
- `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3`

### Testing
- `junit:junit:4.13.2`
- `androidx.compose.ui:ui-test-junit4`
- `androidx.compose.ui:ui-tooling`
- `androidx.compose.ui:ui-test-manifest`
