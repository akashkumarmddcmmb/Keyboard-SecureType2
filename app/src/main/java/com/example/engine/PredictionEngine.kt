package com.example.engine

class PredictionEngine {

    private val commonWords = listOf(
        "the", "and", "hello", "how", "happy", "have", "here", "help", "home", "hope",
        "you", "your", "welcome", "secure", "security", "privacy", "keyboard", "offline",
        "typing", "fast", "smart", "thank", "thanks", "there", "today", "tomorrow",
        "great", "good", "morning", "night", "awesome", "perfect", "please", "quick",
        "message", "notes", "secret", "phone", "friend", "love", "beautiful", "smooth",
        "bharat", "namaste", "shukriya", "dost", "kaise", "theek", "achha"
    )

    private val nextWordMap = mapOf(
        "hello" to listOf("there", "how are you", "world", "friend"),
        "how" to listOf("are you", "is it going", "to", "do you"),
        "thank" to listOf("you", "you so much", "god", "for everything"),
        "thanks" to listOf("a lot", "for your help", "again"),
        "good" to listOf("morning", "evening", "night", "job", "idea"),
        "securetype" to listOf("Keyboard", "is super fast", "protects privacy"),
        "privacy" to listOf("matters", "first", "guaranteed", "mode"),
        "have" to listOf("a great day", "a nice weekend", "fun", "been"),
        "see" to listOf("you soon", "you later", "what happens"),
        "nice" to listOf("to meet you", "work", "job", "picture")
    )

    private val defaultShortcuts = mapOf(
        "ty" to "Thank you so much!",
        "omw" to "On my way!",
        "brb" to "Be right back",
        "st" to "SecureType Keyboard",
        "np" to "No problem!",
        "idk" to "I don't know",
        "btw" to "By the way",
        "gm" to "Good morning!"
    )

    fun getSuggestions(text: String, languageCode: String = "en"): List<String> {
        val trimmed = text.trimEnd()
        if (trimmed.isEmpty()) {
            return listOf("Hello", "How", "Happy", "SecureType")
        }

        // Get the last word token being typed
        val lastWord = text.split("\\s+".toRegex()).lastOrNull()?.lowercase() ?: ""

        // Check if there is an exact shortcut match
        defaultShortcuts[lastWord]?.let { shortcutExpansion ->
            return listOf(shortcutExpansion, lastWord.replaceFirstChar { it.uppercase() })
        }

        if (text.endsWith(" ")) {
            // User just finished a word -> next word prediction
            val previousWord = lastWord.lowercase()
            nextWordMap[previousWord]?.let { return it.take(4) }
            return listOf("the", "and", "to", "you")
        }

        if (lastWord.isEmpty()) {
            return listOf("Hello", "How", "Happy", "Awesome")
        }

        // Prefix match in common words
        val matches = commonWords
            .filter { it.startsWith(lastWord) && it != lastWord }
            .sortedBy { it.length }
            .take(4)

        if (matches.isNotEmpty()) {
            return matches.map { word ->
                if (lastWord.firstOrNull()?.isUpperCase() == true) {
                    word.replaceFirstChar { it.uppercase() }
                } else {
                    word
                }
            }
        }

        // Transliteration hints for Indian languages
        if (languageCode == "hi" || lastWord.startsWith("nam") || lastWord.startsWith("aap")) {
            return when {
                lastWord.startsWith("nam") -> listOf("नमस्ते", "नमस्कार", "नाम")
                lastWord.startsWith("aap") -> listOf("आप", "आपका", "आपकी")
                lastWord.startsWith("k") -> listOf("कैसे", "क्या", "कब")
                else -> listOf(lastWord, "${lastWord}ing", "${lastWord}ed")
            }
        }

        return listOf(
            lastWord.replaceFirstChar { it.uppercase() },
            "${lastWord}s",
            "${lastWord}ing",
            "awesome"
        ).distinct()
    }

    fun checkShortcut(trigger: String): String? {
        return defaultShortcuts[trigger.lowercase()]
    }
}
