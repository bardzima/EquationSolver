# KotlinEquationSolver
A small but fast growing mathematical expressions parser based on Shunting-yard algorithm.

Current version includes only the postfix notation (aka "Reverse Polish Notation" (RPN)) based solver, however other notations might be added later.

The parser recognizes basic arithmetic operators (+, -, /, *, ^), trigonometric functions (currently sin, cos, tan, but more will be added soon), and methods like max and min.

The library is written in Kotlin, and uses Kotlin coroutines for parser optimization (which reduces the running time by 40-60% on average).

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
