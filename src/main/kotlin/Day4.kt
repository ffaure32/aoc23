import kotlin.math.pow

class Day4 {
    private val readInput = readInput(4)
    fun part1(): Int {
        return readInput.sumOf { computePows(it) }
    }

    fun part2(): Int {
        val length = readInput.map { intersectionsCount(it) }
        return countCards(length)
    }

    private fun countCards(length: List<Int>): Int {
        val cardCounts = mutableMapOf<Int, Int>()
        for (i in length.indices) {
            incrementCount(cardCounts, i, 1)
            for (j in i + 1..i + length[i]) {
                incrementCount(cardCounts, j, cardCounts[i]!!)
            }
        }
        return cardCounts.values.sum()
    }

    private fun incrementCount(cardCounts: MutableMap<Int, Int>, index: Int, increment: Int) {
        cardCounts[index] = cardCounts.getOrDefault(index, 0) + increment
    }

    private fun computePows(line: String): Int {
        val intersection = intersectionsCount(line)
        return 2.0.pow(intersection.toDouble() - 1).toInt()

    }

    private fun intersectionsCount(line: String): Int {
        val split = line.split("|")
        val winningNumbers = split[0].split(":")[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }.toSet()
        val ownNumbers = split[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }.toSet()
        val intersection = winningNumbers.intersect(ownNumbers)
        return intersection.size
    }

}