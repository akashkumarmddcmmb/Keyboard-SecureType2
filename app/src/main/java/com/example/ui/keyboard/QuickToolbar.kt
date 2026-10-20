package com.example.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KeyboardTheme

enum class QuickTool {
    EMOJI,
    GIF,
    CLIPBOARD,
    TEXT_EDIT,
    VOICE,
    THEMES,
    SETTINGS
}

@Composable
fun QuickToolbar(
    theme: KeyboardTheme,
    activeTool: QuickTool?,
    onToolClick: (QuickTool) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(38.dp)
            .background(theme.keyboardBackgroundColor)
            .padding(horizontal = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToolbarItem(
            icon = Icons.Default.Mood,
            label = "Emoji",
            isActive = activeTool == QuickTool.EMOJI,
            theme = theme,
            onClick = { onToolClick(QuickTool.EMOJI) }
        )
        // GIF / Reaction Sticker tool as shown in Image 2 & 3
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    if (activeTool == QuickTool.GIF) theme.accentColor.copy(alpha = 0.25f)
                    else theme.keyColor.copy(alpha = 0.2f)
                )
                .clickable { onToolClick(QuickTool.GIF) }
                .testTag("tool_gif"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "GIF",
                color = if (activeTool == QuickTool.GIF) theme.accentColor else theme.keyTextColor.copy(alpha = 0.75f),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp
            )
        }
        ToolbarItem(
            icon = Icons.Default.ContentPaste,
            label = "Clipboard",
            isActive = activeTool == QuickTool.CLIPBOARD,
            theme = theme,
            onClick = { onToolClick(QuickTool.CLIPBOARD) }
        )
        ToolbarItem(
            icon = Icons.Default.Edit,
            label = "Text Edit",
            isActive = activeTool == QuickTool.TEXT_EDIT,
            theme = theme,
            onClick = { onToolClick(QuickTool.TEXT_EDIT) }
        )
        ToolbarItem(
            icon = Icons.Default.Mic,
            label = "Voice",
            isActive = activeTool == QuickTool.VOICE,
            theme = theme,
            onClick = { onToolClick(QuickTool.VOICE) }
        )
        ToolbarItem(
            icon = Icons.Default.Palette,
            label = "Themes",
            isActive = activeTool == QuickTool.THEMES,
            theme = theme,
            onClick = { onToolClick(QuickTool.THEMES) }
        )
        ToolbarItem(
            icon = Icons.Default.Settings,
            label = "Settings",
            isActive = activeTool == QuickTool.SETTINGS,
            theme = theme,
            onClick = { onToolClick(QuickTool.SETTINGS) }
        )
    }
}

@Composable
private fun ToolbarItem(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    theme: KeyboardTheme,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                if (isActive) theme.accentColor.copy(alpha = 0.25f)
                else theme.keyColor.copy(alpha = 0.2f)
            )
            .clickable { onClick() }
            .testTag("tool_${label.lowercase().replace(" ", "_")}"),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(17.dp),
            tint = if (isActive) theme.accentColor else theme.keyTextColor.copy(alpha = 0.75f)
        )
    }
}
