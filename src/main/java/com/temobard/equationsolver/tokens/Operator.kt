package com.temobard.equationsolver.tokens

enum class Operator(val value: String, val precedence: Int, val rightAssociative: Boolean) : Token {

    SUBTRACT("-", 2, false),
    ADD("+", 2, false),
    DIVIDE("/", 3, false),
    MULTIPLY("*", 3, false),

    POWER("^", 4, true),
    SQRT("sqrt", 3, true),
    EXP("exp", 3, true),
    LOG("log", 3, true),
    LN("ln", 3, true),
    LOG10("lg", 3, true),
    LOG2("log2", 3, true),

    SINE("sin", 3, true),
    COSINE("cos", 3, true),
    TANGENT("tan", 3, true),
    COTANGENT("cot", 3, true),
    SECANT("sec", 3, true),
    COSECANT("csc", 3, true),
    ASIN("asin", 3, true),
    ASINH("asinh", 3, true),
    ACOS("acos", 3, true),
    ACOSH("acosh", 3, true),
    ATAN("atan", 3, true),
    ATANH("atanh", 3, true),

    ABS("abs", 3, true),
    MAX("max", 3, true),
    MIN("min", 3, true),

    PAR_LEFT("(", 1, false),
    PAR_RIGHT(")", 1, false)
}