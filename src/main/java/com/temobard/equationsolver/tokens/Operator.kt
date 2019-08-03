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
        Type.SQRT -> sqrt(left)
        Type.SINE -> sin(left)
        Type.COSINE -> cos(left)
        Type.TANGENT -> tan(left)
        Type.MAX -> max(left, right!!)
        Type.MIN -> min(left, right!!)
        else -> throw IllegalArgumentException("Operator not supported")
    }

    fun execute(value: Double): Double = execute(value, null)

    enum class Type(val value: String, val rank: Int, val rightAssociative: Boolean, val operandCount: Int) {
        SUBTRACT("-", 2, false, 2),
        ADD("+", 2, false, 2),
        DIVIDE("/", 3, false, 2),
        MULTIPLY("*", 3, false, 2),
        POWER("^", 4, true, 2),
        SQRT("sqrt", 3, false, 1),
        SINE("sin", 3, true, 1),
        COSINE("cos", 3, true, 1),
        TANGENT("tan", 3, true, 1),
        MAX("max", 3, true, 2),
        MIN("min", 3, true, 2),
        PAR_LEFT("(", 1, false, 0),
        PAR_RIGHT(")", 1, false, 0)
    }
}