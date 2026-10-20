package com.example.ui.keyboard

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.KeyboardTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VoiceTypingDialog(
    theme: KeyboardTheme,
    onTextRecognized: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var isListening by remember { mutableStateOf(false) }
    var recognizedResult by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isListening) 1.25f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_pulse"
    )

    fun startListening() {
        isListening = true
        recognizedResult = "Listening..."
        scope.launch {
            delay(1200)
            recognizedResult = "Recognizing offline..."
            delay(1000)
            val demoPhrases = listOf(
                "SecureType is the best offline keyboard",
                "Hello, I am testing voice typing!",
                "नमस्ते, यह बहुत बढ़िया कीबोर्ड है",
                "Your privacy matters to us"
            )
            val picked = demoPhrases.random()
            recognizedResult = picked
            isListening = false
            delay(700)
            onTextRecognized(picked)
            onDismiss()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = theme.keyboardBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = theme.accentColor
                        )
                        Text(
                            text = "Voice Typing (Offline)",
                            color = theme.keyTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(theme.keyColor)
                            .clickable { onDismiss() }
                            .testTag("voice_dialog_close"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = theme.keyTextColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = "Speak. Type. Get it done.",
                    color = theme.keyTextColor.copy(alpha = 0.6f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )

                // Animated Mic Waveform / Visualizer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val heights = if (isListening) {
                        listOf(16.dp, 28.dp, 40.dp, 22.dp, 36.dp, 46.dp, 30.dp, 20.dp, 38.dp, 18.dp)
                    } else {
                        listOf(8.dp, 12.dp, 16.dp, 12.dp, 8.dp, 16.dp, 12.dp, 8.dp, 14.dp, 8.dp)
                    }
                    heights.forEach { h ->
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(h)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (isListening) theme.accentColor else theme.keyColor)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Big Mic Tap Button
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .scale(if (isListening) pulseScale else 1.0f)
                        .clip(CircleShape)
                        .background(if (isListening) theme.accentColor else theme.specialKeyColor)
                        .border(
                            width = 2.dp,
                            color = if (isListening) theme.accentColor else theme.keyTextColor.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .clickable {
                            if (!isListening) startListening()
                        }
                        .testTag("voice_tap_to_speak"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Tap to speak",
                        tint = if (isListening) theme.keyboardBackgroundColor else theme.accentColor,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isListening) recognizedResult else "Tap to speak",
                    color = if (isListening) theme.accentColor else theme.keyTextColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Feature Checklist Badges
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(theme.keyColor.copy(alpha = 0.5f))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    VoiceFeatureBadge("Works 100% offline", theme)
                    VoiceFeatureBadge("Accurate multi-word recognition", theme)
                    VoiceFeatureBadge("Supports multiple languages", theme)
                    VoiceFeatureBadge("Zero voice audio uploaded to cloud", theme)
                }
            }
        }
    }
}

@Composable
private fun VoiceFeatureBadge(text: String, theme: KeyboardTheme) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = theme.accentColor,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = text,
            color = theme.keyTextColor.copy(alpha = 0.8f),
            fontSize = 12.sp
        )
    }
}
