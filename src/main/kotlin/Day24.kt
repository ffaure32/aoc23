class Day24 {
    private val input = readInput(8)
    fun part1() : Int {
        return 0
    }
    fun part2() : Int {
        return 0
    }
}

class Hailstone(input: String) {
    private val position : Coords3D
    private val vector : Coords3D
    init {
        val split = input.split("@")
        position = splitToCoords(split[0])
        vector = splitToCoords(split[1])
    }

    companion object {
        fun splitToCoords(input : String) : Coords3D {
            val posSplit = input.split(",")
                .map { it.trim() }
                .map { it.toInt() }
            return Coords3D(posSplit[0], posSplit[1], posSplit[2])
        }
    }
}
