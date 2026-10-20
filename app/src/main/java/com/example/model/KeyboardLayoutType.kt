package com.example.model

enum class KeyboardLayoutType {
    QWERTY,
    HINDI,
    NUMBERS,
    SYMBOLS,
    EMOJI
}

enum class KeySoundType(val displayName: String) {
    CLICK("Classic Click"),
    MECHANICAL("Mechanical"),
    SOFT("Soft Tap"),
    POP("Bubble Pop"),
    NONE("Silent")
}

data class Language(
    val code: String,
    val name: String,
    val nativeName: String,
    val flag: String,
    val sampleText: String
) {
    companion object {
        val English = Language("en", "English", "English", "🇺🇸", "Hello, how are you?")
        val Hindi = Language("hi", "Hindi", "हिन्दी", "🇮🇳", "नमस्ते, आप कैसे हैं?")
        val Marathi = Language("mr", "Marathi", "मराठी", "🇮🇳", "नमस्कार, तुम्ही कसे आहात?")
        val Gujarati = Language("gu", "Gujarati", "ગુજરાતી", "🇮🇳", "નમસ્તે, કેમ છો?")
        val Tamil = Language("ta", "Tamil", "தமிழ்", "🇮🇳", "வணக்கம், எப்படி இருக்கிறீர்கள்?")
        val Bengali = Language("bn", "Bengali", "বাংলা", "🇮🇳", "নমস্কার, আপনি কেমন আছেন?")
        val Telugu = Language("te", "Telugu", "తెలుగు", "🇮🇳", "నమస్కారం, ఎలా ఉన్నారు?")

        val allLanguages = listOf(English, Hindi, Marathi, Gujarati, Tamil, Bengali, Telugu)
    }
}

enum class KeyType {
    CHARACTER,
    SHIFT,
    BACKSPACE,
    ENTER,
    SWITCH_LAYOUT,
    EMOJI,
    SPACE,
    VOICE,
    PERIOD,
    COMMA,
    SETTINGS
}

data class KeyModel(
    val mainText: String,
    val secondaryText: String? = null,
    val type: KeyType = KeyType.CHARACTER,
    val weight: Float = 1f,
    val iconRes: String? = null
)
