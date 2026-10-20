package com.example.ui.keyboard

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KeyboardTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    text: String,
    secondaryText: String? = null,
    isSpecial: Boolean = false,
    theme: KeyboardTheme,
    onKeyTap: () -> Unit,
    onLongPress: (() -> Unit)? = null,
    onPointerDown: (() -> Unit)? = null,
    onPointerUp: (() -> Unit)? = null,
    customContent: (@Composable () -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = tween(durationMillis = 60),
        label = "key_scale"
    )

    val bgColor = when {
        isPressed -> theme.accentColor.copy(alpha = 0.35f)
        isSpecial -> theme.specialKeyColor
        else -> theme.keyColor
    }

    val textColor = when {
        isPressed -> theme.accentColor
        isSpecial -> theme.specialKeyTextColor
        else -> theme.keyTextColor
    }

    Box(
        modifier = modifier
            .padding(horizontal = 2.5.dp, vertical = 3.dp)
            .height(46.dp)
            .scale(scale)
            .shadow(
                elevation = if (isPressed) 1.dp else 2.5.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .testTag("key_${text.lowercase()}")
            .let { baseModifier ->
                if (onPointerDown != null && onPointerUp != null) {
                    baseModifier.pointerInteropFilter { motionEvent ->
                        when (motionEvent.action) {
                            MotionEvent.ACTION_DOWN -> {
                                isPressed = true
                                onPointerDown()
                                true
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                isPressed = false
                                onPointerUp()
                                true
                            }
                            else -> false
                        }
                    }
                } else {
                    baseModifier.pointerInput(text) {
                        detectTapGestures(
                            onPress = {
                                isPressed = true
                                tryAwaitRelease()
                                isPressed = false
                            },
                            onTap = { onKeyTap() },
                            onLongPress = {
                                if (onLongPress != null) {
                                    onLongPress()
                                } else if (secondaryText != null) {
                                    onKeyTap()
                                }
                            }
                        )
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (customContent != null) {
            customContent()
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 2.dp, vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (secondaryText != null) {
                    Text(
                        text = secondaryText,
                        fontSize = 9.sp,
                        color = textColor.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Medium,
                        lineHeight = 10.sp
                    )
                }
                Text(
                    text = text,
                    fontSize = if (text.length > 2) 13.sp else 18.sp,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
