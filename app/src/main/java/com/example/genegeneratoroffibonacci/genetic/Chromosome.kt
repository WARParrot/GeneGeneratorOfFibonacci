package com.example.genegeneratoroffibonacci.genetic

import kotlin.random.Random

sealed class Chromosome {
    data class Constant(val value: Int) : Chromosome()
    object Variable : Chromosome()
    data class Sum(val l: Chromosome, val r: Chromosome) : Chromosome()
    data class Sub(val l: Chromosome, val r: Chromosome) : Chromosome()
    data class Mul(val l: Chromosome, val r: Chromosome) : Chromosome()
    data class IfL(val l: Chromosome, val r: Chromosome, val branch1: Chromosome, val branch2: Chromosome) : Chromosome()
    data class Call(val arg: Chromosome) : Chromosome()

    companion object {
        private val terms = listOf(
            { Constant(Random.nextInt(0, 10)) },
            { Variable }
        )
        private val funcs = listOf<(Chromosome, Chromosome) -> Chromosome>(
            { left, right -> Sum(left, right) },
            { left, right -> Sub(left, right) },
            { left, right -> Mul(left, right) },
        )

        fun randomize(maxDepth: Int, currentDepth: Int): Chromosome {
            val isTerminal = currentDepth >= maxDepth || currentDepth > 1 && Random.nextDouble() < 1.0 / maxDepth

            return if (isTerminal) {
                terms.random()()
            }
            else {
                val left = randomize(maxDepth, currentDepth + 1)
                val right = randomize(maxDepth, currentDepth + 1)

                if (Random.nextDouble() < 0.2 && left !is IfL && right !is IfL) {
                    IfL(left, right, randomize(maxDepth, currentDepth + 1), randomize(maxDepth, currentDepth + 1))
                }
                else {
                    funcs.random()(left, right)
                }
            }
        }
    }
}
