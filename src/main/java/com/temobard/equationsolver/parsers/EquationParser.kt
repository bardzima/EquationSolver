package com.temobard.equationsolver.parsers

import com.temobard.equationsolver.solvers.EquationSolver

interface EquationParser {
    fun parse(): EquationSolver
}