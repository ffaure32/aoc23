class Day8 {
    private val input = readInput(8)
    private val directions = input[0]
    private val elementsRegex = "([A-Z0-9]{3})".toRegex()

    private val elements = input.subList(2, input.size).associate {
        val groups = elementsRegex.findAll(it).map(MatchResult::value).toList()
        groups[0] to Element(groups[0], groups[1], groups[2])
    }
    private val elementMap = ElementMap(elements)

    fun part1() : Int {
        return compute("AAA", "ZZZ")
    }

    fun part2() : Long {
        val startKeys = elements.filter { el -> el.key.endsWith("A") }.keys
        val rounds = startKeys.map { compute(it, "Z").toLong() }
        return findLCMOfListOfNumbers(rounds)
    }

    private fun compute(start: String, end: String): Int {
        elementMap.start(start)
        val direction = Direction(directions)
        while (!elementMap.isOver(end)) {
            val nextDirection = direction.next()
            elementMap.next(nextDirection)
        }
        return direction.finalIndex()
    }
}
class Direction(private val direction : String, private var index : Int = 0, private var round : Int = 0) {
    fun next() : Char {
        val nextChar = direction[index]
        updateIndex()
        return nextChar
    }

    private fun updateIndex() {
        index += 1
        if (index == direction.length) {
            index = 0
            round += 1
        }
    }

    fun finalIndex(): Int {
        return (round * direction.length) + index
    }
}

data class Element(val value : String, val left : String, val right : String)

val NilElement = Element("", "", "")
class ElementMap(private val elements : Map<String, Element>) {
    private var currentElement : Element = NilElement

    fun start(start : String) {
        currentElement = getElement(start)
    }
    fun isOver(end : String): Boolean {
        return currentElement === NilElement || currentElement.value.endsWith(end)
    }
    fun next(direction : Char) {
        val nextTarget = if(direction == 'L') currentElement.left else currentElement.right
        currentElement = getElement(nextTarget)
    }

    private fun getElement(key : String) : Element {
        return elements.getOrDefault(key, NilElement)
    }
}

