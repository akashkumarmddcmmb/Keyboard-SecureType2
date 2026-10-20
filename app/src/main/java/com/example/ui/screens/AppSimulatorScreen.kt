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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.KeyboardViewModel
import com.example.ui.keyboard.KeyboardHost

@Composable
fun AppSimulatorScreen(
    viewModel: KeyboardViewModel,
    modifier: Modifier = Modifier
) {
    val theme by viewModel.currentTheme.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val clips by viewModel.clipboardClips.collectAsState()

    var selectedAppIndex by remember { mutableIntStateOf(0) }
    // 100% Original, trademark-free and copyright-free app categories
    val apps = listOf("Messages", "Browser", "Notes", "Social")

    // Local inputs for each app simulation
    var messagesInput by remember { mutableStateOf("Hello SecureType!") }
    var browserInput by remember { mutableStateOf("SecureType keyboard") }
    var notesInput by remember { mutableStateOf("Fast and secure typing with SecureType.") }
    var socialInput by remember { mutableStateOf("Awesome keyboard!") }

    val activeInput = when (selectedAppIndex) {
        0 -> messagesInput
        1 -> browserInput
        2 -> notesInput
        else -> socialInput
    }

    fun updateActiveInput(newText: String) {
        when (selectedAppIndex) {
            0 -> messagesInput = newText
            1 -> browserInput = newText
            2 -> notesInput = newText
            else -> socialInput = newText
        }
    }

    fun appendActiveInput(insert: String) {
        updateActiveInput(activeInput + insert)
    }

    fun deleteActiveInput() {
        if (activeInput.isNotEmpty()) {
            updateActiveInput(activeInput.dropLast(1))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070B14))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header matching graphic
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Smartphone,
                    contentDescription = null,
                    tint = Color(0xFF00F0FF),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Keyboard in Action",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Works smoothly in all your apps.",
                color = Color(0xFF94A3B8),
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // App Tabs (Messages, Browser, Notes, Social)
        ScrollableTabRow(
            selectedTabIndex = selectedAppIndex,
            containerColor = Color(0xFF0F172A),
            contentColor = Color(0xFF00F0FF),
            edgePadding = 6.dp,
            divider = {}
        ) {
            apps.forEachIndexed { index, appName ->
                Tab(
                    selected = selectedAppIndex == index,
                    onClick = { selectedAppIndex = index },
                    text = {
                        Text(
                            text = appName,
                            color = if (selectedAppIndex == index) Color(0xFF00F0FF) else Color(0xFF94A3B8),
                            fontWeight = if (selectedAppIndex == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        // Simulated App View Frame
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111827)),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                when (selectedAppIndex) {
                    0 -> ChatMessagesSimulationView(activeInput)
                    1 -> BrowserSimulationView(activeInput)
                    2 -> NotesSimulationView(activeInput)
                    else -> SocialSimulationView(activeInput)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Embedded SecureType Keyboard typing into the selected app
                KeyboardHost(
                    theme = theme,
                    settings = settings,
                    currentText = activeInput,
                    cursorPosition = activeInput.length,
                    onInsertText = { appendActiveInput(it) },
                    onDeleteText = { deleteActiveInput() },
                    onMoveCursor = {},
                    onSelectAll = {},
                    onCut = { updateActiveInput("") },
                    onCopy = { viewModel.addClip(activeInput) },
                    onPaste = { clips.firstOrNull()?.text?.let { appendActiveInput(it) } },
                    clipboardItems = clips,
                    onTogglePinClip = { viewModel.togglePinClip(it) },
                    onDeleteClip = { viewModel.deleteClip(it) },
                    onClearClips = { viewModel.clearAllClips() }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun ChatMessagesSimulationView(inputText: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0B141A))
    ) {
        // Chat Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1F2C34))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00A884)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("S", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Column {
                    Text("SecureType Chat", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("online • end-to-end encrypted", color = Color(0xFF00A884), fontSize = 10.sp)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Default.Videocam, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Icon(Icons.Default.Call, null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }

        // Chat message area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Received bubble
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(Color(0xFF202C33))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Hello! Try typing securely with SecureType keyboard below.",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            // User typing bubble
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 0.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(Color(0xFF005C4B))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (inputText.isEmpty()) "..." else inputText,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Message input bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1F2C34))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (inputText.isEmpty()) "Type a message..." else inputText,
                    color = if (inputText.isEmpty()) Color(0xFF8696A0) else Color.White,
                    fontSize = 13.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00A884)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun BrowserSimulationView(inputText: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E293B))
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Original search engine header (100% copyright free)
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(24.dp).padding(end = 6.dp))
            Text("Web Search", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        // Search Bar Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF0F172A))
                .border(1.dp, Color(0xFF334155), RoundedCornerShape(24.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.Default.Search, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                Text(
                    text = if (inputText.isEmpty()) "Search or type URL" else inputText,
                    color = if (inputText.isEmpty()) Color(0xFF94A3B8) else Color.White,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                )
                Icon(Icons.Default.Mic, null, tint = Color(0xFF38BDF8), modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun NotesSimulationView(inputText: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E24))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Secure Private Notes",
                color = Color(0xFFFACC15),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text("Locked 🔒", color = Color(0xFF94A3B8), fontSize = 11.sp)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF272730))
                .padding(12.dp)
        ) {
            Text(
                text = if (inputText.isEmpty()) "Tap keyboard to write private notes..." else inputText,
                color = if (inputText.isEmpty()) Color(0xFF71717A) else Color(0xFFF4F4F5),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun SocialSimulationView(inputText: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF18191A))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0284C7)),
                contentAlignment = Alignment.Center
            ) {
                Text("U", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Column {
                Text("Community User", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Public • Just now", color = Color(0xFFB0B3B8), fontSize = 10.sp)
            }
        }

        Text(
            text = if (inputText.isEmpty()) "Share your thoughts with the community..." else inputText,
            color = if (inputText.isEmpty()) Color(0xFFB0B3B8) else Color.White,
            fontSize = 14.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.ThumbUp, null, tint = Color(0xFF0284C7), modifier = Modifier.size(15.dp))
                Text("Like", color = Color(0xFFB0B3B8), fontSize = 12.sp)
            }
            Text("Comment", color = Color(0xFFB0B3B8), fontSize = 12.sp)
            Text("Share", color = Color(0xFFB0B3B8), fontSize = 12.sp)
        }
    }
}
