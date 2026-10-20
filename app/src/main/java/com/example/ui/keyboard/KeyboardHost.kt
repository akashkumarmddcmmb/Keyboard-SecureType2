package com.example.ui.keyboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.FeedbackManager
import com.example.engine.PredictionEngine
import com.example.engine.SmartBackspaceHandler
import com.example.model.ClipboardItem
import com.example.model.KeyModel
import com.example.model.KeyType
import com.example.model.KeyboardHeight
import com.example.model.KeyboardLayoutType
import com.example.model.KeyboardLayouts
import com.example.model.KeyboardSettings
import com.example.model.KeyboardTheme
import com.example.model.TypingMode

@Composable
fun KeyboardHost(
    theme: KeyboardTheme,
    settings: KeyboardSettings,
    currentText: String,
    cursorPosition: Int,
    onInsertText: (String) -> Unit,
    onDeleteText: () -> Unit,
    onMoveCursor: (Int) -> Unit,
    onSelectAll: () -> Unit = {},
    onCut: () -> Unit = {},
    onCopy: () -> Unit = {},
    onPaste: () -> Unit = {},
    onUndo: () -> Unit = {},
    onRedo: () -> Unit = {},
    clipboardItems: List<ClipboardItem> = emptyList(),
    onTogglePinClip: (String) -> Unit = {},
    onDeleteClip: (String) -> Unit = {},
    onAddClip: (String) -> Unit = {},
    onClearClips: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenThemes: () -> Unit = {},
    onChangeTypingMode: ((TypingMode) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentView = LocalView.current
    val scope = rememberCoroutineScope()

    val feedbackManager = remember { FeedbackManager(context) }
    val predictionEngine = remember { PredictionEngine() }

    var isShifted by remember { mutableStateOf(false) }
    var isCapsLock by remember { mutableStateOf(false) }
    var currentLayout by remember { mutableStateOf(KeyboardLayoutType.QWERTY) }
    var activeTool by remember { mutableStateOf<QuickTool?>(null) }
    var showVoiceDialog by remember { mutableStateOf(false) }

    // Long press accents state
    var longPressAccents by remember { mutableStateOf<List<String>?>(null) }

    val smartBackspaceHandler = remember(settings.smartBackspaceEnabled, settings.smartBackspaceRepeatMs) {
        SmartBackspaceHandler(
            scope = scope,
            onDelete = {
                feedbackManager.playKeyFeedback(
                    settings.keySoundType,
                    settings.soundVolume,
                    settings.vibrationEnabled,
                    settings.vibrationStrength,
                    currentView
                )
                onDeleteText()
            },
            isSmartEnabled = { settings.smartBackspaceEnabled },
            minRepeatIntervalMs = settings.smartBackspaceRepeatMs
        )
    }

    val suggestions = remember(currentText, settings.selectedLanguageCode) {
        predictionEngine.getSuggestions(currentText, settings.selectedLanguageCode)
    }

    fun playFeedback() {
        feedbackManager.playKeyFeedback(
            settings.keySoundType,
            settings.soundVolume,
            settings.vibrationEnabled,
            settings.vibrationStrength,
            currentView
        )
    }

    // Outer layout based on typing mode (One-Handed / Floating / Standard)
    val outerModifier = when (settings.typingMode) {
        TypingMode.FLOATING -> {
            modifier
                .fillMaxWidth(0.88f)
                .shadow(16.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, theme.accentColor, RoundedCornerShape(20.dp))
        }
        else -> modifier.fillMaxWidth()
    }

    Box(
        modifier = outerModifier
            .let {
                if (theme.backgroundBrush != null) it.background(theme.backgroundBrush)
                else it.background(theme.keyboardBackgroundColor)
            }
            .padding(bottom = 6.dp)
            .testTag("keyboard_host")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Drag handle for Floating Keyboard mode
            if (settings.typingMode == TypingMode.FLOATING) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DragHandle,
                        contentDescription = "Floating Handle",
                        tint = theme.accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Quick Toolbar
            QuickToolbar(
                theme = theme,
                activeTool = activeTool,
                onToolClick = { tool ->
                    playFeedback()
                    when (tool) {
                        QuickTool.SETTINGS -> onOpenSettings()
                        QuickTool.THEMES -> onOpenThemes()
                        QuickTool.VOICE -> showVoiceDialog = true
                        QuickTool.EMOJI -> {
                            activeTool = if (activeTool == QuickTool.EMOJI) null else QuickTool.EMOJI
                        }
                        QuickTool.GIF -> {
                            activeTool = if (activeTool == QuickTool.GIF) null else QuickTool.GIF
                        }
                        QuickTool.CLIPBOARD -> {
                            activeTool = if (activeTool == QuickTool.CLIPBOARD) null else QuickTool.CLIPBOARD
                        }
                        QuickTool.TEXT_EDIT -> {
                            activeTool = if (activeTool == QuickTool.TEXT_EDIT) null else QuickTool.TEXT_EDIT
                        }
                    }
                }
            )

            // Suggestion Candidate Bar
            if (settings.predictionEnabled && activeTool == null) {
                SuggestionBar(
                    suggestions = suggestions,
                    theme = theme,
                    onSuggestionSelected = { word ->
                        playFeedback()
                        onInsertText("$word ")
                    }
                )
            }

            // Quick Switch Bar: [EN] [HI] [123] [😃] (Image 2 & 3: Quick Switch)
            if (activeTool == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuickSwitchPill(
                        text = "EN",
                        selected = currentLayout == KeyboardLayoutType.QWERTY,
                        theme = theme,
                        onClick = { playFeedback(); currentLayout = KeyboardLayoutType.QWERTY }
                    )
                    QuickSwitchPill(
                        text = "HI",
                        selected = currentLayout == KeyboardLayoutType.HINDI,
                        theme = theme,
                        onClick = { playFeedback(); currentLayout = KeyboardLayoutType.HINDI }
                    )
                    QuickSwitchPill(
                        text = "123",
                        selected = currentLayout == KeyboardLayoutType.NUMBERS,
                        theme = theme,
                        onClick = { playFeedback(); currentLayout = KeyboardLayoutType.NUMBERS }
                    )
                    QuickSwitchPill(
                        text = "!@#",
                        selected = currentLayout == KeyboardLayoutType.SYMBOLS,
                        theme = theme,
                        onClick = { playFeedback(); currentLayout = KeyboardLayoutType.SYMBOLS }
                    )
                    QuickSwitchPill(
                        text = "😃",
                        selected = activeTool == QuickTool.EMOJI,
                        theme = theme,
                        onClick = { playFeedback(); activeTool = QuickTool.EMOJI }
                    )
                }
            }

            // Expanded Tool Panels or Standard Keyboard
            when (activeTool) {
                QuickTool.EMOJI -> {
                    EmojiKeyboard(
                        theme = theme,
                        onEmojiSelected = { emoji ->
                            playFeedback()
                            onInsertText(emoji)
                        },
                        onBackspace = {
                            playFeedback()
                            onDeleteText()
                        },
                        onSwitchToAbc = {
                            playFeedback()
                            activeTool = null
                        }
                    )
                }
                QuickTool.GIF -> {
                    GifStickerPicker(
                        theme = theme,
                        onStickerSelected = { sticker ->
                            playFeedback()
                            onInsertText(" $sticker ")
                            activeTool = null
                        },
                        onClose = { activeTool = null }
                    )
                }
                QuickTool.CLIPBOARD -> {
                    ClipboardDrawer(
                        clips = clipboardItems,
                        theme = theme,
                        onClipSelected = { clipText ->
                            playFeedback()
                            onInsertText(clipText)
                            activeTool = null
                        },
                        onTogglePin = onTogglePinClip,
                        onDeleteClip = onDeleteClip,
                        onAddClip = onAddClip,
                        onClearAll = onClearClips,
                        onClose = { activeTool = null }
                    )
                }
                QuickTool.TEXT_EDIT -> {
                    TextEditingPad(
                        theme = theme,
                        onMoveCursorLeft = {
                            playFeedback()
                            onMoveCursor(-1)
                        },
                        onMoveCursorRight = {
                            playFeedback()
                            onMoveCursor(1)
                        },
                        onMoveCursorStart = {
                            playFeedback()
                            onMoveCursor(-1000)
                        },
                        onMoveCursorEnd = {
                            playFeedback()
                            onMoveCursor(1000)
                        },
                        onSelectAll = {
                            playFeedback()
                            onSelectAll()
                        },
                        onCut = {
                            playFeedback()
                            onCut()
                        },
                        onCopy = {
                            playFeedback()
                            onCopy()
                        },
                        onPaste = {
                            playFeedback()
                            onPaste()
                        },
                        onUndo = {
                            playFeedback()
                            onUndo()
                        },
                        onRedo = {
                            playFeedback()
                            onRedo()
                        },
                        onClose = { activeTool = null }
                    )
                }
                else -> {
                    // One-Handed Mode Layout Wrapper (Left or Right docked)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left-side dock controls if docked to Right
                        if (settings.typingMode == TypingMode.ONE_HANDED_RIGHT) {
                            Column(
                                modifier = Modifier
                                    .width(46.dp)
                                    .padding(horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(theme.keyColor)
                                        .clickable { onChangeTypingMode?.invoke(TypingMode.ONE_HANDED_LEFT) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Dock Left", tint = theme.accentColor, modifier = Modifier.size(18.dp))
                                }
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(theme.keyColor)
                                        .clickable { onChangeTypingMode?.invoke(TypingMode.STANDARD) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Fullscreen, "Expand Full", tint = theme.keyTextColor, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        // Main Typing Rows Column
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            verticalArrangement = Arrangement.spacedBy(1.dp)
                        ) {
                            // Optional top number row
                            if (settings.numberRowEnabled && currentLayout == KeyboardLayoutType.QWERTY) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    KeyboardLayouts.numberRow.forEach { key ->
                                        KeyButton(
                                            modifier = Modifier.weight(1f),
                                            text = key.mainText,
                                            theme = theme,
                                            onKeyTap = {
                                                playFeedback()
                                                onInsertText(key.mainText)
                                            }
                                        )
                                    }
                                }
                            }

                            // Layout Rows based on currentLayout
                            when (currentLayout) {
                                KeyboardLayoutType.HINDI -> {
                                    RenderRow(
                                        row = KeyboardLayouts.hindiRow1,
                                        theme = theme,
                                        isShifted = isShifted || isCapsLock,
                                        onCharTap = { char ->
                                            playFeedback()
                                            onInsertText(char)
                                        }
                                    )
                                    RenderRow(
                                        row = KeyboardLayouts.hindiRow2,
                                        theme = theme,
                                        isShifted = isShifted || isCapsLock,
                                        onCharTap = { char ->
                                            playFeedback()
                                            onInsertText(char)
                                        }
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        KeyButton(
                                            modifier = Modifier.weight(1.3f),
                                            text = "Shift",
                                            isSpecial = true,
                                            theme = theme,
                                            onKeyTap = {
                                                playFeedback()
                                                isShifted = !isShifted
                                            },
                                            customContent = {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowUpward,
                                                    contentDescription = "Shift",
                                                    tint = if (isShifted) theme.accentColor else theme.specialKeyTextColor
                                                )
                                            }
                                        )
                                        KeyboardLayouts.hindiRow3.filter { it.type == KeyType.CHARACTER }.forEach { key ->
                                            KeyButton(
                                                modifier = Modifier.weight(1f),
                                                text = key.mainText,
                                                theme = theme,
                                                onKeyTap = {
                                                    playFeedback()
                                                    onInsertText(key.mainText)
                                                }
                                            )
                                        }
                                        KeyButton(
                                            modifier = Modifier.weight(1.3f),
                                            text = "Delete",
                                            isSpecial = true,
                                            theme = theme,
                                            onKeyTap = {},
                                            onPointerDown = { smartBackspaceHandler.onPointerDown() },
                                            onPointerUp = { smartBackspaceHandler.onPointerUp() },
                                            customContent = {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                                                    contentDescription = "Backspace",
                                                    tint = theme.specialKeyTextColor
                                                )
                                            }
                                        )
                                    }
                                }
                                KeyboardLayoutType.NUMBERS -> {
                                    RenderRow(
                                        row = KeyboardLayouts.numbersRow1,
                                        theme = theme,
                                        isShifted = false,
                                        onCharTap = { playFeedback(); onInsertText(it) }
                                    )
                                    RenderRow(
                                        row = KeyboardLayouts.numbersRow2,
                                        theme = theme,
                                        isShifted = false,
                                        onCharTap = { playFeedback(); onInsertText(it) }
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        KeyButton(
                                            modifier = Modifier.weight(1.4f),
                                            text = "1/2",
                                            isSpecial = true,
                                            theme = theme,
                                            onKeyTap = {
                                                playFeedback()
                                                currentLayout = KeyboardLayoutType.SYMBOLS
                                            }
                                        )
                                        KeyboardLayouts.numbersRow3.filter { it.type == KeyType.CHARACTER }.forEach { key ->
                                            KeyButton(
                                                modifier = Modifier.weight(1f),
                                                text = key.mainText,
                                                theme = theme,
                                                onKeyTap = {
                                                    playFeedback()
                                                    onInsertText(key.mainText)
                                                }
                                            )
                                        }
                                        KeyButton(
                                            modifier = Modifier.weight(1.4f),
                                            text = "Delete",
                                            isSpecial = true,
                                            theme = theme,
                                            onKeyTap = {},
                                            onPointerDown = { smartBackspaceHandler.onPointerDown() },
                                            onPointerUp = { smartBackspaceHandler.onPointerUp() },
                                            customContent = {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                                                    contentDescription = "Backspace",
                                                    tint = theme.specialKeyTextColor
                                                )
                                            }
                                        )
                                    }
                                }
                                KeyboardLayoutType.SYMBOLS -> {
                                    RenderRow(
                                        row = KeyboardLayouts.symbolsRow1,
                                        theme = theme,
                                        isShifted = false,
                                        onCharTap = { playFeedback(); onInsertText(it) }
                                    )
                                    RenderRow(
                                        row = KeyboardLayouts.symbolsRow2,
                                        theme = theme,
                                        isShifted = false,
                                        onCharTap = { playFeedback(); onInsertText(it) }
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        KeyButton(
                                            modifier = Modifier.weight(1.4f),
                                            text = "?123",
                                            isSpecial = true,
                                            theme = theme,
                                            onKeyTap = {
                                                playFeedback()
                                                currentLayout = KeyboardLayoutType.NUMBERS
                                            }
                                        )
                                        KeyboardLayouts.symbolsRow3.filter { it.type == KeyType.CHARACTER }.forEach { key ->
                                            KeyButton(
                                                modifier = Modifier.weight(1f),
                                                text = key.mainText,
                                                theme = theme,
                                                onKeyTap = {
                                                    playFeedback()
                                                    onInsertText(key.mainText)
                                                }
                                            )
                                        }
                                        KeyButton(
                                            modifier = Modifier.weight(1.4f),
                                            text = "Delete",
                                            isSpecial = true,
                                            theme = theme,
                                            onKeyTap = {},
                                            onPointerDown = { smartBackspaceHandler.onPointerDown() },
                                            onPointerUp = { smartBackspaceHandler.onPointerUp() },
                                            customContent = {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                                                    contentDescription = "Backspace",
                                                    tint = theme.specialKeyTextColor
                                                )
                                            }
                                        )
                                    }
                                }
                                else -> {
                                    // Standard QWERTY
                                    RenderRow(
                                        row = KeyboardLayouts.qwertyRow1,
                                        theme = theme,
                                        isShifted = isShifted || isCapsLock,
                                        onCharTap = { char ->
                                            playFeedback()
                                            onInsertText(char)
                                            if (isShifted && !isCapsLock) isShifted = false
                                        },
                                        onCharLongPress = { char ->
                                            KeyboardLayouts.accentsMap[char]?.let {
                                                longPressAccents = it
                                            }
                                        }
                                    )
                                    RenderRow(
                                        row = KeyboardLayouts.qwertyRow2,
                                        theme = theme,
                                        isShifted = isShifted || isCapsLock,
                                        onCharTap = { char ->
                                            playFeedback()
                                            onInsertText(char)
                                            if (isShifted && !isCapsLock) isShifted = false
                                        },
                                        onCharLongPress = { char ->
                                            KeyboardLayouts.accentsMap[char]?.let {
                                                longPressAccents = it
                                            }
                                        }
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        KeyButton(
                                            modifier = Modifier.weight(1.3f),
                                            text = "Shift",
                                            isSpecial = true,
                                            theme = theme,
                                            onKeyTap = {
                                                playFeedback()
                                                isShifted = !isShifted
                                            },
                                            onLongPress = {
                                                playFeedback()
                                                isCapsLock = !isCapsLock
                                                isShifted = isCapsLock
                                            },
                                            customContent = {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowUpward,
                                                    contentDescription = "Shift",
                                                    tint = if (isCapsLock || isShifted) theme.accentColor else theme.specialKeyTextColor
                                                )
                                            }
                                        )
                                        KeyboardLayouts.qwertyRow3.filter { it.type == KeyType.CHARACTER }.forEach { key ->
                                            val char = if (isShifted || isCapsLock) key.mainText.uppercase() else key.mainText.lowercase()
                                            KeyButton(
                                                modifier = Modifier.weight(1f),
                                                text = char,
                                                secondaryText = if (settings.longPressSymbols) key.secondaryText else null,
                                                theme = theme,
                                                onKeyTap = {
                                                    playFeedback()
                                                    onInsertText(char)
                                                    if (isShifted && !isCapsLock) isShifted = false
                                                },
                                                onLongPress = {
                                                    if (KeyboardLayouts.accentsMap.containsKey(char)) {
                                                        longPressAccents = KeyboardLayouts.accentsMap[char]
                                                    } else if (key.secondaryText != null) {
                                                        playFeedback()
                                                        onInsertText(key.secondaryText)
                                                    }
                                                }
                                            )
                                        }
                                        KeyButton(
                                            modifier = Modifier.weight(1.3f),
                                            text = "Delete",
                                            isSpecial = true,
                                            theme = theme,
                                            onKeyTap = {},
                                            onPointerDown = { smartBackspaceHandler.onPointerDown() },
                                            onPointerUp = { smartBackspaceHandler.onPointerUp() },
                                            customContent = {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                                                    contentDescription = "Backspace",
                                                    tint = theme.specialKeyTextColor
                                                )
                                            }
                                        )
                                    }
                                }
                            }

                            // Bottom Row: [?123 / ABC] [Lang Switch] [Spacebar (slide to move cursor)] [.] [Enter]
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                KeyButton(
                                    modifier = Modifier.weight(1.3f),
                                    text = if (currentLayout == KeyboardLayoutType.QWERTY || currentLayout == KeyboardLayoutType.HINDI) "?123" else "ABC",
                                    isSpecial = true,
                                    theme = theme,
                                    onKeyTap = {
                                        playFeedback()
                                        currentLayout = if (currentLayout == KeyboardLayoutType.QWERTY || currentLayout == KeyboardLayoutType.HINDI) {
                                            KeyboardLayoutType.NUMBERS
                                        } else {
                                            KeyboardLayoutType.QWERTY
                                        }
                                    }
                                )

                                KeyButton(
                                    modifier = Modifier.weight(1.1f),
                                    text = "Lang",
                                    isSpecial = true,
                                    theme = theme,
                                    onKeyTap = {
                                        playFeedback()
                                        currentLayout = if (currentLayout == KeyboardLayoutType.HINDI) {
                                            KeyboardLayoutType.QWERTY
                                        } else {
                                            KeyboardLayoutType.HINDI
                                        }
                                    },
                                    customContent = {
                                        Icon(
                                            imageVector = Icons.Default.Language,
                                            contentDescription = "Switch language",
                                            tint = if (currentLayout == KeyboardLayoutType.HINDI) theme.accentColor else theme.specialKeyTextColor
                                        )
                                    }
                                )

                                var dragAccumulator by remember { mutableStateOf(0f) }
                                KeyButton(
                                    modifier = Modifier
                                        .weight(4.4f)
                                        .pointerInput(Unit) {
                                            detectDragGestures(
                                                onDragStart = { dragAccumulator = 0f },
                                                onDrag = { change, dragAmount ->
                                                    change.consume()
                                                    dragAccumulator += dragAmount.x
                                                    if (dragAccumulator > 28f) {
                                                        playFeedback()
                                                        onMoveCursor(1)
                                                        dragAccumulator = 0f
                                                    } else if (dragAccumulator < -28f) {
                                                        playFeedback()
                                                        onMoveCursor(-1)
                                                        dragAccumulator = 0f
                                                    }
                                                }
                                            )
                                        },
                                    text = if (currentLayout == KeyboardLayoutType.HINDI) "हिन्दी" else "English",
                                    isSpecial = false,
                                    theme = theme,
                                    onKeyTap = {
                                        playFeedback()
                                        onInsertText(" ")
                                    }
                                )

                                KeyButton(
                                    modifier = Modifier.weight(1.1f),
                                    text = if (currentLayout == KeyboardLayoutType.HINDI) "।" else ".",
                                    theme = theme,
                                    onKeyTap = {
                                        playFeedback()
                                        onInsertText(if (currentLayout == KeyboardLayoutType.HINDI) "।" else ".")
                                    }
                                )

                                KeyButton(
                                    modifier = Modifier.weight(1.3f),
                                    text = "Enter",
                                    isSpecial = true,
                                    theme = theme,
                                    onKeyTap = {
                                        playFeedback()
                                        onInsertText("\n")
                                    },
                                    customContent = {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardReturn,
                                            contentDescription = "Enter",
                                            tint = theme.accentColor
                                        )
                                    }
                                )
                            }
                        }

                        // Right-side dock controls if docked to Left
                        if (settings.typingMode == TypingMode.ONE_HANDED_LEFT) {
                            Column(
                                modifier = Modifier
                                    .width(46.dp)
                                    .padding(horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(theme.keyColor)
                                        .clickable { onChangeTypingMode?.invoke(TypingMode.ONE_HANDED_RIGHT) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "Dock Right", tint = theme.accentColor, modifier = Modifier.size(18.dp))
                                }
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(theme.keyColor)
                                        .clickable { onChangeTypingMode?.invoke(TypingMode.STANDARD) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Fullscreen, "Expand Full", tint = theme.keyTextColor, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Long Press Accents Overlay
        longPressAccents?.let { accents ->
            LongPressKeyPopup(
                items = accents,
                theme = theme,
                onItemSelected = { selectedChar ->
                    playFeedback()
                    onInsertText(selectedChar)
                },
                onDismiss = { longPressAccents = null }
            )
        }
    }

    // Voice typing dialog
    if (showVoiceDialog) {
        VoiceTypingDialog(
            theme = theme,
            onTextRecognized = { text ->
                onInsertText(text)
            },
            onDismiss = { showVoiceDialog = false }
        )
    }
}

@Composable
private fun QuickSwitchPill(
    text: String,
    selected: Boolean,
    theme: KeyboardTheme,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (selected) theme.accentColor.copy(alpha = 0.25f) else theme.keyColor.copy(alpha = 0.4f))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) theme.accentColor else theme.keyTextColor.copy(alpha = 0.6f),
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun RenderRow(
    row: List<KeyModel>,
    theme: KeyboardTheme,
    isShifted: Boolean,
    onCharTap: (String) -> Unit,
    onCharLongPress: ((String) -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        row.forEach { key ->
            val char = if (isShifted) key.mainText.uppercase() else key.mainText.lowercase()
            KeyButton(
                modifier = Modifier.weight(key.weight),
                text = char,
                secondaryText = key.secondaryText,
                theme = theme,
                onKeyTap = { onCharTap(char) },
                onLongPress = {
                    if (onCharLongPress != null) {
                        onCharLongPress(char)
                    } else if (key.secondaryText != null) {
                        onCharTap(key.secondaryText)
                    }
                }
            )
        }
    }
}
