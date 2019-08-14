package com.temobard.equationsolver.solvers

import kotlin.math.*

sealed class Expression {

    fun solve() : Double = when(this) {
        is Add -> left + right
        is Subtract -> left - right
        is Multiply -> left * right
        is Divide -> left / right
        is Power -> left.pow(right)
        is Sqrt -> sqrt(value)
        is Sin -> sin(value)
        is Cos -> cos(value)
        is Tan -> tan(value)
        is Max -> max(left, right)
        is Min -> min(left, right)
    }

    class Add(val left: Double, val right: Double) : Expression()
    class Subtract(val left: Double, val right: Double) : Expression()
    class Multiply(val left: Double, val right: Double) : Expression()
    class Divide(val left: Double, val right: Double) : Expression()
    class Power(val left: Double, val right: Double) : Expression()
    class Sqrt(val value: Double) : Expression()
    class Sin(val value: Double) : Expression()
    class Cos(val value: Double) : Expression()
    class Tan(val value: Double) : Expression()
    class Max(val left: Double, val right: Double) : Expression()
    class Min(val left: Double, val right: Double) : Expression()
}