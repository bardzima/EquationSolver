package com.temobard.equationsolver.tokens

import java.lang.IllegalArgumentException
import kotlin.math.*

@Deprecated("This class is deprecated. Use Operator enum class instead.")
class Operator_deprecated(val type: Type) : Token {

    fun execute(left: Double, right: Double?): Double = when (type) {
        Type.ADD -> left + right!!
        Type.SUBTRACT -> left - right!!
        Type.DIVIDE -> left / right!!
        Type.MULTIPLY -> left * right!!
        Type.POWER -> left.pow(right!!)
        Type.SQRT -> sqrt(left)
        Type.SINE -> sin(left)
        Type.COSINE -> cos(left)
        Type.TANGENT -> tan(left)
        Type.MAX -> max(left, right!!)
        Type.MIN -> min(left, right!!)
        else -> throw IllegalArgumentException("Operator_deprecated not supported")
    }

    fun execute(value: Double): Double = execute(value, null)

    enum class Type(val value: String, val precedence: Int, val rightAssociative: Boolean, val operandCount: Int) : Token {
        SUBTRACT("-", 2, false, 2),
        ADD("+", 2, false, 2),
        DIVIDE("/", 3, false, 2),
        MULTIPLY("*", 3, false, 2),
        POWER("^", 4, true, 2),
        SQRT("Sqrt", 3, false, 1),
        SINE("Sin", 3, true, 1),
        COSINE("Cos", 3, true, 1),
        TANGENT("Tan", 3, true, 1),
        MAX("Max", 3, true, 2),
        MIN("Min", 3, true, 2),
        PAR_LEFT("(", 1, false, 0),
        PAR_RIGHT(")", 1, false, 0)
    }
}