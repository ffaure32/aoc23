import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day24Test {

    @Test
    fun initHailstone() {
        val hailstone = Hailstone("1, 4, 5 @ 1, -2, 5")
        assertEquals((-2).toFloat(), hailstone.a)
        assertEquals((6).toFloat(), hailstone.b)
    }

    @Test
    fun validIntersection() {
        val input = readInput(241)
        assertTrue(Hailstone(input[0]).intersect(Hailstone(input[1]), 7, 27))
        assertFalse(Hailstone(input[0]).intersect(Hailstone(input[4]), 7, 27))
        assertTrue(Hailstone(input[0]).intersect(Hailstone(input[2]), 7, 27))
    }



    @Test
    fun testPart1() {
        assertEquals(25261, Day24().part1())
    }

    @Test
    fun testPart2() {
        assertEquals(549873212220117L, Day24().part2())
    }

}