package com.temobard.equationsolver.parsers

import com.temobard.equationsolver.solvers.PostfixNotation
import com.temobard.equationsolver.tokens.*
import com.temobard.equationsolver.tokens.Number
import com.temobard.equationsolver.tokens.Operator
import com.temobard.equationsolver.tokens.OperatorType
import com.temobard.equationsolver.tokens.Variable
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

class PostfixParser(private val eqString: String) : EquationParser {

    private var variableSymbol = "x"

    constructor(eqString: String, variableSymbol: String) : this(eqString) {
        this.variableSymbol = variableSymbol
    }

    override fun parse(): PostfixNotation {
        val eq = eqString.replace(" ", "")

        val breaks = ArrayList<String>().apply { add(eq) }
        for (o in OperatorType.values()) {
            var ind = 0
            while (ind < breaks.size) {
                val newBreaks = breakString(breaks[ind], o.value)
                breaks.removeAt(ind)
                breaks.addAll(ind, newBreaks)
                ind += newBreaks.size
            }
        }

        return PostfixNotation(infixToPostfix1(breaks))
    }

    private fun breakString(input: String, delimeter: String): ArrayList<String> {
        val tokens = input.split(delimeter)
        val array = ArrayList<String>()
        for (t in tokens) {
            array.add(t)
            array.add(delimeter)
        }
        array.removeAt(array.size - 1)
        return array
    }

    private fun infixToPostfix(tokens: List<String>): ArrayList<Token> {
        val output = ArrayList<Token>()
        val operators = Stack<Token>()

        for (tokenString in tokens) {
            if (tokenString.isEmpty()) continue
            when (val token = assignToken(tokenString)) {
                is Number, is Variable -> output.add(token)
                is Operator -> {
                    var topOperator: Operator? = try {
                        operators.peek() as Operator
                    } catch (e: Exception) {
                        null
                    }
                    when (token.type) {
                        OperatorType.PAR_LEFT -> operators.push(token)
                        OperatorType.PAR_RIGHT -> {
                            while (topOperator != null && topOperator.type != OperatorType.PAR_LEFT) {
                                output.add(topOperator)
                                operators.pop()
                                topOperator = operators.peek() as Operator
                            }
                            operators.pop()
                        }
                        else -> {
                            while (topOperator != null && topOperator.type.rank > token.type.rank) {
                                output.add(topOperator)
                                operators.pop()
                                topOperator = operators.peek() as Operator
                            }
                            operators.push(token)
                        }

                    }
                }
            }
        }

        while (!operators.isEmpty()) {
            val operator = operators.pop()
            output.add(operator)
        }

        return output
    }

    private fun infixToPostfix1(tokens: List<String>): ArrayList<Token> {
//        val output = ArrayList<Token>()
//        val operators = Stack<Token>()

        val stack = Stack<Token>()
        val output = ArrayList<Token>()

        for (tokenString in tokens) {
            if (tokenString.isEmpty()) continue
            when (val token = assignToken(tokenString)) {
                is Number, is Variable -> output.add(token)
                is Operator -> {
                    when (token.type) {
                        OperatorType.PAR_LEFT -> stack.push(token)
                        OperatorType.PAR_RIGHT -> {
                            var top = stack.popOrNull<Operator>()
                            while (top != null && top.type != OperatorType.PAR_LEFT) {
                                output.add(top);
                                top = stack.popOrNull<Operator>()
                            }
                            if (top?.type != OperatorType.PAR_LEFT)
                                throw IllegalArgumentException("No matching left parenthesis.");
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
            val top = stack.pop();
//            if (!operators.ContainsKey(top)) throw new ArgumentException("No matching right parenthesis.");
            output.add(top);
        }

//        while (!operators.isEmpty()) {
//            val operator = operators.pop()
//            output.add(operator)
//        }

        return output
    }

    private fun assignToken(tokenString: String): Token {
        //Check number
        val number = parseNumber(tokenString)
        if (number != null) return Number(number)

        //Check variable
        if (tokenString.equals(variableSymbol)) return Variable()

        //Check operator
        for (op in OperatorType.values()) {
            if (op.value.equals(tokenString)) return Operator(op)
        }

        throw IllegalArgumentException("Error processing '$tokenString'")
    }

    private fun parseNumber(numString: String): Double? = numString.toDoubleOrNull()
}

private inline fun <reified T> Stack<Token>.popOrNull(): T? {
    return try {
        pop() as T
    } catch (e: java.lang.Exception) {
        null
    }
}

private inline fun <reified T> Stack<Token>.peekOrNull(): T? {
    return try {
        peek() as T
    } catch (e: java.lang.Exception) {
        null
    }
}