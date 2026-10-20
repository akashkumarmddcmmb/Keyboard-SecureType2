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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.SmartBackspaceHandler
import com.example.model.KeyboardHeight
import com.example.model.KeyboardTheme
import com.example.model.TypingMode
import com.example.ui.KeyboardViewModel
import com.example.ui.keyboard.KeyButton
import com.example.ui.keyboard.KeyboardHost

@Composable
fun PlaygroundScreen(
    viewModel: KeyboardViewModel,
    onNavigateToThemes: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme by viewModel.currentTheme.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val text by viewModel.playgroundText.collectAsState()
    val cursorPosition by viewModel.cursorPosition.collectAsState()
    val clips by viewModel.clipboardClips.collectAsState()
    val backspaceDemoText by viewModel.backspaceDemoText.collectAsState()
    val scope = rememberCoroutineScope()

    val smartBackspaceHandler = SmartBackspaceHandler(
        scope = scope,
        onDelete = { viewModel.deleteDemoText() },
        isSmartEnabled = { settings.smartBackspaceEnabled },
        minRepeatIntervalMs = settings.smartBackspaceRepeatMs
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070B14))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Hero Header matching the Graphic
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF00E5FF), Color(0xFF6366F1), Color(0xFFEC4899))
                            )
                        )
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0B132B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "SecureType Shield",
                            tint = Color(0xFF00F0FF),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = "SecureType",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Keyboard",
                        color = Color(0xFF00E5FF),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Type Secure  •  Type Smart  •  Your Privacy Matters",
                color = Color(0xFF94A3B8),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 6 Highlight Badges from the Image
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeaturePill(Icons.Default.Security, "100% Offline\nWorks", Color(0xFF00F0FF))
                FeaturePill(Icons.Default.Bolt, "Fast\nTyping", Color(0xFFFACC15))
                FeaturePill(Icons.Default.Lock, "Privacy\nFirst", Color(0xFF10B981))
                FeaturePill(Icons.Default.Palette, "Multi\nThemes", Color(0xFFA855F7))
                FeaturePill(Icons.Default.Mic, "Voice\nTyping", Color(0xFF38BDF8))
                FeaturePill(Icons.Default.ContentPaste, "Clipboard\nAccess", Color(0xFFF43F5E))
            }
        }

        // Phone Frame Mockup (Matching Top Center of Infographic)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(26.dp), spotColor = Color(0xFF00E5FF).copy(alpha = 0.25f)),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = theme.backgroundColor),
            border = BorderStroke(
                2.dp,
                Brush.linearGradient(
                    listOf(theme.accentColor.copy(alpha = 0.6f), Color(0xFF334155))
                )
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Phone Top Speaker & Status Mockup Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "9:41",
                        color = theme.keyTextColor.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // Speaker Pill
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(5.dp)
                            .clip(CircleShape)
                            .background(theme.keyColor)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SecureType",
                            color = theme.accentColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Phone Input Area: "Type anything..." with send & clear buttons
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(theme.keyColor.copy(alpha = 0.6f))
                        .border(1.dp, theme.accentColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (text.isEmpty()) "Type anything..." else text,
                                color = if (text.isEmpty()) theme.keyTextColor.copy(alpha = 0.4f) else theme.keyTextColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                maxLines = 2
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = "${text.length} chars",
                                    color = theme.keyTextColor.copy(alpha = 0.5f),
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "• 100% Offline Secured",
                                    color = theme.accentColor.copy(alpha = 0.8f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (text.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(theme.specialKeyColor)
                                        .clickable { viewModel.clearPlayground() }
                                        .testTag("clear_input_btn"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = theme.keyTextColor,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(theme.accentColor)
                                    .clickable {
                                        if (text.isNotBlank()) {
                                            viewModel.addClip(text)
                                        }
                                    }
                                    .testTag("submit_input_btn"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                    tint = theme.backgroundColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // The Live Interactive Keyboard
                KeyboardHost(
                    theme = theme,
                    settings = settings,
                    currentText = text,
                    cursorPosition = cursorPosition,
                    onInsertText = { viewModel.insertText(it) },
                    onDeleteText = { viewModel.deleteText() },
                    onMoveCursor = { viewModel.moveCursor(it) },
                    onSelectAll = { viewModel.insertText(" [All Selected] ") },
                    onCut = { viewModel.clearPlayground() },
                    onCopy = { viewModel.addClip(text) },
                    onPaste = { clips.firstOrNull()?.text?.let { viewModel.insertText(it) } },
                    onUndo = { viewModel.undo() },
                    onRedo = { viewModel.redo() },
                    clipboardItems = clips,
                    onTogglePinClip = { viewModel.togglePinClip(it) },
                    onDeleteClip = { viewModel.deleteClip(it) },
                    onAddClip = { viewModel.addClip(it) },
                    onClearClips = { viewModel.clearAllClips() },
                    onOpenSettings = onNavigateToSettings,
                    onOpenThemes = onNavigateToThemes,
                    onChangeTypingMode = { viewModel.setTypingMode(it) }
                )
            }
        }

        // Smart Backspace Interactive Test Module (Matching Infographic Section)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1))))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0284C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Smart Backspace",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Fast, Smooth, No Lag.",
                                color = Color(0xFF38BDF8),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF1E293B))
                            .clickable { viewModel.resetDemoText() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("reset_demo_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Refresh, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(14.dp))
                            Text("Reset Text", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                        }
                    }
                }

                // Checkmarks list
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    BackspaceFeatureRow("Instant first delete")
                    BackspaceFeatureRow("Ultra fast repeat (25ms)")
                    BackspaceFeatureRow("Gradual speed increase")
                    BackspaceFeatureRow("No speed fluctuation")
                    BackspaceFeatureRow("Stops when you release")
                }

                // Interactive Backspace Test Strip
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E293B))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = if (backspaceDemoText.isEmpty()) "[Text empty - tap Reset Text to try again]" else backspaceDemoText,
                            color = if (backspaceDemoText.isEmpty()) Color(0xFF64748B) else Color(0xFFF1F5F9),
                            fontSize = 13.sp,
                            maxLines = 3
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            KeyButton(
                                modifier = Modifier.width(130.dp),
                                text = "Hold to Delete",
                                isSpecial = true,
                                theme = KeyboardTheme.DarkBlue,
                                onKeyTap = {},
                                onPointerDown = { smartBackspaceHandler.onPointerDown() },
                                onPointerUp = { smartBackspaceHandler.onPointerUp() },
                                customContent = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.Backspace,
                                            null,
                                            tint = Color(0xFF00F0FF),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            "Hold Delete",
                                            color = Color(0xFF00F0FF),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // Long Press Actions Showcase Card (Matching Image 2)
        var showLongPressActionsBubble by remember { mutableStateOf(false) }
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
                            text = "Long Press Actions",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "More Control, More Options",
                            color = Color(0xFF38BDF8),
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF00E5FF).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFF00E5FF), RoundedCornerShape(8.dp))
                            .clickable { showLongPressActionsBubble = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("demo_long_press_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Try Long Press Menu", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "Long press on keys reveals character accents (e.g. é, è, â, ₹) or quick actions like Cut, Copy, Paste, Select All.",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )

                // Typing Modes (Image 2 & 3: Standard, One-Handed, Floating)
                Text(
                    text = "Keyboard Layout & Ergonomics",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TypingMode.values().forEach { mode ->
                        val isSelected = settings.typingMode == mode
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFF00E5FF).copy(alpha = 0.2f) else Color(0xFF1E293B))
                                .border(1.dp, if (isSelected) Color(0xFF00E5FF) else Color(0xFF334155), RoundedCornerShape(8.dp))
                                .clickable { viewModel.setTypingMode(mode) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (mode) {
                                    TypingMode.STANDARD -> "Standard"
                                    TypingMode.ONE_HANDED_LEFT -> "1-Hand L"
                                    TypingMode.ONE_HANDED_RIGHT -> "1-Hand R"
                                    TypingMode.FLOATING -> "Floating"
                                },
                                color = if (isSelected) Color(0xFF00E5FF) else Color(0xFFCBD5E1),
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                // Keyboard Height (Image 2: Short / Medium / Tall)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Keyboard Height:", color = Color(0xFF94A3B8), fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        KeyboardHeight.values().forEach { h ->
                            val isSel = settings.keyboardHeight == h
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) Color(0xFF00E5FF).copy(alpha = 0.2f) else Color(0xFF1E293B))
                                    .border(1.dp, if (isSel) Color(0xFF00E5FF) else Color(0xFF334155), RoundedCornerShape(6.dp))
                                    .clickable { viewModel.setKeyboardHeight(h) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = h.displayName,
                                    color = if (isSel) Color(0xFF00E5FF) else Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showLongPressActionsBubble) {
            com.example.ui.keyboard.LongPressQuickActionsBubble(
                theme = theme,
                onCut = { viewModel.clearPlayground() },
                onCopy = { viewModel.addClip(text) },
                onPaste = { clips.firstOrNull()?.text?.let { viewModel.insertText(it) } },
                onSelectAll = { viewModel.insertText(" [Selected All] ") },
                onDismiss = { showLongPressActionsBubble = false }
            )
        }

        // Quick Themes Switcher Strip
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quick Themes Switcher",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = "View All →",
                    color = Color(0xFF00E5FF),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onNavigateToThemes() }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KeyboardTheme.allThemes.take(3).forEach { th ->
                    ThemeMiniChip(
                        theme = th,
                        isSelected = th.id == theme.id,
                        onClick = { viewModel.updateTheme(th) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FeaturePill(icon: ImageVector, label: String, accent: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.15f))
                .border(1.dp, accent.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accent,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = label,
            color = Color(0xFFE2E8F0),
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 11.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun BackspaceFeatureRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF10B981),
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = text,
            color = Color(0xFFE2E8F0),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun ThemeMiniChip(
    theme: KeyboardTheme,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(theme.keyboardBackgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) theme.accentColor else Color(0xFF334155),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 6.dp)
            .testTag("theme_chip_${theme.id}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(theme.accentColor)
            )
            Text(
                text = theme.name,
                color = if (isSelected) theme.accentColor else Color(0xFFCBD5E1),
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
