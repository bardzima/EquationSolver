package com.temobard.equationsolver.tokens

internal data class Operator(val type: OperatorType) :
    Token

internal enum class OperatorType(val value: String, val rank: Int, val rightAssociative: Boolean) {
    SUBTRACT("-", 2, false),
    ADD("+", 2, false),
    DIVIDE("/", 3, false),
    MULTIPLY("*", 3, false),
    POWER("^", 4, true),
    SINUS("sin",3, true),
    COSINUS("cos", 3, false),
    PAR_LEFT("(", 1, false),
    PAR_RIGHT(")", 1, false)
}