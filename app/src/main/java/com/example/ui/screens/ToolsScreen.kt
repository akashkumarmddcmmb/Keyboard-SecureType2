package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.ui.KeyboardViewModel

@Composable
fun ToolsScreen(
    viewModel: KeyboardViewModel,
    modifier: Modifier = Modifier
) {
    val activeLang by viewModel.activeLanguage.collectAsState()
    val shortcuts by viewModel.shortcuts.collectAsState()
    val clips by viewModel.clipboardClips.collectAsState()

    var showAddShortcutDialog by remember { mutableStateOf(false) }
    var newTrigger by remember { mutableStateOf("") }
    var newReplacement by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070B14))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Header
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Keyboard,
                    contentDescription = null,
                    tint = Color(0xFF00F0FF),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Layouts & Powerful Tools",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Multiple layouts, multilingual typing, and text utility toolkit.",
                color = Color(0xFF94A3B8),
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Section: Multilingual Support (Matching Infographic Bottom Right)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Language, null, tint = Color(0xFF38BDF8), modifier = Modifier.size(20.dp))
                    Text(
                        text = "Multilingual Support",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Text(
                    text = "Type seamlessly in your native language with offline transliteration & full scripts.",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Language.allLanguages.forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (lang.code == activeLang.code) Color(0xFF1E293B) else Color(0xFF131E30))
                                .clickable { viewModel.updateLanguage(lang) }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .testTag("lang_${lang.code}"),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(lang.flag, fontSize = 20.sp)
                                Column {
                                    Text(
                                        text = "${lang.name} (${lang.nativeName})",
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = lang.sampleText,
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            if (lang.code == activeLang.code) {
                                Icon(Icons.Default.Check, null, tint = Color(0xFF00F0FF), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // Section: "Other Useful Tools" Grid from Graphic
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Other Useful Tools",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                // 2x3 Grid of Tools
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ToolGridItem(
                        icon = Icons.Default.ContentPaste,
                        title = "Clipboard\nAccess",
                        accent = Color(0xFF00F0FF),
                        modifier = Modifier.weight(1f)
                    )
                    ToolGridItem(
                        icon = Icons.Default.Edit,
                        title = "Text\nEditing",
                        accent = Color(0xFF38BDF8),
                        modifier = Modifier.weight(1f)
                    )
                    ToolGridItem(
                        icon = Icons.Default.NearMe,
                        title = "Cursor\nControl",
                        accent = Color(0xFFA855F7),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ToolGridItem(
                        icon = Icons.Default.SelectAll,
                        title = "Select All\nCut / Copy / Paste",
                        accent = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                    ToolGridItem(
                        icon = Icons.Default.Mic,
                        title = "Voice\nTyping",
                        accent = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                    ToolGridItem(
                        icon = Icons.Default.ShortText,
                        title = "Custom\nShortcuts",
                        accent = Color(0xFFEC4899),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section: Custom Shortcuts Manager
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Custom Shortcuts",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Type abbreviation to expand text automatically.",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF00E5FF).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFF00E5FF), RoundedCornerShape(8.dp))
                            .clickable { showAddShortcutDialog = true }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                            .testTag("add_shortcut_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Add, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(14.dp))
                            Text("Add", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                shortcuts.forEach { shortcut ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E293B))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF0F172A))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(shortcut.trigger, color = Color(0xFF00F0FF), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Text("→", color = Color(0xFF64748B), fontSize = 12.sp)
                            Text(shortcut.replacement, color = Color.White, fontSize = 12.sp)
                        }

                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Remove shortcut",
                            tint = Color(0xFF64748B),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { viewModel.removeShortcut(shortcut.id) }
                        )
                    }
                }
            }
        }

        // Section: Personal Dictionary (Matching Image 3: Personal Dictionary)
        val settings by viewModel.settings.collectAsState()
        var showAddWordDialog by remember { mutableStateOf(false) }
        var newDictWord by remember { mutableStateOf("") }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Personal Dictionary",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Custom words taught to your offline keyboard.",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF00E5FF).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFF00E5FF), RoundedCornerShape(8.dp))
                            .clickable { showAddWordDialog = true }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                            .testTag("add_word_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+ Add Word", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    settings.customDictionary.forEach { word ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(word, color = Color(0xFF00F0FF), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }

        if (showAddWordDialog) {
            AlertDialog(
                onDismissRequest = { showAddWordDialog = false },
                title = { Text("Add to Personal Dictionary", fontWeight = FontWeight.Bold) },
                text = {
                    OutlinedTextField(
                        value = newDictWord,
                        onValueChange = { newDictWord = it },
                        label = { Text("Word or Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newDictWord.isNotBlank()) {
                                viewModel.addCustomDictionaryWord(newDictWord.trim())
                                newDictWord = ""
                                showAddWordDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
                    ) {
                        Text("Add", color = Color(0xFF0A1128), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddWordDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (showAddShortcutDialog) {
        AlertDialog(
            onDismissRequest = { showAddShortcutDialog = false },
            title = { Text("Add Text Shortcut", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newTrigger,
                        onValueChange = { newTrigger = it },
                        label = { Text("Abbreviation (e.g. 'omw')") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newReplacement,
                        onValueChange = { newReplacement = it },
                        label = { Text("Expansion (e.g. 'On my way!')") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addShortcut(newTrigger, newReplacement)
                        newTrigger = ""
                        newReplacement = ""
                        showAddShortcutDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
                ) {
                    Text("Save", color = Color(0xFF0A1128), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddShortcutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ToolGridItem(
    icon: ImageVector,
    title: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E293B))
            .border(1.dp, accent.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
            }
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 13.sp
            )
        }
    }
}
