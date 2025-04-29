package com.example.myq.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myq.R
import com.example.myq.data.auth.FirebaseAuthManager
import com.example.myq.data.model.Surah
import com.example.myq.ui.theme.FontSettings
import com.example.myq.ui.theme.ThemeState
import com.example.myq.viewmodel.SplashViewModel
import com.example.myq.viewmodel.SurahViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    surahViewModel: SurahViewModel = viewModel(),
    splashViewModel: SplashViewModel = viewModel()
) {
    val tabTitles = listOf("SURAH", "JUZ", "BOOKMARK")
    var selectedTabIndex by remember { mutableStateOf(0) }
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showSettingsDialog by remember { mutableStateOf(false) }
    val isDarkTheme = ThemeState.isDarkTheme
    val surahList by surahViewModel.surahList.collectAsState()
    val showSplash by splashViewModel.showSplash.collectAsState()

    // Fetch current user for profile indicator
    val currentUser = FirebaseAuthManager.getCurrentUser()
    val userInitials = currentUser?.displayName?.take(2)?.uppercase() ?:
    currentUser?.email?.take(2)?.uppercase() ?: "?"

    val filteredList = if (searchQuery.isEmpty()) {
        surahList
    } else {
        surahList.filter {
            it.englishName.contains(searchQuery, ignoreCase = true) ||
                    it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    LaunchedEffect(Unit) {
        surahViewModel.fetchSurahList()
        if (showSplash) {
            delay(2000) // Show splash screen for 2 seconds
            splashViewModel.hideSplash()
        }
    }

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        if (showSplash) {
            SplashContent()
        } else {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            )
                            .shadow(4.dp),
                        title = {
                            if (isSearching) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = {
                                        Text(
                                            "Cari surah...",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 16.sp
                                        )
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth(0.85f)
                                        .clip(RoundedCornerShape(24.dp)),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        cursorColor = MaterialTheme.colorScheme.primary
                                    ),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    trailingIcon = {
                                        IconButton(onClick = { searchQuery = ""; isSearching = false }) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Tutup Pencarian",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    },
                                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            } else {
                                Text(
                                    "Al-Qur'an Indonesia",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { isSearching = !isSearching }) {
                                Icon(
                                    imageVector = if (isSearching) Icons.Default.Close else Icons.Default.Search,
                                    contentDescription = if (isSearching) "Tutup Pencarian" else "Cari Surah",
                                    modifier = Modifier.size(28.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            IconButton(onClick = { showSettingsDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Pengaturan",
                                    modifier = Modifier.size(28.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            // Profile Indicator
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f))
                                    .clickable {
                                        navController.navigate("profile")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (currentUser != null) {
                                    Text(
                                        text = userInitials,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Profil",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = MaterialTheme.colorScheme.primary,
                                height = 3.dp
                            )
                        },
                        modifier = Modifier.shadow(2.dp)
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 16.sp,
                                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    }

                    when (selectedTabIndex) {
                        0 -> SurahListScreen(navController, filteredList, FontSettings.translationFontSize, FontSettings.arabicFontSize)
                        1 -> JuzListScreen(navController)
                        2 -> BookmarkPlaceholderScreen()
                    }
                }
            }

            HomeSettingsDialog(
                showDialog = showSettingsDialog,
                currentLanguage = "id",
                currentTheme = if (isDarkTheme) "dark" else "light",
                currentTranslationFontSize = FontSettings.translationFontSize,
                currentArabicFontSize = FontSettings.arabicFontSize,
                onDismiss = { showSettingsDialog = false },
                onConfirm = { lang, theme, transFontSize, arabFontSize ->
                    ThemeState.toggleTheme(theme == "dark")
                    FontSettings.translationFontSize = transFontSize
                    FontSettings.arabicFontSize = arabFontSize
                    showSettingsDialog = false
                }
            )
        }
    }
}

@Composable
private fun SplashContent() {
    val alphaAnimation = remember { Animatable(0f) }
    val scaleAnimation = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        alphaAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = LinearOutSlowInEasing
            )
        )
        scaleAnimation.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(scaleAnimation.value)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "Gambar Al-Qur'an",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Text(
                text = "Al-Qur'an Indonesia",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = alphaAnimation.value)
                )
            )

            Text(
                text = "Baca dan Pelajari Al-Qur'an",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = alphaAnimation.value * 0.7f)
                )
            )
        }

        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            strokeWidth = 3.dp
        )
    }
}

@Composable
private fun SurahListScreen(
    navController: NavHostController,
    surahList: List<Surah>,
    translationFontSize: Float,
    arabicFontSize: Float
) {
    if (surahList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(surahList) { surah ->
                SurahItem(navController, surah, translationFontSize, arabicFontSize)
            }
        }
    }
}

@Composable
private fun SurahItem(
    navController: NavHostController,
    surah: Surah,
    translationFontSize: Float,
    arabicFontSize: Float
) {
    var isPressed by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        else MaterialTheme.colorScheme.surface,
        label = "backgroundColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                    )
                )
            )
            .clickable(
                onClick = {
                    isPressed = true
                    navController.navigate("surahDetail/${surah.number}")
                },
                onClickLabel = "Buka Surah ${surah.englishName}"
            )
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${surah.number}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Column {
                    Text(
                        text = surah.englishName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = surah.englishNameTranslation,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = translationFontSize.sp,
                            color = MaterialTheme.colorScheme.onSurface // Diperbaiki: Ganti onSurfaceVariant jadi onSurface
                        )
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = surah.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = arabicFontSize.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${surah.numberOfAyahs} Ayat",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.End
                )
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(200)
            isPressed = false
        }
    }
}
@Composable
private fun JuzListScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Book,
                contentDescription = "Juz",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Daftar Juz",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Button(onClick = { navController.navigate("juz_list") }) {
                Text("Lihat Daftar Juz")
            }
        }
    }
}

@Composable
private fun BookmarkPlaceholderScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Book,
                contentDescription = "Bookmark",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Bookmark Belum Tersedia",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeSettingsDialog(
    showDialog: Boolean,
    currentLanguage: String,
    currentTheme: String,
    currentTranslationFontSize: Float,
    currentArabicFontSize: Float,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Float, Float) -> Unit
) {
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }
    var selectedTheme by remember { mutableStateOf(currentTheme) }
    var translationFontSize by remember { mutableStateOf(currentTranslationFontSize) }
    var arabicFontSize by remember { mutableStateOf(currentArabicFontSize) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Pengaturan Aplikasi",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Bahasa",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = { selectedLanguage = "id" },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedLanguage == "id") MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else Color.Transparent
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (selectedLanguage == "id") MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Text(
                                    "Indonesia",
                                    color = if (selectedLanguage == "id") MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            OutlinedButton(
                                onClick = { selectedLanguage = "en" },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedLanguage == "en") MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else Color.Transparent
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (selectedLanguage == "en") MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Text(
                                    "English",
                                    color = if (selectedLanguage == "en") MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Tema",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = { selectedTheme = "light" },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedTheme == "light") MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else Color.Transparent
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (selectedTheme == "light") MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Text(
                                    "Terang",
                                    color = if (selectedTheme == "light") MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            OutlinedButton(
                                onClick = { selectedTheme = "dark" },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedTheme == "dark") MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else Color.Transparent
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (selectedTheme == "dark") MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Text(
                                    "Gelap",
                                    color = if (selectedTheme == "dark") MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Ukuran Huruf Terjemahan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Slider(
                            value = translationFontSize,
                            onValueChange = { translationFontSize = it },
                            valueRange = 12f..20f,
                            steps = 7,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "${translationFontSize.toInt()} sp",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Ukuran Huruf Arab",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Slider(
                            value = arabicFontSize,
                            onValueChange = { arabicFontSize = it },
                            valueRange = 16f..28f,
                            steps = 11,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "${arabicFontSize.toInt()} sp",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm(selectedLanguage, selectedTheme, translationFontSize, arabicFontSize)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Simpan",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        "Batal",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        )
    }
}