package com.temobard.equationsolver.parsers

import com.temobard.equationsolver.solvers.PostfixSolver
import com.temobard.equationsolver.tokens.*
import com.temobard.equationsolver.tokens.Number
import com.temobard.equationsolver.tokens.Operator
import com.temobard.equationsolver.tokens.Variable
import jdk.nashorn.internal.objects.NativeArray.forEach
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

class PostfixParser(eqString: String) : PostfixBaseParser(eqString) {

    constructor(eqString: String, variableSymbol: String) : this(eqString) {
        this.variableSymbol = variableSymbol
    }

    override fun parse(): PostfixSolver = runBlocking {
        parseSuspend()
    }

    suspend fun parseSuspend(): PostfixSolver {
        val breaks = breakAll(eqString)
        return PostfixSolver(infixToPostfix(breaks))
    }

    private suspend fun breakAll(eqString: String): ArrayList<String> = withContext(Dispatchers.Default) {
        val eq = eqString.replace(" ", "")

        val breakables = ArrayList<String>().apply { addAll(Delimiter.types) }
        Operator.Type.values().forEach { breakables.add(it.value) }

        val breaks = ArrayList<String>().apply { add(eq) }
        for (o in breakables) {
            val a = ArrayList<String>()
            for (ind in 0 until breaks.size) {
                breaks[ind].let {
                    if(it.length > 1) {
                        val job = async { breakString(it, o) }
                        a.addAll(job.await())
                    } else a.add(it)
                }
            }
            breaks.clear()
            breaks.addAll(a)
        }
        breaks
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
                if (it.type == Operator.Type.PAR_LEFT)
                    throw IllegalArgumentException("No matching right parenthesis.");
            }
            val top = stack.pop();
            output.add(top);
        }

        output
    }
}
