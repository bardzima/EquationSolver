package com.temobard.equationsolver.parsers

import com.temobard.equationsolver.tokens.*
import com.temobard.equationsolver.tokens.Number
import java.util.*

abstract class PostfixBaseParser(protected val eqString: String) : EquationParser {
    protected var variableSymbol = "x"

    protected fun assignToken(tokenString: String): Token {
        //Check number
        val number = parseNumber(tokenString)
        if (number != null) return Number(number)

        //Check variable
        if (tokenString.equals(variableSymbol)) return Variable()

        //Check operator
        for (op in Operator.Type.values()) {
            if (op.value.equals(tokenString)) return Operator(op)
        }

        throw IllegalArgumentException("Error processing '$tokenString'")
    }

    private fun parseNumber(numString: String): Double? = numString.toDoubleOrNull()
}

inline fun <reified T> Stack<Token>.popOrNull(): T? {
    return try {
        pop() as T
    } catch (e: java.lang.Exception) {
        null
    }
}

inline fun <reified T> Stack<Token>.peekOrNull(): T? {
    return try {
        peek() as T
    } catch (e: java.lang.Exception) {
        null
    }
}