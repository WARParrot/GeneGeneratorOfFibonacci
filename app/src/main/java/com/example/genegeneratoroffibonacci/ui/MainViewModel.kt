package com.example.genegeneratoroffibonacci.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genegeneratoroffibonacci.data.MainUiState
import com.example.genegeneratoroffibonacci.genetic.Learning
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel : ViewModel() {
    private val learning = Learning()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        learning.state
            .onEach { learnState ->
                _uiState.value = MainUiState(
                    generation = learnState.generation,
                    bestFitness = learnState.bestFitness,
                    bestCode = learnState.bestCode,
                    isLearning = learnState.isLearning
                )
            }
            .launchIn(viewModelScope)
    }

    fun startLearning() {
        learning.start(viewModelScope)
    }

    fun stopLearning() {
        learning.stop()
    }
}
