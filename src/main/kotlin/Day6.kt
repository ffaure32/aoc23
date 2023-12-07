class Day6 {
    private val input = readInput(6)
    private val durations = lineToLongs(input[0])
    private val distances = lineToLongs(input[1])

    fun part1(): Int {
        val paths = List(durations.size) { i -> Path(durations[i], distances[i]) }
        return paths.map { p -> p.countValidRuns() }.multiply()
    }

    fun part2(): Int {
        val mergedDuration = mergeList(durations)
        val mergedDistance = mergeList(distances)
        return Path(mergedDuration, mergedDistance).countValidRuns()
    }

    private fun mergeList(toMerge : List<Long>) : Long {
        return toMerge.fold("") { acc, l ->  acc+l.toString()}.toLong()
    }
}

data class Path(val duration : Long, val distance : Long) {
    fun countValidRuns() : Int {
        return LongRange(0, duration).map { (duration - it) * it }.filter { length -> length > distance }.size
    }
}