import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day11 {
    private val input = readInput(11)
    private val galaxies = mutableSetOf<Pair<Long,Long>>()
    init {
        input.forEachIndexed {
                i, line ->
            line.forEachIndexed {
                    j, char ->
                if(char == '#') {
                    galaxies.add(Pair(i.toLong(), j.toLong()))
                }
            }
        }
    }
    private val emptyLines = LongRange(0, input.size.toLong()-1)
        .filter{ line -> galaxies.none { g -> g.first == line } }.toList()
    private val emptyColumns = LongRange(0, input[0].length.toLong()-1)
        .filter{ col -> galaxies.none { g -> g.second == col } }.toList()
    fun part1() : Long {
        return compute(1)
    }

    fun part2() : Long {
        return compute(999999)
    }

    private fun compute(distance : Long): Long {
        val alreadyTreated = mutableSetOf<Set<Pair<Long, Long>>>()
        val lengthes = mutableListOf<Long>()
        galaxies.forEach { g1 ->
            galaxies.forEach { g2 ->
                val element = setOf(g1, g2)
                if (g1 !== g2 && !alreadyTreated.contains(element)) {
                    alreadyTreated.add(element)
                    var length = abs(g2.first - g1.first) + abs(g2.second - g1.second)
                    length += emptyLines.count {
                        it > min(g1.first, g2.first) && it < max(g1.first,g2.first)
                    } * distance
                    length += emptyColumns.count {
                        it > min(g1.second, g2.second) && it < max(g1.second, g2.second)
                    } * distance
                    lengthes.add(length)
                }
            }
        }
        return lengthes.sum()
    }

}
