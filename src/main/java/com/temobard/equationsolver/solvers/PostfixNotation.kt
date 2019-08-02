package com.temobard.equationsolver.solvers

import com.temobard.equationsolver.tokens.*
import com.temobard.equationsolver.tokens.Number
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.sin

class PostfixNotation(private val polish: ArrayList<Token>) :
    EquationNotation {

    private val eqString by lazy {
        val stringBuilder = StringBuilder()
        for(token in polish) {
            when(token) {
                is Number -> stringBuilder.append(token.value)
                is Variable -> stringBuilder.append(token.symbol)
                is Operator -> stringBuilder.append(token.type.value)
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

    fun calculate(): Double = calculateFor(0.0)

    fun calculateFor(value: Double): Double {
        val stack = Stack<Double>()

        for (token in polish) {
            when (token) {
                is Number -> stack.push(token.value)
                is Variable -> stack.push(value)
                is Operator -> {
                    val right = stack.pop()
                    val left = stack.pop()

                    stack.push(
                        when (token.type) {
                            OperatorType.ADD -> left + right
                            OperatorType.SUBTRACT -> left - right
                            OperatorType.MULTIPLY -> left * right
                            OperatorType.DIVIDE -> left / right
                            OperatorType.POWER -> right.pow(left)
                            OperatorType.SINUS -> sin(right)
                            else -> throw Exception()
                        }
                    )
                }
            }
        }

        return stack.pop()
    }
}