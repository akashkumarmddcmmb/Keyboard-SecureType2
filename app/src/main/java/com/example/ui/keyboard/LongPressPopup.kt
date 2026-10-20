package com.example.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.model.KeyboardTheme

@Composable
fun LongPressKeyPopup(
    items: List<String>,
    theme: KeyboardTheme,
    onItemSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Popup(
        alignment = Alignment.TopCenter,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = theme.specialKeyColor),
            modifier = Modifier
                .shadow(12.dp, RoundedCornerShape(12.dp))
                .border(1.dp, theme.accentColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { char ->
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(theme.keyColor)
                            .clickable {
                                onItemSelected(char)
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            color = theme.keyTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LongPressQuickActionsBubble(
    theme: KeyboardTheme,
    onCut: () -> Unit,
    onCopy: () -> Unit,
    onPaste: () -> Unit,
    onSelectAll: () -> Unit,
    onDismiss: () -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            modifier = Modifier
                .shadow(16.dp, RoundedCornerShape(14.dp))
                .border(1.dp, theme.accentColor, RoundedCornerShape(14.dp))
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ActionRowItem(label = "Cut", icon = Icons.Default.ContentCut, theme = theme, onClick = { onCut(); onDismiss() })
                ActionRowItem(label = "Copy", icon = Icons.Default.ContentCopy, theme = theme, onClick = { onCopy(); onDismiss() })
                ActionRowItem(label = "Paste", icon = Icons.Default.ContentPaste, theme = theme, onClick = { onPaste(); onDismiss() })
                ActionRowItem(label = "Select All", icon = Icons.Default.SelectAll, theme = theme, onClick = { onSelectAll(); onDismiss() })
            }
        }
    }
}

@Composable
private fun ActionRowItem(
    label: String,
    icon: ImageVector,
    theme: KeyboardTheme,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(theme.keyColor.copy(alpha = 0.5f))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = theme.accentColor, modifier = Modifier.size(16.dp))
        Text(text = label, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}
