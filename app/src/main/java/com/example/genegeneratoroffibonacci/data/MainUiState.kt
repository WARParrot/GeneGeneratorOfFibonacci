package com.example.genegeneratoroffibonacci.data

data class MainUiState(
    val generation: Int = 0,
    val bestFitness: Int = Int.MAX_VALUE,
    val bestCode: String? = null,
    val isLearning: Boolean = false
)
