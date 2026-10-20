package com.example.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.SelectAll
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

@Composable
fun TextEditingPad(
    theme: KeyboardTheme,
    onMoveCursorLeft: () -> Unit,
    onMoveCursorRight: () -> Unit,
    onMoveCursorStart: () -> Unit,
    onMoveCursorEnd: () -> Unit,
    onSelectAll: () -> Unit,
    onCut: () -> Unit,
    onCopy: () -> Unit,
    onPaste: () -> Unit,
    onUndo: () -> Unit = {},
    onRedo: () -> Unit = {},
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(236.dp)
            .background(theme.keyboardBackgroundColor)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Text Editing & Cursor Control",
                color = theme.accentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(theme.keyColor)
                    .clickable { onClose() }
                    .testTag("text_edit_close"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close text editor",
                    tint = theme.keyTextColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Action Buttons Row 1: Select All, Cut, Copy, Paste
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            EditActionButton(label = "Select All", icon = Icons.Default.SelectAll, theme = theme, onClick = onSelectAll, modifier = Modifier.weight(1.1f))
            EditActionButton(label = "Cut", icon = Icons.Default.ContentCut, theme = theme, onClick = onCut, modifier = Modifier.weight(1f))
            EditActionButton(label = "Copy", icon = Icons.Default.ContentCopy, theme = theme, onClick = onCopy, modifier = Modifier.weight(1f))
            EditActionButton(label = "Paste", icon = Icons.Default.ContentPaste, theme = theme, onClick = onPaste, modifier = Modifier.weight(1f))
        }

        // Action Buttons Row 2: Undo, Redo, Home, End (from Image 3)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            EditActionButton(label = "Undo", icon = Icons.AutoMirrored.Filled.Undo, theme = theme, onClick = onUndo, modifier = Modifier.weight(1f))
            EditActionButton(label = "Redo", icon = Icons.AutoMirrored.Filled.Redo, theme = theme, onClick = onRedo, modifier = Modifier.weight(1f))
            EditActionButton(label = "Start", icon = Icons.Default.ArrowUpward, theme = theme, onClick = onMoveCursorStart, modifier = Modifier.weight(1f))
            EditActionButton(label = "End", icon = Icons.Default.ArrowDownward, theme = theme, onClick = onMoveCursorEnd, modifier = Modifier.weight(1f))
        }

        // D-Pad Navigation Area
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DpadButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDesc = "Cursor Left",
                theme = theme,
                onClick = onMoveCursorLeft
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .size(56.dp, 40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(theme.specialKeyColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DPAD",
                    color = theme.accentColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp
                )
            }

            DpadButton(
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                contentDesc = "Cursor Right",
                theme = theme,
                onClick = onMoveCursorRight
            )
        }
    }
}

@Composable
private fun EditActionButton(
    label: String,
    icon: ImageVector,
    theme: KeyboardTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(34.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(theme.keyColor)
            .clickable { onClick() }
            .testTag("action_${label.lowercase().replace(" ", "_")}"),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = theme.accentColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                color = theme.keyTextColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DpadButton(
    icon: ImageVector,
    contentDesc: String,
    theme: KeyboardTheme,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp, 40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(theme.keyColor)
            .clickable { onClick() }
            .testTag("dpad_${contentDesc.lowercase().replace(" ", "_")}"),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDesc,
            tint = theme.keyTextColor,
            modifier = Modifier.size(20.dp)
        )
    }
}
