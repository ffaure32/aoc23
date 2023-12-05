import kotlin.math.max
import kotlin.math.min

class Day3 {
    private val readInput = readInput(3)
    private val lastIndex = readInput[0].length-1
    private val numbersRegex = "(\\d+)".toRegex()
    private val noSpecialCharactersRegex = "([0-9.]+)".toRegex()
    val pairs = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()

    fun part1(): Int {
        return readInput.mapIndexed {
            lineIndex, line ->
            val groups = numbersRegex.findAll(line)
            groups.map { group -> value(lineIndex, group.value, group.range) }.sum()
        }.sum()
    }

    fun part2(): Int {
        readInput.mapIndexed {
            lineIndex, line ->
            val groups = numbersRegex.findAll(line)
            groups.map { group -> stars(lineIndex, group.value, group.range) }.toList()
        }
        return pairs.filter { it.value.size>=2 }.map { it.value.multiply() }.sum()
    }

    private fun stars(lineIndex: Int, value: String, range: IntRange) {
        val prev = max(0, range.first - 1)
        val next = min(lastIndex, range.last + 1)
        val extendedRange = IntRange(prev, next)
        if (lineIndex > 0) {
            val indexOf = readInput[lineIndex - 1].substring(extendedRange).indexOf('*')
            if(indexOf>=0) {
                addPair(Pair(lineIndex - 1, prev + indexOf), value)
            }
        }
        if (lineIndex < readInput.size - 1) {
            val indexOf = readInput[lineIndex + 1].substring(extendedRange).indexOf('*')
            if(indexOf>=0) {
                addPair(Pair(lineIndex + 1, prev + indexOf), value)
            }
        }
        if (prev >= 0) {
            if(readInput[lineIndex][prev] == '*') {
                addPair(Pair(lineIndex, prev), value)
            }
        }
        if (next <= lastIndex) {
            if(readInput[lineIndex][next] == '*') {
                addPair(Pair(lineIndex, next), value)
            }
        }
    }

    private fun addPair(pair: Pair<Int, Int>, valueInt: String) {
        val values = pairs[pair]
        if (values == null) {
            pairs[pair] = mutableListOf(valueInt.toInt())
        } else {
            values.add(valueInt.toInt())
        }
    }

    fun value(lineIndex: Int, value: String, range: IntRange): Int {
        var surroundingChars = ""
        val prev = max(0, range.first - 1)
        val next = min(lastIndex, range.last + 1)
        val extendedRange = IntRange(prev, next)
        if (lineIndex > 0) {
            surroundingChars += readInput[lineIndex - 1].substring(extendedRange)
        }
        if (lineIndex < readInput.size - 1) {
            surroundingChars += readInput[lineIndex + 1].substring(extendedRange)
        }
        if (prev >= 0) {
            surroundingChars += readInput[lineIndex][prev]
        }
        if (next <= lastIndex) {
            surroundingChars += readInput[lineIndex][next]
        }
        return if (noSpecialCharactersRegex.matches(surroundingChars)) 0 else value.toInt()
    }
}