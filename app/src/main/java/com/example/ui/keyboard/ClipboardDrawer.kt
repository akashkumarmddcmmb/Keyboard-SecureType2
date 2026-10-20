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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ClipboardItem
import com.example.model.KeyboardTheme

@Composable
fun ClipboardDrawer(
    clips: List<ClipboardItem>,
    theme: KeyboardTheme,
    onClipSelected: (String) -> Unit,
    onTogglePin: (String) -> Unit,
    onDeleteClip: (String) -> Unit,
    onAddClip: (String) -> Unit = {},
    onClearAll: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newClipText by remember { mutableStateOf("") }

    val displayedClips = remember(clips, selectedTab) {
        if (selectedTab == 0) {
            clips.filter { it.isPinned || !it.isHistory }
        } else {
            clips.sortedByDescending { it.timestamp }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(236.dp)
            .background(theme.keyboardBackgroundColor)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        // Top Bar: Tabs [Clipboard] [History] + Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabPill("Clipboard", selected = selectedTab == 0, theme = theme, onClick = { selectedTab = 0 })
                TabPill("History", selected = selectedTab == 1, theme = theme, onClick = { selectedTab = 1 })
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // + Add to clipboard button (from Image 2 & 3)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(theme.accentColor.copy(alpha = 0.2f))
                        .clickable { showAddDialog = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("add_clip_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(Icons.Default.Add, null, tint = theme.accentColor, modifier = Modifier.size(14.dp))
                        Text(
                            text = "Add",
                            color = theme.accentColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                if (clips.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(theme.keyColor)
                            .clickable { onClearAll() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("clipboard_clear_all"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Clear",
                            color = theme.keyTextColor.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(theme.keyColor)
                        .clickable { onClose() }
                        .testTag("clipboard_close"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = theme.keyTextColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        if (displayedClips.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (selectedTab == 0) "Clipboard is empty. Tap '+ Add' to save snippets." else "No recent history.",
                    color = theme.keyTextColor.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(displayedClips, key = { it.id }) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (item.isPinned) theme.specialKeyColor
                                else theme.keyColor.copy(alpha = 0.7f)
                            )
                            .clickable { onClipSelected(item.text) }
                            .padding(horizontal = 10.dp, vertical = 7.dp)
                            .testTag("clip_item_${item.id}"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.text,
                            color = theme.keyTextColor,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (item.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                contentDescription = "Pin clip",
                                tint = if (item.isPinned) theme.accentColor else theme.keyTextColor.copy(alpha = 0.4f),
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onTogglePin(item.id) }
                            )

                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete clip",
                                tint = theme.keyTextColor.copy(alpha = 0.4f),
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onDeleteClip(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add to Clipboard", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newClipText,
                    onValueChange = { newClipText = it },
                    label = { Text("Text snippet to store offline") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newClipText.isNotBlank()) {
                            onAddClip(newClipText.trim())
                            newClipText = ""
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.accentColor)
                ) {
                    Text("Save", color = theme.backgroundColor, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun TabPill(
    title: String,
    selected: Boolean,
    theme: KeyboardTheme,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (selected) theme.accentColor.copy(alpha = 0.2f) else theme.keyColor.copy(alpha = 0.4f))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (selected) theme.accentColor else theme.keyTextColor.copy(alpha = 0.6f),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 11.sp
        )
    }
}
