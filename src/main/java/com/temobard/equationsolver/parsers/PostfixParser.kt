package com.temobard.equationsolver.parsers

import com.temobard.equationsolver.solvers.PostfixSolver
import com.temobard.equationsolver.tokens.Delimiter
import com.temobard.equationsolver.tokens.Operator

/**
 * The postfix (Reverse Polish Notation) parser.
 * Uses the Shunting-yard algorithm to parse a mathematical expression
 * @param eqString String containing the expression
 */
class PostfixParser(
    eqString: String,
    variableSymbol: String = DEFAULT_VARIABLE_SYMBOL
) : PostfixBaseParser(eqString, variableSymbol) {

    override fun parse(): PostfixSolver {
        val eq = eqString.replace(" ", "")

        val splitters = ArrayList<String>()
        Operator.values().forEach { splitters.add(it.value) }
        Delimiter.types.forEach { splitters.add(it) }

        val breaks = ArrayList<String>().apply { add(eq) }
        for (s in splitters) {
            val a = ArrayList<String>()
            for (ind in 0 until breaks.size) {
                breaks[ind].let {
                    if (it.length > 1) {
                        a.addAll(breakString(breaks[ind], s))
                    } else a.add(it)
                }
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
}
