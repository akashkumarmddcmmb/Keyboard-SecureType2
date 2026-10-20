package com.example.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SmartBackspaceHandler(
    private val scope: CoroutineScope,
    private val onDelete: () -> Unit,
    private val isSmartEnabled: () -> Boolean,
    private val minRepeatIntervalMs: Long = 25L
) {
    private var repeatJob: Job? = null

    fun onPointerDown() {
        // 1. Instant first delete
        onDelete()

        // Cancel previous repeat if any
        repeatJob?.cancel()

        repeatJob = scope.launch {
            if (!isSmartEnabled()) {
                // Standard slow backspace fallback
                delay(400)
                while (isActive) {
                    onDelete()
                    delay(70)
                }
                return@launch
            }

            // 2. Smart Backspace: Initial delay before repeating
            delay(220)

            // 3. Gradual acceleration: Start at 80ms and decrease down to 25ms smoothly
            var currentInterval = 80L
            val targetInterval = minRepeatIntervalMs.coerceAtLeast(15L)

            while (isActive) {
                onDelete()
                delay(currentInterval)

                if (currentInterval > targetInterval) {
                    currentInterval = (currentInterval - 5L).coerceAtLeast(targetInterval)
                }
            }
        }
    }

    fun onPointerUp() {
        // 4. Stops immediately when you release
        repeatJob?.cancel()
        repeatJob = null
    }

    fun cancel() {
        repeatJob?.cancel()
        repeatJob = null
    }
}
