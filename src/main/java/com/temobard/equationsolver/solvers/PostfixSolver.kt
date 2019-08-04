package com.temobard.equationsolver.solvers

import com.temobard.equationsolver.tokens.*
import com.temobard.equationsolver.tokens.Number
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sin

/**
 * Postfix notation (Reverse Polish Notation) based solver
 * @param polish an array of tokens organized in the RPN order
 */
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

    /**
     * Optimize the given RPN series as much as possible in order to reduce calculation time
     */
    fun optimize() {
        //TODO: here we should pre-calculate the numeric part of the equation
        //in order to reduce the overall calculation time
    }

    override fun toString(): String = eqString

    /**
     * Calculates the RPN array.
     * Use this function for either numeric equations, or for the 0 value of symbolic equations
     * @return calculated value
     */
    override fun calculate(): Double = calculateFor(0.0)

    /**
     * Calculates the RPN array.
     * @param value variable value to calculate equation for
     * @return calculated value
     */
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