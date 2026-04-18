package com.example.genegeneratoroffibonacci.genetic

import kotlin.random.Random

object Evolution {
    fun tournamentSelection(population: List<Pair<Chromosome, Int>>, size: Int): Chromosome {
        return (1..size)
            .map { population.random() }
            .minBy { it.second }
            .first
    }

    fun crossover(parent1: Chromosome, parent2: Chromosome): Chromosome {
        val replaceWhat = getRandomNode(parent1)
        val replaceTo = getRandomNode(parent2)
        return replacedNode(parent1, replaceWhat, replaceTo)
    }

    fun mutate(child: Chromosome, mutationRate: Double, secondaryMutationRate: Double): Chromosome {
        return if (Random.nextDouble() < mutationRate) {
            val replaceWhat = getRandomNode(child)
            val replaceTo = Chromosome.randomize(3, 0)
            replacedNode(child, replaceWhat, replaceTo)
        }
        else if (Random.nextDouble() < secondaryMutationRate) {
            val replaceWhat = getRandomNode(child)
            val replaceTo = Chromosome.Call(Chromosome.randomize(3, 0))
            replacedNode(child, replaceWhat, replaceTo)
        }
        else {
            child
        }
    }

    fun countNodes(node: Chromosome): Int {
        return when (node) {
            is Chromosome.Constant -> 1
            is Chromosome.Variable -> 1
            is Chromosome.Sum -> 1 + countNodes(node.l) + countNodes(node.r)
            is Chromosome.Sub -> 1 + countNodes(node.l) + countNodes(node.r)
            is Chromosome.Mul -> 1 + countNodes(node.l) + countNodes(node.r)
            is Chromosome.IfL -> 1 + countNodes(node.l) + countNodes(node.r) +
                    countNodes(node.branch1) + countNodes(node.branch2)
            is Chromosome.Call -> 1 + countNodes(node.arg)
        }
    }

    fun getNodeAtIndex(root: Chromosome, index: Int): Chromosome {
        var currentIndex = 0
        var result: Chromosome? = null

        fun dfs(node: Chromosome) {
            if (result != null) return
            if (currentIndex == index) {
                result = node
                return
            }
            currentIndex++
            when (node) {
                is Chromosome.Constant, Chromosome.Variable -> {}
                is Chromosome.Sum -> {
                    dfs(node.l)
                    dfs(node.r)
                }
                is Chromosome.Sub -> {
                    dfs(node.l)
                    dfs(node.r)
                }
                is Chromosome.Mul -> {
                    dfs(node.l)
                    dfs(node.r)
                }
                is Chromosome.IfL -> {
                    dfs(node.l)
                    dfs(node.r)
                    dfs(node.branch1)
                    dfs(node.branch2)
                }
                is Chromosome.Call -> {
                    dfs(node.arg)
                }
            }
        }
        dfs(root)
        return result ?: error("Index out of bounds.")
    }

    fun getRandomNode(root: Chromosome): Chromosome {
        val totalNodes = countNodes(root)
        val goal = Random.nextInt(totalNodes)
        return getNodeAtIndex(root, goal)
    }

    fun replacedNode(root: Chromosome, replaceWhat: Chromosome, replaceTo: Chromosome): Chromosome {
        if (root == replaceWhat) return replaceTo

        return when (root) {
            is Chromosome.Constant, Chromosome.Variable -> root
            is Chromosome.Sum -> Chromosome.Sum(
                replacedNode(root.l, replaceWhat, replaceTo),
                replacedNode(root.r, replaceWhat, replaceTo)
            )
            is Chromosome.Sub -> Chromosome.Sub(
                replacedNode(root.l, replaceWhat, replaceTo),
                replacedNode(root.r, replaceWhat, replaceTo)
            )
            is Chromosome.Mul -> Chromosome.Mul(
                replacedNode(root.l, replaceWhat, replaceTo),
                replacedNode(root.r, replaceWhat, replaceTo)
            )
            is Chromosome.IfL -> Chromosome.IfL(
                replacedNode(root.l, replaceWhat, replaceTo),
                replacedNode(root.r, replaceWhat, replaceTo),
                replacedNode(root.branch1, replaceWhat, replaceTo),
                replacedNode(root.branch2, replaceWhat, replaceTo)
            )
            is Chromosome.Call -> Chromosome.Call(
                replacedNode(root.arg, replaceWhat, replaceTo)
            )
        }
    }
}
