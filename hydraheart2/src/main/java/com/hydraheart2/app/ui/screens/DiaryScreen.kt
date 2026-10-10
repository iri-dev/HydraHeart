package com.hydraheart2.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hydraheart2.app.ui.components.FrostedGlass
import com.hydraheart2.app.util.HapticEngine
import com.hydraheart2.app.viewmodel.HydraViewModel

@Composable
fun DiaryScreen(viewModel: HydraViewModel, onBack: () -> Unit) {
    val theme = viewModel.currentTheme
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(theme.bg1, theme.bg2, theme.bg3)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 100.dp)
        ) {
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    HapticEngine.tick(context)
                    onBack()
                }) {
                    Text("←", fontSize = 22.sp, color = theme.accent)
                }
                Text("Hydra's Diary 📖", color = theme.accent3, style = MaterialTheme.typography.headlineMedium)
            }
            if (viewModel.diaryEntries.isEmpty()) {
                FrostedGlass(modifier = Modifier.fillMaxWidth(), theme = theme, blurRadius = 22.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📖", fontSize = 40.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Hydra's diary is empty",
                            color = theme.textPrimary,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "As you track drinks, Hydra will write about her day with you!",
                            color = theme.textSoft,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                viewModel.diaryEntries.forEach { entry ->
                    FrostedGlass(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        theme = theme,
                        blurRadius = 18.dp
                    ) {
                        Text(entry.time, color = theme.textMuted, style = MaterialTheme.typography.labelSmall)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            entry.text,
                            color = theme.textSoft,
                            lineHeight = 20.sp,
                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)
                        )
                    }
                }
            }
        }
    }
}
