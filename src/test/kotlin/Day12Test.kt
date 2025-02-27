import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {
    @Test
    fun part1() {
        assertEquals(7260, Day12().part1())
    }

    @Test
    fun rowPermutations() {
        assertEquals(1, Row("???.###","1,1,3").unknownPermutations())
        assertEquals(4, Row(".??..??...?##.","1,1,3").unknownPermutations())
        assertEquals(10, Row("?###????????","3,2,1").unknownPermutations())
    }

    @Test
    fun rowPermutationsPar2() {
        assertEquals(1, Row("???.###","1,1,3", 5).unknownPermutations())
        // assertEquals(16384, Row(".??..??...?##.","1,1,3", 5).unknownPermutations())
        // assertEquals(10, Row("?###????????","3,2,1", 5).unknownPermutations())
    }

    @Test
    fun part2() {
        assertEquals(1909291258644L, Day12().part2())
    }
}