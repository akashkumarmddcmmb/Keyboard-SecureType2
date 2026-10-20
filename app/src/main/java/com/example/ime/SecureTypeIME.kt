package com.example.ime

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.data.ClipboardRepository
import com.example.model.KeyboardSettings
import com.example.model.KeyboardTheme
import com.example.ui.keyboard.KeyboardHost

class SecureTypeIME : InputMethodService(), LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val store = ViewModelStore()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val viewModelStore: ViewModelStore get() = store
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    private var currentTextState by mutableStateOf("")
    private lateinit var clipboardRepo: ClipboardRepository

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        clipboardRepo = ClipboardRepository(this)
    }

    override fun onCreateInputView(): View {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        val composeView = ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
            setViewTreeLifecycleOwner(this@SecureTypeIME)
            setViewTreeViewModelStoreOwner(this@SecureTypeIME)
            setViewTreeSavedStateRegistryOwner(this@SecureTypeIME)

            setContent {
                val clips by clipboardRepo.clips.collectAsState()
                val theme = KeyboardTheme.DarkBlue
                val settings = KeyboardSettings()

                KeyboardHost(
                    theme = theme,
                    settings = settings,
                    currentText = currentTextState,
                    cursorPosition = currentTextState.length,
                    onInsertText = { text ->
                        if (text == "\n") {
                            sendKeyChar('\n')
                        } else {
                            currentInputConnection?.commitText(text, 1)
                        }
                        currentTextState += text
                    },
                    onDeleteText = {
                        currentInputConnection?.deleteSurroundingText(1, 0)
                        if (currentTextState.isNotEmpty()) {
                            currentTextState = currentTextState.dropLast(1)
                        }
                    },
                    onMoveCursor = { offset ->
                        val ic = currentInputConnection ?: return@KeyboardHost
                        if (offset > 0) {
                            sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT)
                        } else {
                            sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT)
                        }
                    },
                    onSelectAll = {
                        currentInputConnection?.performContextMenuAction(android.R.id.selectAll)
                    },
                    onCut = {
                        currentInputConnection?.performContextMenuAction(android.R.id.cut)
                    },
                    onCopy = {
                        currentInputConnection?.performContextMenuAction(android.R.id.copy)
                    },
                    onPaste = {
                        currentInputConnection?.performContextMenuAction(android.R.id.paste)
                    },
                    clipboardItems = clips,
                    onTogglePinClip = { clipboardRepo.togglePin(it) },
                    onDeleteClip = { clipboardRepo.deleteClip(it) },
                    onClearClips = { clipboardRepo.clearAll() }
                )
            }
        }
        return composeView
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        store.clear()
    }
}
