package com.example.genegeneratoroffibonacci.genetic

import com.example.genegeneratoroffibonacci.data.LearningState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class Learning (
    val populationSize: Int = 200,
    val maxGenerations: Int = 100,
    val maxTreeDepth: Int = 5,
    val mutationRate: Double = 0.1,
    val delayMs: Long = 16
) {
    private val _state = MutableSharedFlow<LearningState>(replay = 1)
    val state: SharedFlow<LearningState> = _state.asSharedFlow()

    private var evolutionJob: Job? = null

    fun start(scope: CoroutineScope) {
        evolutionJob?.cancel()
        evolutionJob = scope.launch(Dispatchers.Default) {
            runEvolution()
        }
    }

    fun stop() {
        evolutionJob?.cancel()
        evolutionJob = null
    }

    private suspend fun runEvolution() {
        var population = List(populationSize) {
            Chromosome.randomize(maxTreeDepth, 0)
        }
        var generation = 0
        var bestFitness = Int.MAX_VALUE
        var bestGene: Chromosome? = null
        var bestCode: String? = null

        _state.emit(LearningState(generation, bestFitness, null, true))

        while (generation < maxGenerations) {
            val fitnessScores = population.map { chromosome ->
                chromosome to Fitness.evaluate(chromosome)
            }
            val best = fitnessScores.minBy { it.second }
            bestGene = best.first
            bestFitness = best.second
            bestCode = KotlinCodeGenerator.generate(bestGene)

            _state.emit(LearningState(generation, bestFitness, bestCode, true))

            if (bestFitness == 0) break

            val newPopulation = mutableListOf(bestGene)
            while (newPopulation.size < populationSize) {
                val parent1 = Evolution.tournamentSelection(fitnessScores, 3)
                val parent2 = Evolution.tournamentSelection(fitnessScores, 3)
                var child = Evolution.crossover(parent1, parent2)
                child = Evolution.mutate(child, mutationRate)
                newPopulation.add(child)
            }
            population = newPopulation
            generation++

            delay(delayMs)
        }
        _state.emit(LearningState(generation, bestFitness, bestCode, false))
    }
}