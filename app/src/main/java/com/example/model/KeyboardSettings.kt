package com.example.model

enum class KeyboardHeight(val displayName: String, val keyHeightDp: Int) {
    SHORT("Short", 40),
    MEDIUM("Medium", 46),
    TALL("Tall", 52)
}

enum class TypingMode(val displayName: String) {
    STANDARD("Standard (Full Width)"),
    ONE_HANDED_LEFT("One-Handed (Left)"),
    ONE_HANDED_RIGHT("One-Handed (Right)"),
    FLOATING("Floating Keyboard")
}

data class KeyboardSettings(
    val themeId: String = "dark_blue",
    val selectedLanguageCode: String = "en",
    val keySoundType: KeySoundType = KeySoundType.CLICK,
    val soundVolume: Float = 0.8f,
    val vibrationEnabled: Boolean = true,
    val vibrationStrength: Int = 35, // ms
    val smartBackspaceEnabled: Boolean = true,
    val smartBackspaceRepeatMs: Long = 25L,
    val numberRowEnabled: Boolean = true,
    val autoCapitalization: Boolean = true,
    val predictionEnabled: Boolean = true,
    val privacyMode: Boolean = true,
    val longPressSymbols: Boolean = true,
    val keyboardHeight: KeyboardHeight = KeyboardHeight.MEDIUM,
    val typingMode: TypingMode = TypingMode.STANDARD,
    val autoThemeFollowSystem: Boolean = false,
    val customDictionary: List<String> = listOf("SecureType", "CyberShield", "Encrypt")
)

data class ClipboardItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val isHistory: Boolean = false
)

data class TextShortcut(
    val id: String = java.util.UUID.randomUUID().toString(),
    val trigger: String,
    val replacement: String
)
