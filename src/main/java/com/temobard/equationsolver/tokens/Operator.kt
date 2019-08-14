package com.temobard.equationsolver.tokens

enum class Operator(val value: String, val precedence: Int, val rightAssociative: Boolean) : Token {

    SUBTRACT("-", 2, false),
    ADD("+", 2, false),
    DIVIDE("/", 3, false),
    MULTIPLY("*", 3, false),
    POWER("^", 4, true),
    SQRT("sqrt", 3, false),
    SINE("sin", 3, true),
    COSINE("cos", 3, true),
    TANGENT("tan", 3, true),
    MAX("max", 3, true),
    MIN("min", 3, true),
    PAR_LEFT("(", 1, false),
    PAR_RIGHT(")", 1, false)

/*    class Add : Operator("+", 2, false)
    class Subtract : Operator("-", 2, false)
    class Multiply : Operator("*", 3, false)
    class Divide : Operator("/", 3, false)
    class Power: Operator("^", 4, true)*/
}