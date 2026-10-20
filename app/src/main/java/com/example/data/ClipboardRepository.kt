package com.example.data

import android.content.Context
import com.example.model.ClipboardItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClipboardRepository(context: Context) {
    private val prefs = context.getSharedPreferences("securetype_clipboard", Context.MODE_PRIVATE)

    private val _clips = MutableStateFlow<List<ClipboardItem>>(
        listOf(
            ClipboardItem(
                id = "clip_1",
                text = "SecureType ensures 100% privacy & zero data leaks.",
                isPinned = true
            ),
            ClipboardItem(
                id = "clip_2",
                text = "Looking forward to our sync at 4:00 PM!",
                isPinned = true
            ),
            ClipboardItem(
                id = "clip_3",
                text = "akashkumarmddcmmb@gmail.com",
                isPinned = false
            ),
            ClipboardItem(
                id = "clip_4",
                text = "https://github.com/securetype/keyboard",
                isPinned = false
            )
        )
    )
    val clips: StateFlow<List<ClipboardItem>> = _clips.asStateFlow()

    fun addClip(text: String) {
        if (text.isBlank()) return
        val current = _clips.value.toMutableList()
        current.removeAll { it.text == text }
        current.add(0, ClipboardItem(text = text))
        _clips.value = current.take(30)
    }

    fun togglePin(id: String) {
        _clips.value = _clips.value.map {
            if (it.id == id) it.copy(isPinned = !it.isPinned) else it
        }
    }

    fun deleteClip(id: String) {
        _clips.value = _clips.value.filterNot { it.id == id }
    }

    fun clearAll() {
        _clips.value = emptyList()
        prefs.edit().clear().apply()
    }
}
