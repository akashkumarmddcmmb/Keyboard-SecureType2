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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KeyboardTheme
import com.example.ui.KeyboardViewModel

@Composable
fun ThemesScreen(
    viewModel: KeyboardViewModel,
    modifier: Modifier = Modifier
) {
    val currentTheme by viewModel.currentTheme.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070B14))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Header matching infographic
        Column(modifier = Modifier.padding(bottom = 14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    tint = Color(0xFF00F0FF),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Beautiful Themes",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Choose your style, express your vibe.",
                color = Color(0xFF94A3B8),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(KeyboardTheme.allThemes, key = { it.id }) { theme ->
                ThemeCard(
                    theme = theme,
                    isSelected = theme.id == currentTheme.id,
                    onSelect = { viewModel.updateTheme(theme) }
                )
            }
        }
    }
}

@Composable
private fun ThemeCard(
    theme: KeyboardTheme,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("theme_card_${theme.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            brush = if (isSelected) {
                Brush.linearGradient(listOf(theme.accentColor, Color(0xFF38BDF8)))
            } else {
                Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF334155)))
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Theme title and active pill
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
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(theme.accentColor)
                    )
                    Text(
                        text = theme.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(theme.accentColor.copy(alpha = 0.2f))
                            .border(1.dp, theme.accentColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = theme.accentColor,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Active",
                                color = theme.accentColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E293B))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Apply",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Text(
                text = theme.description,
                color = Color(0xFF94A3B8),
                fontSize = 12.sp
            )

            // Miniature Realistic Keyboard Mockup Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(theme.keyboardBackgroundColor)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    // Mini Key Row 1
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P").forEach { char ->
                            MiniKey(char, theme, Modifier.weight(1f))
                        }
                    }

                    // Mini Key Row 2
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L").forEach { char ->
                            MiniKey(char, theme, Modifier.weight(1f))
                        }
                    }

                    // Mini Key Row 3
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MiniSpecialKey("⇧", theme, Modifier.weight(1.3f))
                        listOf("Z", "X", "C", "V", "B", "N", "M").forEach { char ->
                            MiniKey(char, theme, Modifier.weight(1f))
                        }
                        MiniSpecialKey("⌫", theme, Modifier.weight(1.3f))
                    }

                    // Mini Bottom Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MiniSpecialKey("?123", theme, Modifier.weight(1.3f))
                        MiniSpecialKey("🌐", theme, Modifier.weight(1.1f))
                        Box(
                            modifier = Modifier
                                .weight(4.4f)
                                .height(22.dp)
                                .padding(horizontal = 2.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(theme.keyColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "English",
                                color = theme.keyTextColor.copy(alpha = 0.6f),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        MiniKey(".", theme, Modifier.weight(1.1f))
                        MiniSpecialKey("↵", theme, Modifier.weight(1.3f), isAccent = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniKey(text: String, theme: KeyboardTheme, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(22.dp)
            .padding(horizontal = 1.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(theme.keyColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = theme.keyTextColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun MiniSpecialKey(
    text: String,
    theme: KeyboardTheme,
    modifier: Modifier = Modifier,
    isAccent: Boolean = false
) {
    Box(
        modifier = modifier
            .height(22.dp)
            .padding(horizontal = 1.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isAccent) theme.accentColor else theme.specialKeyColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isAccent) theme.backgroundColor else theme.specialKeyTextColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
