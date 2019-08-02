package com.temobard.equationsolver.parsers

import com.temobard.equationsolver.solvers.EquationNotation

interface EquationParser {
    fun parse(): EquationNotation
}