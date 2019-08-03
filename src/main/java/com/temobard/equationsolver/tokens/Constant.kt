package com.temobard.equationsolver.tokens

class Constant(val type: ConstantType) : Token {

    enum class ConstantType(val moniker: String, val value: Double) {
        PI("pi", kotlin.math.PI),
        E("e", kotlin.math.E)
    }
}