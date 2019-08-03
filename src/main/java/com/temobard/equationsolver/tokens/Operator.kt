package com.temobard.equationsolver.tokens

import java.lang.IllegalArgumentException
import kotlin.math.*

class Operator(val type: Type) : Token {

    fun execute(left: Double, right: Double?): Double = when (type) {
        Type.ADD -> left + right!!
        Type.SUBTRACT -> left - right!!
        Type.DIVIDE -> left / right!!
        Type.MULTIPLY -> left * right!!
        Type.POWER -> left.pow(right!!)
        Type.SINE -> sin(left)
        Type.COSINE -> cos(left)
        else -> throw IllegalArgumentException("Operator not supported")
    }

    fun execute(value: Double): Double = execute(value, null)

    enum class Type(val value: String, val rank: Int, val rightAssociative: Boolean, val operandCount: Int) {
        SUBTRACT("-", 2, false, 2),
        ADD("+", 2, false, 2),
        DIVIDE("/", 3, false, 2),
        MULTIPLY("*", 3, false, 2),
        POWER("^", 4, true, 2),
        SINE("sin", 3, true, 1),
        COSINE("cos", 3, false, 1),
        PAR_LEFT("(", 1, false, 0),
        PAR_RIGHT(")", 1, false, 0)
    }
}