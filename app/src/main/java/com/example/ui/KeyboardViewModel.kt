package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.data.ClipboardRepository
import com.example.model.ClipboardItem
import com.example.model.KeySoundType
import com.example.model.KeyboardHeight
import com.example.model.KeyboardSettings
import com.example.model.KeyboardTheme
import com.example.model.Language
import com.example.model.TextShortcut
import com.example.model.TypingMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class KeyboardViewModel(application: Application) : AndroidViewModel(application) {

    private val clipboardRepo = ClipboardRepository(application)

    private val _settings = MutableStateFlow(KeyboardSettings())
    val settings: StateFlow<KeyboardSettings> = _settings.asStateFlow()

    private val _currentTheme = MutableStateFlow(KeyboardTheme.DarkBlue)
    val currentTheme: StateFlow<KeyboardTheme> = _currentTheme.asStateFlow()

    private val _activeLanguage = MutableStateFlow(Language.English)
    val activeLanguage: StateFlow<Language> = _activeLanguage.asStateFlow()

    private val _playgroundText = MutableStateFlow("Hello SecureType! ")
    val playgroundText: StateFlow<String> = _playgroundText.asStateFlow()

    private val _cursorPosition = MutableStateFlow(17)
    val cursorPosition: StateFlow<Int> = _cursorPosition.asStateFlow()

    // Undo & Redo History stacks (Image 3: Text Editing Tools)
    private val undoStack = mutableListOf<String>()
    private val redoStack = mutableListOf<String>()

    // Smart Backspace demo text
    private val _backspaceDemoText = MutableStateFlow(
        "Hold the backspace button to test the ultra-fast 25ms speed! Instant first delete, gradual acceleration, and zero lag."
    )
    val backspaceDemoText: StateFlow<String> = _backspaceDemoText.asStateFlow()

    val clipboardClips: StateFlow<List<ClipboardItem>> = clipboardRepo.clips

    private val _shortcuts = MutableStateFlow(
        listOf(
            TextShortcut(trigger = "ty", replacement = "Thank you so much!"),
            TextShortcut(trigger = "omw", replacement = "On my way!"),
            TextShortcut(trigger = "brb", replacement = "Be right back"),
            TextShortcut(trigger = "st", replacement = "SecureType Keyboard")
        )
    )
    val shortcuts: StateFlow<List<TextShortcut>> = _shortcuts.asStateFlow()

    fun updateTheme(theme: KeyboardTheme) {
        _currentTheme.value = theme
        _settings.update { it.copy(themeId = theme.id) }
    }

    fun updateLanguage(language: Language) {
        _activeLanguage.value = language
        _settings.update { it.copy(selectedLanguageCode = language.code) }
    }

    fun insertText(newText: String) {
        val current = _playgroundText.value
        undoStack.add(current)
        redoStack.clear()
        val pos = _cursorPosition.value.coerceIn(0, current.length)
        val updated = current.substring(0, pos) + newText + current.substring(pos)
        _playgroundText.value = updated
        _cursorPosition.value = pos + newText.length
    }

    fun deleteText() {
        val current = _playgroundText.value
        val pos = _cursorPosition.value.coerceIn(0, current.length)
        if (pos > 0 && current.isNotEmpty()) {
            undoStack.add(current)
            redoStack.clear()
            val updated = current.substring(0, pos - 1) + current.substring(pos)
            _playgroundText.value = updated
            _cursorPosition.value = pos - 1
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.add(_playgroundText.value)
            val previous = undoStack.removeAt(undoStack.lastIndex)
            _playgroundText.value = previous
            _cursorPosition.value = previous.length
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.add(_playgroundText.value)
            val next = redoStack.removeAt(redoStack.lastIndex)
            _playgroundText.value = next
            _cursorPosition.value = next.length
        }
    }

    fun moveCursor(offset: Int) {
        val current = _playgroundText.value
        val newPos = (_cursorPosition.value + offset).coerceIn(0, current.length)
        _cursorPosition.value = newPos
    }

    fun clearPlayground() {
        undoStack.add(_playgroundText.value)
        _playgroundText.value = ""
        _cursorPosition.value = 0
    }

    // Smart Backspace specific demo controls
    fun deleteDemoText() {
        if (_backspaceDemoText.value.isNotEmpty()) {
            _backspaceDemoText.value = _backspaceDemoText.value.dropLast(1)
        }
    }

    fun resetDemoText() {
        _backspaceDemoText.value =
            "Hold the backspace button to test the ultra-fast 25ms speed! Instant first delete, gradual acceleration, and zero lag."
    }

    // Modes & Layout Heights
    fun setKeyboardHeight(height: KeyboardHeight) {
        _settings.update { it.copy(keyboardHeight = height) }
    }

    fun setTypingMode(mode: TypingMode) {
        _settings.update { it.copy(typingMode = mode) }
    }

    fun toggleFollowSystemTheme(enabled: Boolean) {
        _settings.update { it.copy(autoThemeFollowSystem = enabled) }
    }

    // Settings modifiers
    fun toggleSound(enabled: Boolean) {
        _settings.update {
            it.copy(keySoundType = if (enabled) KeySoundType.CLICK else KeySoundType.NONE)
        }
    }

    fun setSoundType(type: KeySoundType) {
        _settings.update { it.copy(keySoundType = type) }
    }

    fun setSoundVolume(volume: Float) {
        _settings.update { it.copy(soundVolume = volume) }
    }

    fun toggleVibration(enabled: Boolean) {
        _settings.update { it.copy(vibrationEnabled = enabled) }
    }

    fun setVibrationStrength(strength: Int) {
        _settings.update { it.copy(vibrationStrength = strength) }
    }

    fun toggleSmartBackspace(enabled: Boolean) {
        _settings.update { it.copy(smartBackspaceEnabled = enabled) }
    }

    fun setSmartBackspaceSpeed(speedMs: Long) {
        _settings.update { it.copy(smartBackspaceRepeatMs = speedMs) }
    }

    fun toggleNumberRow(enabled: Boolean) {
        _settings.update { it.copy(numberRowEnabled = enabled) }
    }

    fun togglePrediction(enabled: Boolean) {
        _settings.update { it.copy(predictionEnabled = enabled) }
    }

    fun togglePrivacyMode(enabled: Boolean) {
        _settings.update { it.copy(privacyMode = enabled) }
    }

    fun addCustomDictionaryWord(word: String) {
        if (word.isNotBlank()) {
            _settings.update {
                it.copy(customDictionary = (it.customDictionary + word.trim()).distinct())
            }
        }
    }

    // Clipboard ops
    fun addClip(text: String) = clipboardRepo.addClip(text)
    fun togglePinClip(id: String) = clipboardRepo.togglePin(id)
    fun deleteClip(id: String) = clipboardRepo.deleteClip(id)
    fun clearAllClips() = clipboardRepo.clearAll()

    // Shortcut ops
    fun addShortcut(trigger: String, replacement: String) {
        if (trigger.isNotBlank() && replacement.isNotBlank()) {
            _shortcuts.update {
                it + TextShortcut(trigger = trigger.trim(), replacement = replacement.trim())
            }
        }
    }

    fun removeShortcut(id: String) {
        _shortcuts.update { it.filterNot { item -> item.id == id } }
    }
}
