import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class Day5 {
    private val input = readInput(501)
    private val seeds = Regex("\\d+").findAll(input[0]).map(MatchResult::value).map(String::toLong).toList()

    private var lineIndex = 3
    private val intermediateMappings = IntRange(1, 7).map{ IntermediateRangeList(getNextRangeList()) }.toList()

    private val seedsWithRanges = (seeds.indices step 2).map { i -> seeds[i]..<seeds[i] + seeds[i + 1] }

    private fun getNextRangeList(): List<String> {
        return input.drop(lineIndex).takeWhile(String::isNotEmpty).toList()
            .also { currentRange -> lineIndex += currentRange.size + 2 }
    }

    fun part1() : Long = seeds.minOf {
        computeLocation(it)
    }

    suspend fun part2() : Long = withContext(Dispatchers.IO) {
        seedsWithRanges.map { range -> async {range.minOf(::computeLocation) }}.awaitAll().min()
    }

    private fun computeLocation(seed: Long): Long =
        intermediateMappings.fold(seed) { acc, next -> next.findDestination(acc) }

    class IntermediateRangeList(input : List<String>) {
        private val seeds : List<IntermediateRange> = input.map {
            val line = it.split(" ")
            IntermediateRange(line[0].toLong(), line[1].toLong(), line[2].toLong())
        }.sortedBy { il -> il.sourceRangeStart }

        fun findDestination(source: Long): Long {
            val seedRange = seeds.firstOrNull { it.sourceInRange(source) }
            return seedRange?.computeDestination(source) ?: source
        }
    }

    class IntermediateRange(private val destinationRangeStart: Long, val sourceRangeStart : Long,
                            rangeLength : Long) {
        private val range = LongRange(sourceRangeStart, sourceRangeStart+rangeLength)

        fun sourceInRange(source : Long): Boolean {
            return source in range
        }

        fun computeDestination(source : Long) : Long {
            return destinationRangeStart+source-sourceRangeStart
        }
    }
}
