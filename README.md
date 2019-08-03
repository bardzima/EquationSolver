# KotlinEquationSolver
A small but fast growing mathematical expressions parser based on the [Shunting-yard algorithm](https://en.wikipedia.org/wiki/Shunting-yard_algorithm).

Current version includes only the postfix notation (aka "Reverse Polish Notation" (RPN)) based solver, however other notations might be added later.

The parser recognizes basic arithmetic operators (+, -, /, *, ^), trigonometric functions (currently sin, cos, tan, but more will be added soon), and methods like max and min. The parser also recognizes common constants (e.g. 'pi' or 'e') and accepts custom symbols for variable, with 'x' being the default symbol.

The library is written in Kotlin, and uses Kotlin coroutines for parser optimization (which reduces the running time by 40-60% on average). Use *parseSuspend()* method if called from a coroutine scope.

# Usage
Use *Parser class to parse the expression string. Use *Solver class to solve the parsed equation.

# Examples
#### Example 1
```
val eqString = "33 + 4 * 2 / ((( 1 - 5 ) ^ 2) ^ 3)"
val equation = PostfixParser(eqString).parse()
println(equation.calculate())
```
#### Example 2
```
val eqString = "x + 3*(x+(4+7)) - (3-x)"
val equation = PostfixParser(eqString).parse()
println(equation.calculateFor(5.2))
```
#### Example 3
```
val eqString = "cos(x) * sin(pi)*max(2,3)"
val equation = PostfixParser(eqString, "p").parse()
println(equation.calculateFor(-10.23))
```


# License

    Copyright 2019 Temo Bardzimashvili

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
