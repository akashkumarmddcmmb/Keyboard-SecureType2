package com.example.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class KeyboardTheme(
    val id: String,
    val name: String,
    val description: String,
    val backgroundColor: Color,
    val keyboardBackgroundColor: Color,
    val keyColor: Color,
    val keyTextColor: Color,
    val specialKeyColor: Color,
    val specialKeyTextColor: Color,
    val accentColor: Color,
    val previewGradient: List<Color>,
    val backgroundBrush: Brush? = null
) {
    companion object {
        val DarkBlue = KeyboardTheme(
            id = "dark_blue",
            name = "Dark Blue",
            description = "Deep navy tech with electric cyan glow",
            backgroundColor = Color(0xFF070E1E),
            keyboardBackgroundColor = Color(0xFF0D1B2A),
            keyColor = Color(0xFF1E2D4A),
            keyTextColor = Color(0xFFE2E8F0),
            specialKeyColor = Color(0xFF283B61),
            specialKeyTextColor = Color(0xFF00E5FF),
            accentColor = Color(0xFF00E5FF),
            previewGradient = listOf(Color(0xFF0D1B2A), Color(0xFF1E2D4A), Color(0xFF00E5FF))
        )

        val PurpleNeon = KeyboardTheme(
            id = "purple_neon",
            name = "Purple Glow",
            description = "Cyberpunk dark violet with vivid pink glow",
            backgroundColor = Color(0xFF130924),
            keyboardBackgroundColor = Color(0xFF1A0C33),
            keyColor = Color(0xFF2E1757),
            keyTextColor = Color(0xFFF3E8FF),
            specialKeyColor = Color(0xFF481C7E),
            specialKeyTextColor = Color(0xFFF43F5E),
            accentColor = Color(0xFFD946EF),
            previewGradient = listOf(Color(0xFF1A0C33), Color(0xFF481C7E), Color(0xFFD946EF))
        )

        val GreenMatrix = KeyboardTheme(
            id = "green_matrix",
            name = "Green Matrix",
            description = "Terminal hacker vibe with phosphor green keys",
            backgroundColor = Color(0xFF041009),
            keyboardBackgroundColor = Color(0xFF081C10),
            keyColor = Color(0xFF113821),
            keyTextColor = Color(0xFFE6F4EA),
            specialKeyColor = Color(0xFF1A5231),
            specialKeyTextColor = Color(0xFF00FF66),
            accentColor = Color(0xFF00FF66),
            previewGradient = listOf(Color(0xFF081C10), Color(0xFF1A5231), Color(0xFF00FF66))
        )

        val LightMode = KeyboardTheme(
            id = "light_mode",
            name = "Modern Light",
            description = "Bright, clean and easy on your eyes",
            backgroundColor = Color(0xFFF1F5F9),
            keyboardBackgroundColor = Color(0xFFE2E8F0),
            keyColor = Color(0xFFFFFFFF),
            keyTextColor = Color(0xFF0F172A),
            specialKeyColor = Color(0xFFCBD5E1),
            specialKeyTextColor = Color(0xFF2563EB),
            accentColor = Color(0xFF2563EB),
            previewGradient = listOf(Color(0xFFF8FAFC), Color(0xFFFFFFFF), Color(0xFF2563EB))
        )

        val Sunset = KeyboardTheme(
            id = "sunset",
            name = "Sunset",
            description = "Warm dusk gradient with glowing coral and amber",
            backgroundColor = Color(0xFF1F1117),
            keyboardBackgroundColor = Color(0xFF2B1621),
            keyColor = Color(0xFF421E31),
            keyTextColor = Color(0xFFFFF1F2),
            specialKeyColor = Color(0xFF5E2744),
            specialKeyTextColor = Color(0xFFFF7A59),
            accentColor = Color(0xFFFF6B4A),
            previewGradient = listOf(Color(0xFF2B1621), Color(0xFF5E2744), Color(0xFFFF6B4A))
        )

        val Ocean = KeyboardTheme(
            id = "ocean",
            name = "Ocean Teal",
            description = "Deep marine waters with vibrant seafoam azure",
            backgroundColor = Color(0xFF041824),
            keyboardBackgroundColor = Color(0xFF0A253A),
            keyColor = Color(0xFF133E5E),
            keyTextColor = Color(0xFFE0F2FE),
            specialKeyColor = Color(0xFF1B537E),
            specialKeyTextColor = Color(0xFF38BDF8),
            accentColor = Color(0xFF38BDF8),
            previewGradient = listOf(Color(0xFF0A253A), Color(0xFF133E5E), Color(0xFF38BDF8))
        )

        val NeonBlue = KeyboardTheme(
            id = "neon_blue",
            name = "Neon Blue",
            description = "High-voltage tech blue with glowing electric accents",
            backgroundColor = Color(0xFF030D22),
            keyboardBackgroundColor = Color(0xFF08183A),
            keyColor = Color(0xFF102D6E),
            keyTextColor = Color(0xFFEFF6FF),
            specialKeyColor = Color(0xFF1D4ED8),
            specialKeyTextColor = Color(0xFF60A5FA),
            accentColor = Color(0xFF3B82F6),
            previewGradient = listOf(Color(0xFF08183A), Color(0xFF102D6E), Color(0xFF3B82F6))
        )

        val Minimal = KeyboardTheme(
            id = "minimal",
            name = "Dark Minimal",
            description = "Monochrome matte black with pure white precision",
            backgroundColor = Color(0xFF09090B),
            keyboardBackgroundColor = Color(0xFF121214),
            keyColor = Color(0xFF222226),
            keyTextColor = Color(0xFFFAFAFA),
            specialKeyColor = Color(0xFF2E2E33),
            specialKeyTextColor = Color(0xFFFFFFFF),
            accentColor = Color(0xFFFFFFFF),
            previewGradient = listOf(Color(0xFF121214), Color(0xFF222226), Color(0xFFFFFFFF))
        )

        val WallpaperSunset = KeyboardTheme(
            id = "wallpaper_sunset",
            name = "Sunset Wallpaper",
            description = "Dynamic wallpaper background with glowing dusk gradients",
            backgroundColor = Color(0xFF150B1E),
            keyboardBackgroundColor = Color(0xCC1A0C2E),
            keyColor = Color(0x803B1D54),
            keyTextColor = Color(0xFFFFF1F2),
            specialKeyColor = Color(0x995B2282),
            specialKeyTextColor = Color(0xFFF472B6),
            accentColor = Color(0xFFFB7185),
            previewGradient = listOf(Color(0xFFE11D48), Color(0xFF9333EA), Color(0xFF3B82F6)),
            backgroundBrush = Brush.verticalGradient(
                listOf(Color(0xFF831843), Color(0xFF581C87), Color(0xFF1E1B4B))
            )
        )

        val WallpaperAurora = KeyboardTheme(
            id = "wallpaper_aurora",
            name = "Aurora Glow",
            description = "Dynamic northern lights wallpaper with emerald and cyan",
            backgroundColor = Color(0xFF041315),
            keyboardBackgroundColor = Color(0xCC062326),
            keyColor = Color(0x800C4A4D),
            keyTextColor = Color(0xFFF0FDF4),
            specialKeyColor = Color(0x99115E59),
            specialKeyTextColor = Color(0xFF2DD4BF),
            accentColor = Color(0xFF34D399),
            previewGradient = listOf(Color(0xFF059669), Color(0xFF0D9488), Color(0xFF0284C7)),
            backgroundBrush = Brush.linearGradient(
                listOf(Color(0xFF064E3B), Color(0xFF134E4A), Color(0xFF0C4A6E))
            )
        )

        val allThemes = listOf(
            DarkBlue, PurpleNeon, GreenMatrix, LightMode, Sunset, Ocean, NeonBlue, Minimal,
            WallpaperSunset, WallpaperAurora
        )

        fun fromId(id: String): KeyboardTheme {
            return allThemes.find { it.id == id } ?: DarkBlue
        }
    }
}
