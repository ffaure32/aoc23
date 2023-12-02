import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day2Test {
    @Test
    fun part1() {
        assertEquals(2486, Day2().part1())
    }

    @Test
    fun testColorPower() {
        val color = Day2.Colors(mapOf(Pair("blue", 4), Pair("green", 2), Pair("red", 6)))
        assertEquals(48, color.power())
    }

    @Test
    fun part2() {
        assertEquals(87984, Day2().part2())
    }

}