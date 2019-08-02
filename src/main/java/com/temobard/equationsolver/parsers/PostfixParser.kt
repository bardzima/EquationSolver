package com.temobard.equationsolver.parsers

import com.temobard.equationsolver.solvers.PostfixSolver
import com.temobard.equationsolver.tokens.*
import com.temobard.equationsolver.tokens.Number
import com.temobard.equationsolver.tokens.Operator
import com.temobard.equationsolver.tokens.Variable
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

class PostfixParser(eqString: String) : PostfixBaseParser(eqString) {

    constructor(eqString: String, variableSymbol: String) : this(eqString) {
        this.variableSymbol = variableSymbol
    }

    override fun parse(): PostfixSolver {
        val eq = eqString.replace(" ", "")

        val breaks = ArrayList<String>().apply { add(eq) }
        for (o in Operator.Type.values()) {
            val a = ArrayList<String>()
            for (ind in 0 until breaks.size) {
                a.addAll(breakString(breaks[ind], o.value))
            }
            breaks.clear()
            breaks.addAll(a)
        }

        return PostfixSolver(infixToPostfix(breaks))
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
        val stack = Stack<Token>()
        val output = ArrayList<Token>()

        for (tokenString in tokens) {
            if (tokenString.isEmpty()) continue
            when (val token = assignToken(tokenString)) {
                is Number, is Variable -> output.add(token)
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
            stack.peekOrNull<Operator>()?.let {
                if(it.type == Operator.Type.PAR_LEFT)
                    throw IllegalArgumentException("No matching right parenthesis.");
            }
            val top = stack.pop();
            output.add(top);
        }

        return output
    }
}
