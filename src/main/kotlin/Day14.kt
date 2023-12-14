class Day14 {
    private val input = readInput(14)

    fun part1() : Int {
        val newLines = rollBoulders(input)
        return sumBoulders(newLines)

    }

    private fun sumBoulders(newLines: List<String>): Int {
        return newLines.mapIndexed{
            i, l -> (newLines.size-i) * l.count { c-> c== 'O' }
        }.sum()
    }

    private fun rollBoulders(lines: List<String>) : List<String> {
        return pivot(rollPivotedBoulders(pivot(lines)))
    }

    private fun rollPivotedBoulders(rotatedLines: List<String>): MutableList<String> {
        val newLines = mutableListOf<String>()
        rotatedLines.forEach { line ->
            newLines.add(rollLine(line))
        }
        return newLines
    }

    private fun rollLine(line: String): String {
        var end = ""
        var lineToShrink = line
        while (end.length < line.length) {
            val nextWall = lineToShrink.indexOf('#')
            val toNextWall = if (nextWall >= 0) lineToShrink.substring(0, nextWall) else lineToShrink
            val count = toNextWall.count { it == 'O' }
            val length = toNextWall.length
            end += "O".repeat(count)
            end += ".".repeat(length - count)
            if (nextWall >= 0) {
                end += '#'
            }
            lineToShrink = lineToShrink.substring(nextWall + 1)
        }
        return end
    }

    fun part2() : Int {
        var boulders = input
        val cyclesCount = 1000000000
        val alreadySeenGrids = mutableMapOf<List<String>, Int>()
        for(i in 1..cyclesCount) {
            boulders = cycle(boulders)
            if(alreadySeenGrids.containsKey(boulders)) {
                val firstOccurrence = alreadySeenGrids[boulders]!!
                val valueIndex = (cyclesCount - i) % (firstOccurrence - i) + firstOccurrence
                val targetLines = alreadySeenGrids.filter { it.value == valueIndex }.map { it.key }.first()
                return sumBoulders(targetLines)
            }
            alreadySeenGrids[boulders] = i
        }
        return 0
    }

    private fun cycle(boulders: List<String>): List<String> {
        var newBoulders = boulders
        for (i in 1..4) {
            val matrix = rollBoulders(newBoulders)
            newBoulders = rotateClockwise(matrix)
        }
        return newBoulders
    }

    private fun pivot(matrix : List<String>) : List<String> {
        return IntRange(0, matrix[0].length - 1).map { index ->
            matrix.map { l -> l[index] }.joinToString("")
        }
    }

    private fun rotateClockwise(matrix : List<String>) : List<String> {
        return rotateArray(matrix.map { s -> s.toCharArray() }.toTypedArray())
            .map { line -> String(line)}.toList()
    }

    private fun rotateArray(originalArray : Array<CharArray>) : Array<CharArray> {
        val newArray = Array(originalArray.size) { CharArray(originalArray[0].size) }
        for (row in originalArray.indices) {
            for(col in 0..<originalArray[0].size)
            {
                newArray[ col ][ originalArray.size - 1 - row ] = originalArray[ row ][ col ]
            }
        }
        return newArray
    }
}

