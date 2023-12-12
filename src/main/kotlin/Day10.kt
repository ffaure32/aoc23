class Day10 {
    private val input = readInput(10)
    private val start = findStart()
    val validPipes = mutableMapOf<Int, MutableSet<Int>>()
    val insidePipes = mutableMapOf<Int, MutableSet<Int>>()
    var counter = 1

    fun addPipe(pipe : Pipe) {
        validPipes[pipe.line]!!.add(pipe.column)
    }

    fun part1() : Int {
        computePipes()
        return counter/2
    }

    fun part2() : Int {
        var countInnerPipes = 0
        computePipes()
        input.forEachIndexed {
            i, it ->
            val validPipesOnLine = validPipes[i]
            var pipesCount = 0
            var opener = ' '
            it.forEachIndexed {
                j, _ ->
                if(validPipesOnLine!!.contains(j)) {
                    val value = input[i][j]
                    print(value)
                    when {
                        value == '|' -> {
                            pipesCount +=1
                        }
                        value == 'F' || value == 'L' || value == 'S' -> {
                            opener = value
                        }
                        value == 'J' && opener == 'F' -> {
                            pipesCount += 1
                        }
                        value == '7' && (opener == 'L' || opener == 'S') -> {
                            pipesCount += 1
                        }
                    }
                }
                else if(pipesCount %2 == 1) {
                    countInnerPipes +=1
                    print('*')
                } else {
                    print(' ')
                }
            }
            println()
        }
        return countInnerPipes
    }

    private fun computePipes() {
        input.forEachIndexed { i, _ -> validPipes[i] = mutableSetOf() }
        input.forEachIndexed { i, _ -> insidePipes[i] = mutableSetOf() }

        println(getPipe(start).value)
        var previousPipe = getPipe(start)
        addPipe(previousPipe)
        var currentPipe = startFirstNeighbour()
        addPipe(currentPipe)
        while (currentPipe.value != 'S') {
            val value = currentPipe.value
            when (value) {
                '|' -> {
                    if (previousPipe.line > currentPipe.line) {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.up())
                    } else {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.down())
                    }
                }

                '-' -> {
                    if (previousPipe.column > currentPipe.column) {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.left())
                    } else {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.right())
                    }
                }

                'L' -> {
                    if (previousPipe.line != currentPipe.line) {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.right())
                    } else {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.up())
                    }
                }

                'J' -> {
                    if (previousPipe.line != currentPipe.line) {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.left())
                    } else {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.up())
                    }
                }

                '7' -> {
                    if (previousPipe.line != currentPipe.line) {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.left())
                    } else {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.down())
                    }
                }

                'F' -> {
                    if (previousPipe.line != currentPipe.line) {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.right())
                    } else {
                        previousPipe = currentPipe
                        currentPipe = getPipe(currentPipe.down())
                    }
                }
            }
            counter += 1
            addPipe(currentPipe)
        }
    }

    fun startFirstNeighbour(): Pipe {
        return getPipe(start.first-1, start.second)
    }

    fun getPipe(pipe : Pair<Int, Int>) : Pipe {
        return getPipe(pipe.first, pipe.second)
    }

    fun getPipe(line : Int, column : Int) : Pipe {
        return Pipe(line, column, input[line][column])
    }

    private fun findStart(): Pair<Int, Int> {
        input.forEachIndexed { i, line ->
            val col = line.indexOf('S')
            if (col >= 0) {
                return Pair(i, col)
            }
        }
        throw IllegalStateException()
    }
}
data class Pipe(val line : Int, val column : Int, val value : Char) {
    fun down() = Pair(line+1, column)
    fun up() = Pair(line-1, column)
    fun left() = Pair(line, column-1)
    fun right() = Pair(line, column+1)
}
