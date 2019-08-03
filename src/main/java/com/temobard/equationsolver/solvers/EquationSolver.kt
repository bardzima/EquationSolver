package com.temobard.equationsolver.solvers

interface EquationSolver {
    fun calculate(): Double
    fun calculateFor(value: Double): Double
}