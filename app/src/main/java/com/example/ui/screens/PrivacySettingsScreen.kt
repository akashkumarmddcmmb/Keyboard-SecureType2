package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KeySoundType
import com.example.model.KeyboardHeight
import com.example.model.TypingMode
import com.example.ui.KeyboardViewModel

@Composable
fun PrivacySettingsScreen(
    viewModel: KeyboardViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()
    val theme by viewModel.currentTheme.collectAsState()
    var showDataWipedNotice by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070B14))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Privacy First Showcase Card (Matching Infographic Section)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D172A)),
            border = BorderStroke(2.dp, Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF3B82F6))))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Large Cyber Shield Badge
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFF00F0FF).copy(alpha = 0.35f), Color(0xFF0284C7).copy(alpha = 0.1f))
                            )
                        )
                        .border(2.dp, Color(0xFF00F0FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Privacy Shield",
                        tint = Color(0xFF00F0FF),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Privacy First",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Your data stays with you.",
                        color = Color(0xFF38BDF8),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // 5 Privacy Commitments from Graphic
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E293B))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PrivacyCommitmentRow(Icons.Default.Block, "No data collection", Color(0xFFF43F5E))
                    PrivacyCommitmentRow(Icons.Default.Block, "No typing logs", Color(0xFFF43F5E))
                    PrivacyCommitmentRow(Icons.Default.WifiOff, "No online tracking (100% Offline)", Color(0xFF00F0FF))
                    PrivacyCommitmentRow(Icons.Default.Lock, "Zero keystroke telemetry transmitted", Color(0xFF10B981))
                    PrivacyCommitmentRow(Icons.Default.Security, "Your privacy, our priority", Color(0xFF38BDF8))
                }

                // Erase local clipboard & cache button
                Button(
                    onClick = {
                        viewModel.clearAllClips()
                        viewModel.clearPlayground()
                        showDataWipedNotice = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444).copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Brush.linearGradient(listOf(Color(0xFFEF4444), Color(0xFFDC2626)))),
                    modifier = Modifier.fillMaxWidth().testTag("wipe_data_btn")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.DeleteSweep, null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                        Text(
                            text = if (showDataWipedNotice) "Local Data Cleared Successfully ✓" else "Wipe All Local History & Clips",
                            color = Color(0xFFFCA5A5),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Custom Settings (Matching Infographic "Custom Settings: Make it yours")
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Settings, null, tint = Color(0xFF00F0FF), modifier = Modifier.size(20.dp))
                    Text(
                        text = "Custom Settings",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Key Sound Setting
                SettingToggleRow(
                    title = "Key Sound",
                    subtitle = "Sound feedback on key press (${settings.keySoundType.displayName})",
                    checked = settings.keySoundType != KeySoundType.NONE,
                    onCheckedChange = { viewModel.toggleSound(it) }
                )

                if (settings.keySoundType != KeySoundType.NONE) {
                    // Sound type chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        KeySoundType.values().filter { it != KeySoundType.NONE }.forEach { st ->
                            val isSel = st == settings.keySoundType
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Color(0xFF00E5FF).copy(alpha = 0.2f) else Color(0xFF1E293B))
                                    .border(1.dp, if (isSel) Color(0xFF00E5FF) else Color(0xFF334155), RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setSoundType(st) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = st.displayName.split(" ").first(),
                                    color = if (isSel) Color(0xFF00E5FF) else Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    // Volume slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Volume", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            Text("${(settings.soundVolume * 100).toInt()}%", color = Color(0xFF00E5FF), fontSize = 11.sp)
                        }
                        Slider(
                            value = settings.soundVolume,
                            onValueChange = { viewModel.setSoundVolume(it) },
                            valueRange = 0.1f..1.0f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF00E5FF),
                                activeTrackColor = Color(0xFF00E5FF)
                            )
                        )
                    }
                }

                // Vibration Haptics
                SettingToggleRow(
                    title = "Vibration & Haptics",
                    subtitle = "Tactile response on every key press",
                    checked = settings.vibrationEnabled,
                    onCheckedChange = { viewModel.toggleVibration(it) }
                )

                // Smart Backspace
                SettingToggleRow(
                    title = "Smart Backspace",
                    subtitle = "Instant first delete + 25ms ultra-fast acceleration",
                    checked = settings.smartBackspaceEnabled,
                    onCheckedChange = { viewModel.toggleSmartBackspace(it) }
                )

                // Always Show Number Row
                SettingToggleRow(
                    title = "Number Row",
                    subtitle = "Display numbers (0-9) above QWERTY row",
                    checked = settings.numberRowEnabled,
                    onCheckedChange = { viewModel.toggleNumberRow(it) }
                )

                // Word Prediction & Suggestions
                SettingToggleRow(
                    title = "Word Prediction & Candidate Bar",
                    subtitle = "Smart offline next-word and completion suggestions",
                    checked = settings.predictionEnabled,
                    onCheckedChange = { viewModel.togglePrediction(it) }
                )

                // Auto Theme Follow System
                SettingToggleRow(
                    title = "Auto Theme (Follow System)",
                    subtitle = "Automatically switch between Dark & Light mode",
                    checked = settings.autoThemeFollowSystem,
                    onCheckedChange = { viewModel.toggleFollowSystemTheme(it) }
                )

                // Strict Privacy Mode
                SettingToggleRow(
                    title = "Strict Privacy Mode",
                    subtitle = "Lock all local history and force 100% offline isolation",
                    checked = settings.privacyMode,
                    onCheckedChange = { viewModel.togglePrivacyMode(it) }
                )

                // Keyboard Height Selection (Image 2)
                Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                    Text("Keyboard Height", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("Adjust size for thumb comfort", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        KeyboardHeight.values().forEach { h ->
                            val isSel = settings.keyboardHeight == h
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Color(0xFF00E5FF).copy(alpha = 0.2f) else Color(0xFF1E293B))
                                    .border(1.dp, if (isSel) Color(0xFF00E5FF) else Color(0xFF334155), RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setKeyboardHeight(h) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = h.displayName,
                                    color = if (isSel) Color(0xFF00E5FF) else Color(0xFFCBD5E1),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Typing Mode Selection (Image 2 & 3)
                Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                    Text("Ergonomics & Layout Mode", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("Standard, One-Handed, or Floating window", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TypingMode.values().forEach { mode ->
                            val isSel = settings.typingMode == mode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Color(0xFF00E5FF).copy(alpha = 0.2f) else Color(0xFF1E293B))
                                    .border(1.dp, if (isSel) Color(0xFF00E5FF) else Color(0xFF334155), RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setTypingMode(mode) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (mode) {
                                        TypingMode.STANDARD -> "Standard"
                                        TypingMode.ONE_HANDED_LEFT -> "1-Hand L"
                                        TypingMode.ONE_HANDED_RIGHT -> "1-Hand R"
                                        TypingMode.FLOATING -> "Floating"
                                    },
                                    color = if (isSel) Color(0xFF00E5FF) else Color(0xFFCBD5E1),
                                    fontSize = 10.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // App Screens Showcase / Splash Preview (Image 2: Splash, Settings, Theme, Language)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0284C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Security, null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Text("SecureType", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Type Freely, Stay Secure", color = Color(0xFF00E5FF), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Text(
                    text = "Ultra Fast • Privacy First • 100% Offline • Modern UI",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // System Keyboard Integration Wizard
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
                Text(
                    text = "Use as Android System Keyboard",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "SecureType can be enabled as your primary system on-screen keyboard in all phone apps.",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("enable_ime_settings_btn")
                    ) {
                        Text("1. Enable in Settings", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            try {
                                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                                imm?.showInputMethodPicker()
                            } catch (_: Exception) {}
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("switch_ime_picker_btn")
                    ) {
                        Text("2. Switch Keyboard", color = Color(0xFF0A1128), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun PrivacyCommitmentRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Text(text, color = Color(0xFFF1F5F9), fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Color(0xFF94A3B8), fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF00E5FF),
                checkedTrackColor = Color(0xFF0284C7),
                uncheckedThumbColor = Color(0xFF94A3B8),
                uncheckedTrackColor = Color(0xFF1E293B)
            )
        )
    }
}
