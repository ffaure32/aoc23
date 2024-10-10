import kotlin.math.max
import kotlin.math.min

class Day21 {
    private val input = readInput(21)
    fun part1() : Int {
        return findPositions(input, 64)
    }
    fun part2() : Int {
        return findPositions(input, 26501365)
    }
}

fun findPositions(lines: List<String>, stepsCount: Int): Int {
    val garden = initGrid(lines)
    for (i in 1..stepsCount) {
        garden.step()
        // garden.print()
    }
    return garden.gardenPlots()
}

var startingPosition : Pair<Int, Int>? = null

fun findRocks(lineIndex: Int, line: String): Set<Pair<Int, Int>> {
    val rocksOnLine = mutableSetOf<Pair<Int,Int>>()
    var index = -1
    while (line.indexOf('#', index + 1).also { index = it } != -1) {
        rocksOnLine.add(Pair(lineIndex, index))
    }
    val sPos = line.indexOf('S')
    if(sPos >=0) {
        startingPosition = Pair(lineIndex, sPos)
    }
    return rocksOnLine
}

fun initGrid(lines: List<String>): Garden {
    val rocks = lines.flatMapIndexed { ind, it -> findRocks(ind, it) }.toSet()
    return Garden(lines.size, rocks, startingPosition!!)
}

class Garden(val gardenLength: Int, val rocks: Set<Pair<Int, Int>>, startingPosition: Pair<Int, Int>) {

    var elfPositions = mutableSetOf(startingPosition)
    fun step() {
        elfPositions = elfPositions.flatMap { neighbours(it) }.toMutableSet()
    }

    private fun neighbours(position: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val newNeighbours = mutableSetOf<Pair<Int, Int>>()
        newNeighbours.add(Pair(position.first-1, position.second))
        newNeighbours.add(Pair(position.first+1, position.second))
        newNeighbours.add(Pair(position.first, position.second-1))
        newNeighbours.add(Pair(position.first, position.second+1))
        return newNeighbours.filter { !rocks.contains(Pair(moduloNeg(it.first), moduloNeg(it.second))) }.toSet()
    }

    fun gardenPlots(): Int {
        return elfPositions.size
    }

    fun moduloNeg(num: Int): Int {
        if(num<0) {
            return gardenLength+(num%gardenLength)
        } else {
            return num%gardenLength
        }
    }
    fun print() {
        val minLine = min(0, elfPositions.map { it.first }.min())
        val maxLine = max(gardenLength-1, elfPositions.map { it.first }.max())
        val minX = min(0, elfPositions.map { it.second }.min())
        val maxX = max(gardenLength-1, elfPositions.map { it.second }.max())

        for (i in minLine..maxLine) {
            for (j in minX..maxX) {
                if(elfPositions.contains(Pair(i, j))) {
                    print('O')
                } else if(rocks.contains(Pair(moduloNeg(i), moduloNeg(j)))) {
                    print('#')
                } else {
                    print('.')
                }
            }
            println()
        }
        println("-".repeat(gardenLength))
    }
}
