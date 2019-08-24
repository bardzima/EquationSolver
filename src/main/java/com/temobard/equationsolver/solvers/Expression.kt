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
        is Exponent -> exp(value)
        is Logarithm -> log(left, right)
        is LogNatural -> ln(value)
        is Log10 -> log10(value)
        is Log2 -> log2(value)

        is Sin -> sin(value)
        is Cos -> cos(value)
        is Tan -> tan(value)
        is Cot -> 1.0 / tan(value)
        is Sec -> TODO()
        is Csc -> TODO()
        is Asin -> asin(value)
        is Asinh -> asinh(value)
        is Acos -> acos(value)
        is Acosh -> acosh(value)
        is Atan -> atan(value)
        is Atanh -> atanh(value)

        is Abs -> abs(value)
        is Max -> max(left, right)
        is Min -> min(left, right)
    }

    //Basic functions
    class Add(val left: Double, val right: Double) : Expression()
    class Subtract(val left: Double, val right: Double) : Expression()
    class Multiply(val left: Double, val right: Double) : Expression()
    class Divide(val left: Double, val right: Double) : Expression()

    //Exponential functions
    class Power(val left: Double, val right: Double) : Expression()
    class Sqrt(val value: Double) : Expression()
    class Exponent(val value: Double) : Expression()
    class Logarithm(val left: Double, val right: Double) : Expression()
    class LogNatural(val value: Double) : Expression()
    class Log10(val value: Double) : Expression()
    class Log2(val value: Double) : Expression()

    //Trigonometric functions
    class Sin(val value: Double) : Expression()
    class Cos(val value: Double) : Expression()
    class Tan(val value: Double) : Expression()
    class Cot(val value: Double) : Expression()
    class Sec(val value: Double) : Expression()
    class Csc(val value: Double) : Expression()
    class Asin(val value: Double) : Expression()
    class Asinh(val value: Double) : Expression()
    class Acos(val value: Double) : Expression()
    class Acosh(val value: Double) : Expression()
    class Atan(val value: Double) : Expression()
    class Atanh(val value: Double) : Expression()

    class Abs(val value: Double) : Expression()
    class Max(val left: Double, val right: Double) : Expression()
    class Min(val left: Double, val right: Double) : Expression()
}