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

    fun mutate(child: Chromosome, mutationRate: Double): Chromosome {
        return if (Random.nextDouble() < mutationRate) {
            val replaceWhat = getRandomNode(child)
            val replaceTo = Chromosome.randomize(3, 0)
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
            is Chromosome.Div -> 1 + countNodes(node.l) + countNodes(node.r)
            is Chromosome.IfL -> 1 + countNodes(node.l) + countNodes(node.r) +
                    countNodes(node.branch1) + countNodes(node.branch2)
            is Chromosome.Call -> 1 + countNodes(node.arg)
        }
    }

    fun getNodeAtIndex(node: Chromosome, index: Int): Pair<Chromosome, Int> {
        if (index == 0) return node to 0

        var left = index - 1
        return when (node) {
            is Chromosome.Constant, Chromosome.Variable -> node to left
            is Chromosome.Sum -> {
                val (leftNode, leftLeft) = getNodeAtIndex(node.l, left)
                if (leftLeft == 0) return leftNode to 0
                left = leftLeft
                val (rightNode, rightLeft) = getNodeAtIndex(node.r, left - 1)
                rightNode to rightLeft
            }
            is Chromosome.Sub -> {
                val (leftNode, leftLeft) = getNodeAtIndex(node.l, left)
                if (leftLeft == 0) return leftNode to 0
                left = leftLeft
                val (rightNode, rightLeft) = getNodeAtIndex(node.r, left - 1)
                rightNode to rightLeft
            }
            is Chromosome.Mul -> {
                val (leftNode, leftLeft) = getNodeAtIndex(node.l, left)
                if (leftLeft == 0) return leftNode to 0
                left = leftLeft
                val (rightNode, rightLeft) = getNodeAtIndex(node.r, left - 1)
                rightNode to rightLeft
            }
            is Chromosome.Div -> {
                val (leftNode, leftLeft) = getNodeAtIndex(node.l, left)
                if (leftLeft == 0) return leftNode to 0
                left = leftLeft
                val (rightNode, rightLeft) = getNodeAtIndex(node.r, left - 1)
                rightNode to rightLeft
            }
            is Chromosome.IfL -> {
                val (leftNode, leftLeft) = getNodeAtIndex(node.l, left)
                if (leftLeft == 0) return leftNode to 0
                left = leftLeft
                val (rightNode, rightLeft) = getNodeAtIndex(node.r, left - 1)
                if (rightLeft == 0) return rightNode to 0
                left = rightLeft
                val (branch1Node, branch1Left) = getNodeAtIndex(node.branch1, left - 1)
                if (branch1Left == 0) return branch1Node to 0
                left = branch1Left
                val (branch2Node, branch2Left) = getNodeAtIndex(node.branch2, left - 1)
                branch2Node to branch2Left
            }
            is Chromosome.Call -> {
                getNodeAtIndex(node.arg, left)
            }
        }
    }

    fun getRandomNode(root: Chromosome): Chromosome {
        val totalNodes = countNodes(root)
        val goal = Random.nextInt(totalNodes)
        return getNodeAtIndex(root, goal).first
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
            is Chromosome.Div -> Chromosome.Div(
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
