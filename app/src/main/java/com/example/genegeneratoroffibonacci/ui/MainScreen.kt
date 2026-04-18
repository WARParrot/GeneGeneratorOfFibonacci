package com.example.genegeneratoroffibonacci.ui

import android.content.ClipData
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gene Generator of Fibonacci",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (uiState.isLearning) viewModel.stopLearning()
                else viewModel.startLearning()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text (if (uiState.isLearning) "Stop genetic algorithm" else "Start genetic algorithm")
        }

        if (uiState.isLearning) {
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { uiState.generation / 100f}
            )
            Text(
                text = "Generation: ${uiState.generation} out of 100",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Straying score: ${uiState.bestFitness}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        uiState.bestCode?.let { code ->
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Generated kotlin code",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = code,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { scope.launch { clipboard.setClipEntry(ClipEntry(ClipData.newPlainText("Fibonacci code",code))) } },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Copy to clipboard")
                    }
                }
            }
        }
    }
}