package com.example.engine

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View
import com.example.model.KeySoundType

class FeedbackManager(private val context: Context) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, 60)
        } catch (_: Exception) {}
    }

    fun playKeyFeedback(
        soundType: KeySoundType,
        soundVolume: Float,
        vibrate: Boolean,
        vibrateMs: Int,
        view: View? = null
    ) {
        if (vibrate) {
            triggerVibration(vibrateMs, view)
        }

        if (soundType != KeySoundType.NONE) {
            playKeySound(soundType, soundVolume)
        }
    }

    fun triggerVibration(vibrateMs: Int, view: View? = null) {
        try {
            if (view != null && view.isHapticFeedbackEnabled) {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            } else if (vibrator != null && vibrator.hasVibrator()) {
                val duration = vibrateMs.toLong().coerceIn(10L, 80L)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(duration)
                }
            }
        } catch (_: Exception) {}
    }

    private fun playKeySound(soundType: KeySoundType, volume: Float) {
        try {
            when (soundType) {
                KeySoundType.CLICK -> {
                    audioManager?.playSoundEffect(AudioManager.FX_KEY_CLICK, volume)
                }
                KeySoundType.MECHANICAL -> {
                    // Crisp higher tone
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 25)
                }
                KeySoundType.SOFT -> {
                    // Soft low click
                    audioManager?.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, volume * 0.7f)
                }
                KeySoundType.POP -> {
                    // Playful pop tone
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 20)
                }
                KeySoundType.NONE -> {}
            }
        } catch (_: Exception) {}
    }

    fun release() {
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (_: Exception) {}
    }
}
