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
        val findPositions = findPositions(input, 6).elfPositions.count()
        assertEquals(16, findPositions)
    }


    @Test
    fun part1() {
        assertEquals(3724, Day21().part1())
    }

    @Test
    fun part2() {
        assertEquals(620348631910321, Day21().part2())
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
        assertEquals(167004, findPositions.elfPositions.count())
    }

    @Test
    fun testNegModulo() {
        println(0%7)
        println(7%7)
        println(7+(-8%7))
    }
}