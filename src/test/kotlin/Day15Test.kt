import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day15Test {
    @Test
    fun part1() {
        assertEquals(513158, Day15().part1())
    }

    @Test
    fun part2() {
        assertEquals(200277, Day15().part2())
    }

    @Test
    fun hashAlgorithmTest() {
        val initialHashIsZero = reindeerHash("HASH")
        assertEquals(52, initialHashIsZero)
        assertEquals(30, reindeerHash("rn=1"))
    }
}