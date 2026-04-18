package com.example.genegeneratoroffibonacci.genetic

import kotlin.math.absoluteValue

object Fitness {
    private val testCases = listOf(
        0 to 0,
        1 to 1,
        2 to 1,
        3 to 2,
        4 to 3,
        5 to 5,
        6 to 8,
        7 to 13,
        10 to 55,
        15 to 610
    )

    fun evaluate(gene: Chromosome): Int {
        var error = 0

        for ((n, expected) in testCases) {
            val result = interpret(gene, n, gene) ?: return Int.MAX_VALUE
            error += (result - expected).absoluteValue
        }
        return error.absoluteValue
    }

    private fun interpret(node: Chromosome, n: Int, gene: Chromosome): Int? {
        return when (node) {
            is Chromosome.Constant -> node.value
            is Chromosome.Variable -> n
            is Chromosome.Sum -> {
                val left = interpret(node.l, n, gene) ?: return null
                val right = interpret(node.r, n, gene) ?: return null
                left + right
            }
            is Chromosome.Sub -> {
                val left = interpret(node.l, n, gene) ?: return null
                val right = interpret(node.r, n, gene) ?: return null
                left - right
            }
            is Chromosome.Mul -> {
                val left = interpret(node.l, n, gene) ?: return null
                val right = interpret(node.r, n, gene) ?: return null
                left * right
            }
            is Chromosome.IfL -> {
                val left = interpret(node.l, n, gene) ?: return null
                val right = interpret(node.r, n, gene) ?: return null
                if (left < right) {
                    interpret(node.branch1, n, gene)
                }
                else {
                    interpret(node.branch2, n, gene)
                }
            }
            is Chromosome.Call -> {
                val newN = interpret(node.arg, n, gene) ?: return null
                if (newN < 0) return 0
                interpret(gene, newN, gene)
            }
        }
    }
}
