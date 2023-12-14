import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13Test {
    @Test
    fun part1() {
        assertEquals(39939, Day13().part1())
    }

    @Test
    fun part2() {
        assertEquals(32069, Day13().part2())
    }

    @Test
    fun testFullVertical() {
        val firstSamplePattern = Pattern(
            listOf(
                "#.##..##.",
                "..#.##.#.",
                "##......#",
                "##......#",
                "..#.##.#.",
                "..##..##.",
                "#.#.##.#.",
            )
        )
        assertEquals(5, firstSamplePattern.verticalReflection())
        assertEquals(300, firstSamplePattern.findSmudge())
    }

        @Test
    fun testFullHorizontal() {
        assertEquals(100, Pattern(listOf("..", "..", "..", "**")).horizontalReflection())
        assertEquals(200, Pattern(listOf("..", "**", "**", "..")).horizontalReflection())
        assertEquals(300, Pattern(listOf("..", "**", "..", "..", "**", "..")).horizontalReflection())
        assertEquals(400, Pattern(listOf("qsd", "..", "**", "..", "..", "**", "..")).horizontalReflection())
        val secondSamplePattern = Pattern(
            listOf(
                "#...##..#",
                "#....#..#",
                "..##..###",
                "#####.##.",
                "#####.##.",
                "..##..###",
                "#....#..#",
            )
        )
        assertEquals(400, secondSamplePattern.horizontalReflection())
        assertEquals(100, secondSamplePattern.findSmudge())
    }

    @Test
    fun testHorizontal() {
        assertEquals(200, Pattern(listOf("**", "..", "..", "**")).horizontalReflection())
        assertEquals(300, Pattern(listOf("***", "**", "..", "..", "**")).horizontalReflection())
        assertEquals(200, Pattern(listOf("**", "..", "..", "**", "***")).horizontalReflection())
    }


}