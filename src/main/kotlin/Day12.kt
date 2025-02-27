class Day12 {
    private val input = readInput(12)
    fun part1() : Int {
        return input.map {
            val (row, countsStr) = it.split(" ")
            Row(row, countsStr)
        }.sumOf {
            r -> r.unknownPermutations()
        }
    }
    fun part2() : Long {
        val input = readInput(12)

        val springRecords: List<SpringRecord> =
            input.mapTo(mutableListOf()) { line ->
                val (row, countsStr) = line.split(" ")
                val counts = countsStr.split(",").map(String::toInt)
                SpringRecord(row, counts)
            }

        return springRecords
            .map(SpringRecord::unfold)
            .sumOf { (row, counts) ->
                validCombinations(row, counts)
            }
    }


}
class Row(val initialSprings : String, val initialRecords : String, multiply : Int = 1) {
    val springs = IntRange(1, multiply-1).fold(initialSprings) { acc, _ -> "$acc?$initialSprings" }
    val records = IntRange(1, multiply-1).fold(initialRecords) { acc, _ -> "$acc,$initialRecords" }
    val unknowns = springs.count { it=='?'}
    val totalPlaces = records.split(',').sumOf { it.toInt() }
    val existingPlaces = springs.count { it=='#'}
    val validArrangement = ("\\.*#{"+records.replace(",", "}\\.+#{")+"}\\.*").toRegex()
    fun unknownPermutations(): Int {
        val toPlace = totalPlaces-existingPlaces
        val empty = unknowns-toPlace
        val init = mutableListOf<Char>()
        IntRange(1, toPlace).forEach{init.add('#')}
        IntRange(1, empty).forEach{init.add('.')}
        val possiblePermutations = permutations(init)

        return possiblePermutations!!.map {
            var possiblePermutation = springs
            it.forEach {
                c -> possiblePermutation = possiblePermutation.replaceFirst('?', c)
            }
            possiblePermutation
        }.count {
            validArrangement.matches(it)
        }
    }

    fun unknownPermutationsRec() : Int {
        return 0
    }

}

val permutationCache = mutableMapOf<List<Char>, Set<List<Char>>> ()
fun permutations(list: List<Char>): Set<List<Char>>? {
    if(permutationCache.containsKey(list)) {
        return permutationCache[list]
    }
    val result =  when {
        list.size <= 1 -> setOf(list)
        else ->
            permutations(list.drop(1))!!.map { perm ->
                (list.indices).map { i ->
                    perm.subList(0, i) + list.first() + perm.drop(i)
                }
            }.flatten().toSet()
    }
    permutationCache[list] = result
    return result

}

data class SpringRecord(val row: String, val brokenCounts: List<Int>)

fun SpringRecord.unfold() = SpringRecord(
    (0 until 5).joinToString("?") { row },
    (0 until 5).flatMap { brokenCounts }
)

fun validCombinations(row: String, brokenGroupLengths: List<Int>): Long {
    val cache = Array(row.length) {
        LongArray(brokenGroupLengths.size + 1) { -1L }
    }

    // from inclusive, to exclusive
    fun brokenGroupPossible(from: Int, to: Int): Boolean = when {
        to > row.length -> {
            // not enough springs remaining
            false
        }

        to == row.length -> {
            // all in range must not be marked as working
            (from until to).all { row[it] != '.' }
        }

        else -> {
            // all in range must not be marked as working,
            // and the following spring must not be marked as broken
            (from until to).all { row[it] != '.' } && row[to] != '#'
        }
    }

    fun compute(rowIndex: Int, brokenGroupIndex: Int): Long {
        if (rowIndex == row.length) {
            return if (brokenGroupIndex == brokenGroupLengths.size) 1 else 0
        }

        if (cache[rowIndex][brokenGroupIndex] != -1L) {
            return cache[rowIndex][brokenGroupIndex]
        }

        fun computeWorking(): Long =
            compute(rowIndex + 1, brokenGroupIndex)

        fun computeBroken(): Long {
            if (brokenGroupIndex == brokenGroupLengths.size) {
                return 0
            }
            // index of the end of the group, exclusive
            val endGroupIdx = rowIndex + brokenGroupLengths[brokenGroupIndex]

            if (!brokenGroupPossible(rowIndex, endGroupIdx)) {
                return 0
            }
            if (endGroupIdx == row.length) {
                return if (brokenGroupIndex == brokenGroupLengths.size - 1) 1 else 0
            }
            // set i to position after end of this group, including the working spring
            // that ends this group, and increment j
            return compute(endGroupIdx + 1, brokenGroupIndex + 1)
        }

        return when (val c = row[rowIndex]) {
            '.' -> computeWorking()
            '#' -> computeBroken()
            '?' -> computeWorking() + computeBroken()
            else -> throw RuntimeException("Illegal character in spring record: $c")
        }
            .also { cache[rowIndex][brokenGroupIndex] = it }
    }

    return compute(0, 0)
}