import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day21Test {
    @Test
    fun part1Test() {
        val input = """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
        """.trimIndent().split("\n")
        val findPositions = findPositions(input, 6)
        assertEquals(16, findPositions)
    }


    @Test
    fun part1() {
        assertEquals(3724, Day21().part1())
    }

    @Test
    fun part2() {
        assertEquals(8754, Day21().part2())
    }

    @Test
    fun part2Test() {
        val input = """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
        """.trimIndent().split("\n")
        val findPositions = findPositions(input, 500)
        assertEquals(167004, findPositions)
    }

    @Test
    fun testNegModulo() {
        println(0%7)
        println(7%7)
        println(7+(-8%7))
    }
}