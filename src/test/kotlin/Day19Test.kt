import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day19Test {
    @Test
    fun testPart1() {
        assertEquals(2042, Day19().part1())
    }

    @Test
    fun testPart2() {
        assertEquals(136146366355609L, Day19().part2())
    }

    @Test
    fun testParsing() {
        val input = "px{a<2006:qkq,m>2090:A,rfg}"

        val workflow = parsePart(input)
        println(workflow)
        assertEquals("px", workflow.name)
        assertEquals(3, workflow.rules.size)
    }

    @Test
    fun acceptedRangeWithoutCondition() {
        val acceptedRange = AcceptedRange(Rule(TrueCondition(), "any"))
        assertTrue(acceptedRange.ranges.all { it.value == IntRange(1, 4000) })
    }

    @Test
    fun acceptedRangeWithCondition() {
        val acceptedRange = AcceptedRange(Rule(PartCondition('m','<',1500), "any"))
        assertTrue(acceptedRange.ranges.filter { it.key != 'm' }.all { it.value == IntRange(1, 4000) })
        assertEquals(IntRange(1, 1499), acceptedRange.ranges['m'])
    }

    @Test
    fun testAcceptedRangeSimpleWorfklow() {
        val input = "qz{a<1970:A,x>3109:A,a>2070:A,A}"

        val workflow = parsePart(input)
        workflow.acceptedRanges().forEach { println(it.ranges) }
    }

}