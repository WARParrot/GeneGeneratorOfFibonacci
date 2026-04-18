package com.example.genegeneratoroffibonacci.data

data class LearningState(
    val generation: Int,
    val bestFitness: Int,
    val bestCode: String?,
    val isLearning: Boolean
)
