import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class Day3Test {

    @Test
    fun regexLine() {
        val regex = "([0-9.]+)".toRegex()
            assertTrue(regex.matches("1234"))
            assertFalse(regex.matches("12+34"))
            assertTrue(regex.matches("12.34"))
            assertTrue(regex.matches("......"))
            assertFalse(regex.matches(".../..."))
    }
    @Test
    fun part1() {
        assertEquals(526404, Day3().part1())
    }

    @Test
    fun testColorPower() {
        val color = Day2.Colors(mapOf(Pair("blue", 4), Pair("green", 2), Pair("red", 6)))
        assertEquals(48, color.power())
    }

    @Test
    fun part2() {
        assertEquals(84399773, Day3().part2())
    }

}