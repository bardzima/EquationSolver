package com.temobard.equationsolver.parsers

import com.temobard.equationsolver.solvers.PostfixSolver
import com.temobard.equationsolver.tokens.*
import com.temobard.equationsolver.tokens.Number
import com.temobard.equationsolver.tokens.Operator
import com.temobard.equationsolver.tokens.Variable
import java.lang.IllegalArgumentException
import java.util.*
import javax.swing.plaf.ListUI
import kotlin.collections.ArrayList

class PostfixParser_Obsolete(eqString: String) : PostfixBaseParser(eqString) {

    constructor(eqString: String, variableSymbol: String) : this(eqString) {
        this.variableSymbol = variableSymbol
    }

    override fun parse(): PostfixSolver {
        val eq = eqString.replace(" ", "")

        val breakables = ArrayList<String>()
        Operator.Type.values().forEach { breakables.add(it.value) }
        Delimiter.types.forEach { breakables.add(it) }

        val breaks = ArrayList<String>().apply { add(eq) }
        for (o in breakables) {
            val a = ArrayList<String>()
            for (ind in 0 until breaks.size) {
                a.addAll(breakString(breaks[ind], o))
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
