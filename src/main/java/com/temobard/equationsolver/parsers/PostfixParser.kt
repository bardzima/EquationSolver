package com.temobard.equationsolver.parsers

import com.temobard.equationsolver.solvers.PostfixSolver
import com.temobard.equationsolver.tokens.*
import com.temobard.equationsolver.tokens.Number
import com.temobard.equationsolver.tokens.Operator_deprecated
import com.temobard.equationsolver.tokens.Variable
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

/**
 * The postfix (Reverse Polish Notation) parser.
 * Uses the Shunting-yard algorithm to parse a mathematical expression
 * @param eqString String containing the expression
 */
class PostfixParser(eqString: String) : PostfixBaseParser(eqString) {

    constructor(eqString: String, variableSymbol: String) : this(eqString) {
        this.variableSymbol = variableSymbol
    }

    /**
     * Thread-blocking parser method.
     * Use this method only from within a regular thread.
     * @return parsed equation in Reversed Polish Notation format
     */
    override fun parse(): PostfixSolver = runBlocking {
        parseSuspend()
    }

    /**
     * Coroutine scope-friendly parser method.
     * Call this method from a coroutine scope.
     * @return parsed equation in Reversed Polish Notation format
     */
    suspend fun parseSuspend(): PostfixSolver {
        val splits = splitAll(eqString)
        return PostfixSolver(infixToPostfix(splits))
    }

    /**
     * Splits the expression string into tokens
     */
    private suspend fun splitAll(eqString: String): ArrayList<String> = withContext(Dispatchers.Default) {
        val eq = eqString.replace(" ", "")

        val splitters = ArrayList<String>().apply { addAll(Delimiter.types) }
//        Operator_deprecated.Type.values().forEach { splitters.Add(it.value) }
        Operator.values().forEach { splitters.add(it.value) }

        val breaks = ArrayList<String>().apply { add(eq) }
        for (splitter in splitters) {
            val a = ArrayList<String>()
            for (ind in 0 until breaks.size) {
                breaks[ind].let {
                    if(it.length > 1) {
                        val job = async { splitString(it, splitter) }
                        a.addAll(job.await())
                    } else a.add(it)
                }
            }
            breaks.clear()
            breaks.addAll(a)
        }
        breaks
    }

    /**
     * Splits string into a series of sub-strings
     */
    private fun splitString(input: String, delimeter: String): ArrayList<String> {
        val tokens = input.split(delimeter)
        val array = ArrayList<String>()
        for (t in tokens) {
            array.add(t)
            array.add(delimeter)
        }
        array.removeAt(array.size - 1)
        return array
    }

    /**
     * Experimental infix-to-postfix converter
     */
    private suspend fun infixToPostfixCoroutine_Experimental(tokens: List<String>): ArrayList<Token> =
        withContext(Dispatchers.IO) {
        val stack = Stack<Token>()
        val output = ArrayList<Token>()

        for (tokenString in tokens) {
            if (tokenString.isEmpty()) continue

            val job = async {
                assignToken(tokenString)
            }

            when (val token = job.await()) {
                is Number, is Variable -> output.add(token)
                is Operator_deprecated -> {
                    when (token.type) {
                        Operator_deprecated.Type.PAR_LEFT -> stack.push(token)
                        Operator_deprecated.Type.PAR_RIGHT -> {
                            var top = stack.popOrNull<Operator_deprecated>()
                            while (top != null && top.type != Operator_deprecated.Type.PAR_LEFT) {
                                output.add(top);
                                top = stack.popOrNull<Operator_deprecated>()
                            }
                            if (top?.type != Operator_deprecated.Type.PAR_LEFT)
                                throw IllegalArgumentException("No matching left parenthesis.");
                        }
                        else -> {
                            var op2 = stack.peekOrNull<Operator_deprecated>()
                            while (op2 != null) {
                                val c = token.type.precedence.compareTo(op2.type.precedence);
                                if (c < 0 || !token.type.rightAssociative && c <= 0) {
                                    output.add(stack.pop());
                                } else {
                                    break;
                                }
                                op2 = stack.peekOrNull<Operator_deprecated>()
                            }
                            stack.push(token)
                        }
                    }
                }
            }
        }

        while (!stack.isEmpty()) {
            stack.peekOrNull<Operator_deprecated>()?.let {
                if (it.type == Operator_deprecated.Type.PAR_LEFT)
                    throw IllegalArgumentException("No matching right parenthesis.");
            }
            val top = stack.pop()
            output.add(top)
        }

        output
    }
}
