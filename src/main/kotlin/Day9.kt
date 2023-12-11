class Day9 {
    private val input = readInput(9)

    fun part1() : Int {
        return compute { it }
    }

    fun part2() : Int {
        return compute { it.reversed() }
    }

    private fun compute(transform: (List<Int>) -> List<Int>): Int {
        val lines = input.map { line ->
            line.split(" ").map { it.toInt() }
        }
        val length = lines[0].size
        val multipliers = findMultipliers(length)
        return lines.sumOf {
            transform(it).mapIndexed { index,
                                       value ->
                multipliers.getOrDefault(index + 1, 1) * value
            }.sum()
        }
    }

    private fun findMultipliers(length: Int): MutableMap<Int, Int> {
        var currentOccurrences = IntRange(1, length).toList()
        val multipliers = mutableMapOf(length to currentOccurrences.last())
        for (i in length - 1 downTo 1) {
            val newList = mutableListOf(1)
            for (j in 1..<currentOccurrences.size - 1) {
                newList.add(newList[j - 1] + currentOccurrences[j])
            }
            currentOccurrences = newList
            val sign = if ((newList.size+length) % 2 == 0) 1 else -1
            multipliers[i] = newList.last() * sign
        }
        return multipliers
    }

}
