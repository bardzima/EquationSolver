package com.temobard.equationsolver.parsers

import com.temobard.equationsolver.tokens.*
import com.temobard.equationsolver.tokens.Number
import java.util.*

abstract class PostfixBaseParser(protected val eqString: String) : EquationParser {
    protected var variableSymbol = "x"

    protected fun infixToPostfix(tokens: List<String>): ArrayList<Token> {
        val stack = Stack<Token>()
        val output = ArrayList<Token>()

        for (tokenString in tokens) {
            if (tokenString.isEmpty()) continue
            when (val token = assignToken(tokenString)) {
                is Number, is Variable, is Constant -> output.add(token)
                is Delimiter -> {/*IGNORE*/}
                is Operator -> {
                    when (token.type) {
                        Operator.Type.PAR_LEFT -> stack.push(token)
                        Operator.Type.PAR_RIGHT -> {
                            var top = stack.popOrNull<Operator>()
                            while (top != null && top.type != Operator.Type.PAR_LEFT) {
                                output.add(top);
                                top = stack.popOrNull<Operator>()
                            }
                            if (top?.type != Operator.Type.PAR_LEFT)
                                throw java.lang.IllegalArgumentException("No matching left parenthesis.");
                        }
                        else -> {
                            var op2 = stack.peekOrNull<Operator>()
                            while (op2 != null) {
                                val c = token.type.rank.compareTo(op2.type.rank);
                                if (c < 0 || !token.type.rightAssociative && c <= 0) {
                                    output.add(stack.pop());
                                } else {
                                    break;
                                }
                                op2 = stack.peekOrNull<Operator>()
                            }
                            stack.push(token);
                        }
                    }
                }
            }
        }

        while (!stack.isEmpty()) {
            stack.peekOrNull<Operator>()?.let {
                if (it.type == Operator.Type.PAR_LEFT)
                    throw java.lang.IllegalArgumentException("No matching right parenthesis.");
            }
            val top = stack.pop();
            output.add(top);
        }

        return output
    }

    protected fun assignToken(tokenString: String): Token {

        //Check number
        val number = parseNumber(tokenString)
        if (number != null) return Number(number)

        //Check variable
        if (tokenString == variableSymbol) return Variable()

        //Check operator
        for (op in Operator.Type.values()) {
            if (op.value == tokenString) return Operator(op)
        }

        //Check constants
        for(con in Constant.ConstantType.values()) {
            if(tokenString == con.moniker) return Constant(con)
        }

        //Check Delimiters
        for(del in Delimiter.types) {
            if(tokenString == del) return Delimiter()
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