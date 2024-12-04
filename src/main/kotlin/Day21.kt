class Day21 {
    private val input = readInput(21)
    fun part1(): Int {
        return findPositions(input, 64).gardenPlots()
    }

    fun part2(): Long {

        val gridSize = input.size
        val stepCount = 26501365L
        val centerPos = (gridSize - 1) / 2
        val multiplier = (stepCount - centerPos) / gridSize
        val garden = findPositions(input, 2 * gridSize + centerPos)

        val upperEdge = garden.elfPositions.count { it.first in -gridSize * 2..<-gridSize && it.second in 0..<gridSize }
        val leftEdge = garden.elfPositions.count { it.first in 0..<gridSize && it.second in -2 * gridSize..<-gridSize }
        val downEdge = garden.elfPositions.count { it.first in 2 * gridSize..<3 * gridSize && it.second in 0..<gridSize }
        val rightEdge = garden.elfPositions.count { it.first in 0..<gridSize && it.second in 2 * gridSize..<3 * gridSize }
        val edges = upperEdge+leftEdge+downEdge+rightEdge

        val nearCenter = garden.elfPositions.count { it.first in gridSize..<2 * gridSize && it.second in 0..<gridSize }

        val insideBorderUpperLeft = garden.elfPositions.count { it.first in -gridSize..<0 && it.second in -gridSize..<0 }
        val insideBorderLowerLeft = garden.elfPositions.count { it.first in gridSize..<2 * gridSize && it.second in -gridSize..<0 }
        val insideBorderLowerRight = garden.elfPositions.count { it.first in gridSize..<2 * gridSize && it.second in gridSize..<2 * gridSize }
        val insideBorderUpperRight = garden.elfPositions.count { it.first in -gridSize..<0 && it.second in gridSize..<2 * gridSize }
        val insideBorders = insideBorderUpperLeft+insideBorderLowerLeft+insideBorderLowerRight+insideBorderUpperRight

        val outsideBorderUpperLeft = garden.elfPositions.count { it.first in -gridSize * 2..<-gridSize && it.second in -gridSize..<0 }
        val outsideBorderLowerLeft = garden.elfPositions.count { it.first in 2 * gridSize..<3 * gridSize && it.second in -gridSize..<0 }
        val outsideBorderLowerRight = garden.elfPositions.count { it.first in 2 * gridSize..<3 * gridSize && it.second in gridSize..<2 * gridSize }
        val outsideBorderUpperRight = garden.elfPositions.count { it.first in -gridSize * 2..<-gridSize && it.second in gridSize..<2 * gridSize }
        val outsideBorders = (outsideBorderUpperLeft+outsideBorderLowerLeft+outsideBorderLowerRight+outsideBorderUpperRight)

        val center = garden.elfPositions.count { it.first in 0..<gridSize && it.second in 0..<gridSize }
        return (multiplier - 1) * (multiplier - 1) * center + multiplier * multiplier * nearCenter + (multiplier - 1) * insideBorders + multiplier * outsideBorders + edges
    }
}

fun findPositions(lines: List<String>, stepsCount: Int): Garden {
    val garden = initGrid(lines)
    for (i in 1..stepsCount) {
        garden.step()
    }
    return garden
}

var startingPosition: Pair<Int, Int>? = null

fun findRocks(lineIndex: Int, line: String): Set<Pair<Int, Int>> {
    val rocksOnLine = mutableSetOf<Pair<Int, Int>>()
    var index = -1
    while (line.indexOf('#', index + 1).also { index = it } != -1) {
        rocksOnLine.add(Pair(lineIndex, index))
    }
    val sPos = line.indexOf('S')
    if (sPos >= 0) {
        startingPosition = Pair(lineIndex, sPos)
    }
    return rocksOnLine
}

fun initGrid(lines: List<String>): Garden {
    val rocks = lines.flatMapIndexed { ind, it -> findRocks(ind, it) }.toSet()
    return Garden(lines.size, rocks)
}

class Garden(val gardenLength: Int, val rocks: Set<Pair<Int, Int>>) {

    var elfPositions = mutableSetOf(startingPosition!!)
    fun step() {
        elfPositions = elfPositions.flatMap { neighbours(it) }.toMutableSet()
    }

    private fun neighbours(position: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val newNeighbours = mutableSetOf<Pair<Int, Int>>()
        newNeighbours.add(Pair(position.first - 1, position.second))
        newNeighbours.add(Pair(position.first + 1, position.second))
        newNeighbours.add(Pair(position.first, position.second - 1))
        newNeighbours.add(Pair(position.first, position.second + 1))
        return newNeighbours.filter { !rocks.contains(Pair(moduloNeg(it.first), moduloNeg(it.second))) }.toSet()
    }

    fun gardenPlots(): Int {
        return elfPositions.size
    }

    fun moduloNeg(num: Int): Int {
        return if (num < 0) {
            gardenLength + (num % gardenLength)
        } else {
            num % gardenLength
        }
    }
}
