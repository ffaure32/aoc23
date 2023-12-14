class Day13 {
    private val input = readInput(13)
    private var lineIndex = 0
    private val patterns = mutableListOf<Pattern>()

    init {
        while(lineIndex<input.size) {
            patterns.add(Pattern(nextPatternInput()))
        }
    }
    private fun nextPatternInput(): List<String> {
        return input.drop(lineIndex).takeWhile(String::isNotEmpty).toList()
            .also { currentRange -> lineIndex += currentRange.size + 1 }
    }
    fun part1() : Int {
        return patterns.sumOf { p -> p.findReflection() }
    }
    fun part2() : Int {
        return patterns.sumOf { p -> p.findSmudge() }
    }
}

class Pattern(private val lines: List<String>, private val parent: Pattern? = null){
    private var vertical = false
    private var foundIndex = -1

    fun findReflection() : Int {
        val verticalValue = verticalReflection()
        return if(verticalValue == -1) {
            horizontalReflection()
        } else {
            vertical = true
            verticalValue
        }
    }

    fun verticalReflection(): Int {
        val rotatedLines = IntRange(0, lines[0].length-1).map { index ->
            lines.map { l -> l[index] }.toString()
        }
        return reflection(rotatedLines, true)
    }
    fun horizontalReflection(): Int {
        return reflection(lines, false) * 100
    }

    private fun reflection(toReflect : List<String>, verticalAttempt : Boolean): Int {
        val l = toReflect.size
        val mid = l / 2

        for(i in 0..<mid) {
            val startIndex = i+1
            val compareStart = toReflect.subList(0,startIndex) == toReflect.subList(startIndex, startIndex+startIndex).reversed()
            if(compareStart) {
                foundIndex = startIndex
                if(notSameAsParent(verticalAttempt))
                    return foundIndex
            }
            val endIndex= l-startIndex
            val compareEnd = toReflect.subList(endIndex,l) == toReflect.subList(endIndex-startIndex, endIndex).reversed()
            if(compareEnd) {
                foundIndex = endIndex
                if(notSameAsParent(verticalAttempt))
                    return foundIndex
            }

        }
        return -1
    }

    private fun notSameAsParent(verticalAttempt: Boolean) =
        parent == null || parent.foundIndex != foundIndex || parent.vertical != verticalAttempt

    fun findSmudge() : Int {
        findReflection()
        for(l in lines.indices) {
            for(c in 0..<lines[0].length) {
                val newLines = newPatternInput(l, c)
                val ref = Pattern(newLines, this).findReflection()
                if(ref > 0) {
                    return ref
                }
            }
        }
        return -1
    }

    private fun newPatternInput(line: Int, column: Int): MutableList<String> {
        val previous = lines[line][column]
        val new = if (previous == '.') '#' else '.'
        val newLines = lines.toMutableList()
        val newString = newLines[line].substring(0, column) + new + newLines[line].substring(column + 1)
        newLines[line] = newString
        return newLines
    }
}
