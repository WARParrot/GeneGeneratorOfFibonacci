package com.example.genegeneratoroffibonacci.genetic

object KotlinCodeGenerator {
    fun generate(gene: Chromosome): String {
        val body = gene.toKotlin()
        return body
    }

    private fun Chromosome.toKotlin(): String = when (this) {
        is Chromosome.Constant -> value.toString()
        is Chromosome.Variable -> "n"
        is Chromosome.Sum -> "(${l.toKotlin()} + ${r.toKotlin()})"
        is Chromosome.Sub -> "(${l.toKotlin()} - ${r.toKotlin()})"
        is Chromosome.Mul -> "(${l.toKotlin()} * ${r.toKotlin()})"
        is Chromosome.IfL -> "(${l.toKotlin()} < ${r.toKotlin()}) ${branch1.toKotlin()} else ${branch2.toKotlin()}"
        is Chromosome.Call -> "fib(${arg.toKotlin()})"
    }
}