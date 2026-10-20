package com.example.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KeyboardLayouts
import com.example.model.KeyboardTheme

@Composable
fun EmojiKeyboard(
    theme: KeyboardTheme,
    onEmojiSelected: (String) -> Unit,
    onBackspace: () -> Unit,
    onSwitchToAbc: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val categories = listOf("Smileys", "Gestures", "Hearts", "Tech")

    val currentEmojis = when (selectedCategoryIndex) {
        0 -> KeyboardLayouts.emojiSmileys
        1 -> KeyboardLayouts.emojiGestures
        2 -> KeyboardLayouts.emojiHearts
        else -> KeyboardLayouts.emojiTech
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .background(theme.keyboardBackgroundColor)
    ) {
        // Category tabs
        ScrollableTabRow(
            selectedTabIndex = selectedCategoryIndex,
            containerColor = theme.keyboardBackgroundColor,
            contentColor = theme.accentColor,
            edgePadding = 8.dp,
            divider = {}
        ) {
            categories.forEachIndexed { index, title ->
                Tab(
                    selected = selectedCategoryIndex == index,
                    onClick = { selectedCategoryIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedCategoryIndex == index) theme.accentColor else theme.keyTextColor.copy(alpha = 0.6f),
                            fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        // Emoji grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(currentEmojis) { emoji ->
                Box(
                    modifier = Modifier
                        .height(38.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(theme.keyColor.copy(alpha = 0.3f))
                        .clickable { onEmojiSelected(emoji) }
                        .testTag("emoji_$emoji"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 22.sp)
                }
            }
        }

        // Bottom switch row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(theme.specialKeyColor)
                    .clickable { onSwitchToAbc() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("emoji_switch_abc"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ABC",
                    color = theme.specialKeyTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(theme.specialKeyColor)
                    .clickable { onBackspace() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("emoji_backspace"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = theme.specialKeyTextColor
                )
            }
        }
    }
}
