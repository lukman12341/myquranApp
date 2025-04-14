package com.example.myq.ui.screen



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myq.ui.theme.ThemeState

// com.example.myq.ui.screen.SettingsDialog
@Composable
fun SettingsDialog(
    showDialog: Boolean,
    currentLanguage: String,
    currentTheme: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 4.dp) {
                var selectedLanguage by remember { mutableStateOf(currentLanguage) }
                var selectedTheme by remember { mutableStateOf(currentTheme) }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Pengaturan", style = MaterialTheme.typography.titleLarge)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Bahasa", style = MaterialTheme.typography.titleMedium)

                    Column {
                        RadioButtonWithLabel("Indonesia", selectedLanguage == "id") {
                            selectedLanguage = "id"
                        }
                        RadioButtonWithLabel("English", selectedLanguage == "en") {
                            selectedLanguage = "en"
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Tema", style = MaterialTheme.typography.titleMedium)

                    Column {
                        RadioButtonWithLabel("Mode terang", selectedTheme == "light") {
                            selectedTheme = "light"
                        }
                        RadioButtonWithLabel("Mode gelap", selectedTheme == "dark") {
                            selectedTheme = "dark"
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("BATAL")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            // Update tema saat tombol OK diklik
                            ThemeState.toggleTheme(selectedTheme == "dark")
                            onConfirm(selectedLanguage, selectedTheme)
                            onDismiss()
                        }) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RadioButtonWithLabel(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}
