import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day5Test {
    @Test
    fun part1() {
        assertEquals(579439039L, Day5().part1())
    }

    @Test
    fun part2() {

        runBlocking {
            assertEquals(7873084L, Day5().part2())
        }
    }

}