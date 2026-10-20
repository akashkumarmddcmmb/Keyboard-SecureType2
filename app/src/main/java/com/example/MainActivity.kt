package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.KeyboardViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AppSimulatorScreen
import com.example.ui.screens.PlaygroundScreen
import com.example.ui.screens.PrivacySettingsScreen
import com.example.ui.screens.ThemesScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.theme.MyApplicationTheme

enum class NavTab(val title: String, val icon: ImageVector) {
    PLAYGROUND("Keyboard", Icons.Default.Keyboard),
    THEMES("Themes", Icons.Default.Palette),
    SIMULATOR("In Action", Icons.Default.Smartphone),
    TOOLS("Tools", Icons.Default.Widgets),
    PRIVACY("Privacy", Icons.Default.Security)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val keyboardViewModel: KeyboardViewModel = viewModel()
                var selectedTabIndex by remember { mutableIntStateOf(0) }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF070B14)),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .testTag("main_navigation_bar"),
                            containerColor = Color(0xFF0B132B),
                            tonalElevation = 8.dp
                        ) {
                            NavTab.values().forEachIndexed { index, tab ->
                                val isSelected = selectedTabIndex == index
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { selectedTabIndex = index },
                                    icon = {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.title,
                                            tint = if (isSelected) Color(0xFF00F0FF) else Color(0xFF94A3B8)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.title,
                                            color = if (isSelected) Color(0xFF00F0FF) else Color(0xFF94A3B8),
                                            fontSize = 11.sp
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = Color(0xFF00E5FF).copy(alpha = 0.18f)
                                    ),
                                    modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (selectedTabIndex) {
                            0 -> PlaygroundScreen(
                                viewModel = keyboardViewModel,
                                onNavigateToThemes = { selectedTabIndex = 1 },
                                onNavigateToSettings = { selectedTabIndex = 4 }
                            )
                            1 -> ThemesScreen(viewModel = keyboardViewModel)
                            2 -> AppSimulatorScreen(viewModel = keyboardViewModel)
                            3 -> ToolsScreen(viewModel = keyboardViewModel)
                            else -> PrivacySettingsScreen(viewModel = keyboardViewModel)
                        }
                    }
                }
            }
        }
    }
}
