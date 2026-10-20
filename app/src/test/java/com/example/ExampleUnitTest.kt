package com.example

import com.example.engine.PredictionEngine
import com.example.model.KeyboardLayouts
import com.example.model.KeyboardTheme
import com.example.model.Language
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testKeyboardThemesCountAndProperties() {
        val themes = KeyboardTheme.allThemes
        assertTrue(themes.isNotEmpty())
        assertEquals("Dark Blue", KeyboardTheme.DarkBlue.name)
        assertEquals("dark_blue", KeyboardTheme.DarkBlue.id)

        // Verify key themes from infographic
        assertNotNull(KeyboardTheme.fromId("purple_neon"))
        assertNotNull(KeyboardTheme.fromId("green_matrix"))
        assertNotNull(KeyboardTheme.fromId("light_mode"))
        assertNotNull(KeyboardTheme.fromId("sunset"))
        assertNotNull(KeyboardTheme.fromId("ocean"))
        assertNotNull(KeyboardTheme.fromId("neon_blue"))
        assertNotNull(KeyboardTheme.fromId("minimal"))
    }

    @Test
    fun testPredictionEngineSuggestions() {
        val engine = PredictionEngine()
        val emptySuggestions = engine.getSuggestions("")
        assertTrue(emptySuggestions.isNotEmpty())

        val prefixSuggestions = engine.getSuggestions("hel")
        assertTrue(prefixSuggestions.any { it.contains("hello", ignoreCase = true) })

        // Shortcut expansion test: "ty" -> "Thank you so much!"
        val shortcutSuggestions = engine.getSuggestions("ty")
        assertTrue(shortcutSuggestions.contains("Thank you so much!"))
    }

    @Test
    fun testKeyboardLayoutsAvailability() {
        // QWERTY rows check
        assertTrue(KeyboardLayouts.qwertyRow1.isNotEmpty())
        assertTrue(KeyboardLayouts.qwertyRow2.isNotEmpty())
        assertTrue(KeyboardLayouts.qwertyRow3.isNotEmpty())

        // Row 1 should start with Q, W, E, R, T, Y
        assertEquals("Q", KeyboardLayouts.qwertyRow1[0].mainText)
        assertEquals("W", KeyboardLayouts.qwertyRow1[1].mainText)

        // Hindi rows should be defined with vowels and consonants
        assertTrue(KeyboardLayouts.hindiRow1.isNotEmpty())
        assertTrue(KeyboardLayouts.hindiRow2.isNotEmpty())
        assertTrue(KeyboardLayouts.hindiRow3.isNotEmpty())

        // Number row (0-9)
        assertEquals(10, KeyboardLayouts.numberRow.size)
        assertEquals("1", KeyboardLayouts.numberRow[0].mainText)
        assertEquals("0", KeyboardLayouts.numberRow[9].mainText)

        // Currency symbol Indian Rupee ₹ from Image 2
        assertTrue(KeyboardLayouts.numbersRow2.any { it.mainText == "₹" })
    }

    @Test
    fun testLanguagesList() {
        val languages = Language.allLanguages
        assertTrue(languages.any { it.code == "en" })
        assertTrue(languages.any { it.code == "hi" })
        assertEquals("हिन्दी", Language.Hindi.nativeName)
    }
}
