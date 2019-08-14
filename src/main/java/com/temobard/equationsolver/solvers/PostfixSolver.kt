package com.temobard.equationsolver.solvers

import com.temobard.equationsolver.tokens.*
import com.temobard.equationsolver.tokens.Number
import java.util.*
import kotlin.collections.ArrayList

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
                is Operator -> stringBuilder.append(token.value)
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
                    val expr = when(token) {
                        Operator.ADD -> Expression.Add(stack.pop(), right)
                        Operator.SUBTRACT -> Expression.Subtract(stack.pop(), right)
                        Operator.MULTIPLY -> Expression.Multiply(stack.pop(), right)
                        Operator.DIVIDE -> Expression.Divide(stack.pop(), right)
                        Operator.POWER -> Expression.Power(stack.pop(), right)
                        Operator.SQRT -> Expression.Sqrt(right)
                        Operator.SINE -> Expression.Sin(right)
                        Operator.COSINE -> Expression.Cos(right)
                        Operator.TANGENT -> Expression.Tan(right)
                        Operator.MAX -> Expression.Max(stack.pop(), right)
                        Operator.MIN -> Expression.Min(stack.pop(), right)
                        else -> throw IllegalArgumentException()

                    }
                    stack.push(expr.solve())
                }
                is Operator_deprecated -> {
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