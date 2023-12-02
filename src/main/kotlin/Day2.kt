import kotlin.math.max

class Day2 {
    fun part1(): Int {
        val readInput = readInput(2)
        return readInput.map {
            buildGame(it)
        }.filter {
            !it.isNotValid()
        }.map { it.id }.sum()
    }

    fun part2(): Int {
        val readInput = readInput(2)
        return readInput.map {
            buildGame(it)
        }.sumOf {
            it.power()
        }
    }

    private fun buildGame(line: String): Game {
        val split = line.split(":")
        val gameId = split[0].split(" ")[1].toInt()
        val colorList = split[1].split(";").map {
            val pairs = it.split(",")
            Colors(pairs.associate {
                val colorPair = it.split(" ")
                Pair(colorPair[2], colorPair[1].toInt())
            })
        }
        return Game(gameId, colorList)
    }

    data class Game(val id: Int, val colors: List<Colors>) {
        fun isNotValid(): Boolean {
            return colors.any() { it.isNotValid() }
        }

        fun power(): Int {
            return maxByColor().power()
        }

        private fun maxByColor(): Colors {
            val max = mutableMapOf<String, Int>()
            colors.forEach {
                it.colors.entries.forEach { color ->
                    max[color.key] = max(color.value, max.getOrDefault(color.key, color.value))
                }
            }
            return Colors(max)
        }
    }

    data class Colors(val colors: Map<String, Int>) {
        fun power(): Int {
            return colors.values.reduce { acc, next -> acc * next }
        }

        fun isNotValid(): Boolean {
            return mapOf("red" to 12, "green" to 13, "blue" to 14)
                .any{ invalidColor(it.key, it.value)}
        }

        private fun invalidColor(color : String, colorMax : Int) : Boolean {
            return colors.getOrDefault(color, 0) > colorMax
        }
    }
}