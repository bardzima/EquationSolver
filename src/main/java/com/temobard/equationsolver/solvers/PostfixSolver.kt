package com.temobard.equationsolver.solvers

import com.temobard.equationsolver.tokens.*
import com.temobard.equationsolver.tokens.Number
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sin

class PostfixSolver(private val polish: ArrayList<Token>) : EquationSolver {

    private val eqString by lazy {
        val stringBuilder = StringBuilder()
        for (token in polish) {
            when (token) {
                is Number -> stringBuilder.append(token.value)
                is Variable -> stringBuilder.append(token.symbol)
                is Operator -> stringBuilder.append(token.type.value)
                is Constant -> stringBuilder.append(token.type.moniker)
                else -> throw Exception()
            }
            stringBuilder.append(" ")
        }
        stringBuilder.toString()
    }

    fun optimize() {
        //TODO: here we should pre-calculate the numeric part of the equation
        //in order to reduce the overall calculation time
    }

    override fun toString(): String = eqString

    override fun calculate(): Double = calculateFor(0.0)

    override fun calculateFor(value: Double): Double {
        val stack = Stack<Double>()

        for (token in polish) {
            when (token) {
                is Number -> stack.push(token.value)
                is Constant -> stack.push(token.type.value)
                is Variable -> stack.push(value)
                is Operator -> {
                    val right = stack.pop()
                    stack.push(
                        when {
                            token.type.operandCount == 1 -> token.execute(right)
                            token.type.operandCount == 2 -> token.execute(stack.pop(), right)
                            else -> throw IllegalArgumentException()
                        }
                    )
                }
            }
        }

        return stack.pop()
    }
}