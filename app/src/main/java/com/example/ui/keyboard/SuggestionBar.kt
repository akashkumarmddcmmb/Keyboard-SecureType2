package com.example.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.KeyboardTheme

@Composable
fun SuggestionBar(
    suggestions: List<String>,
    theme: KeyboardTheme,
    onSuggestionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(38.dp)
            .background(theme.keyboardBackgroundColor)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Previous suggestions",
            tint = theme.keyTextColor.copy(alpha = 0.4f),
            modifier = Modifier.padding(end = 4.dp)
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            suggestions.take(3).forEachIndexed { index, suggestion ->
                val isPrimary = index == 0
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 3.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isPrimary) theme.accentColor.copy(alpha = 0.15f)
                            else theme.keyColor.copy(alpha = 0.4f)
                        )
                        .clickable { onSuggestionSelected(suggestion) }
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                        .testTag("suggestion_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = suggestion,
                        color = if (isPrimary) theme.accentColor else theme.keyTextColor,
                        fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }

                if (index < suggestions.take(3).lastIndex) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(16.dp)
                            .background(theme.keyTextColor.copy(alpha = 0.15f))
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Next suggestions",
            tint = theme.keyTextColor.copy(alpha = 0.4f),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
