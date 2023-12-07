import kotlin.math.pow

class Day7 {
    private val input = readInput(7)

    fun part1(): Int {
        return compute()
    }

    fun part2(): Int {
        return compute(true)
    }

    private fun compute(pimped : Boolean = false) : Int {
        val hands : List<Hand> = input.map{
            val split = it.split(" ")
            Hand(split[0], split[1].toInt(), pimped)
        }
        return hands.sorted().mapIndexed{ index, hand -> hand.bid * (index+1)}.sum()
    }
}

enum class HandStrength(val hand: List<Int>, val strength : Int, private val strengthWithJoker : Int) {
    HIGH_CARD(listOf(1, 1, 1, 1, 1), 1, 2),
    PAIR(listOf(2, 1, 1, 1), 2, 4),
    TWO_PAIRS(listOf(2, 2, 1), 3, 5) {
        override fun pimpWithJoker(count: Int): Int {
            return if( count == 2) 6 else super.pimpWithJoker(count)
        }
    },
    THREE_OF_A_KIND(listOf(3, 1, 1), 4, 6),
    FULL(listOf(3, 2), 5, 7),
    FOUR_OF_A_KIND(listOf(4, 1), 6, 7),
    FIVE_OF_A_KIND(listOf(5), 7, 7);

    open fun pimpWithJoker(count : Int): Int {
        return if (count == 0) strength else strengthWithJoker
    }
}
val cards = mapOf(
    'T' to 10,
    'J' to 11,
    'Q' to 12,
    'K' to 13,
    'A' to 14
)

val cardsWithJoker = cards.entries.associate { e ->  if(e.key=='J') Pair(e.key, 1) else Pair(e.key, e.value)}

class Hand(private val hand: String, val bid: Int, private val pimped: Boolean = false) : Comparable<Hand> {
    private val handStrength =
        HandStrength.entries.first { hs -> hs.hand == hand.groupingBy { it }.eachCount().values.sortedDescending() }
    private val handScoreWithJoker = if(pimped) handStrength.pimpWithJoker(hand.count { it =='J' }) else handStrength.strength

    private val cardsScore = hand.indices.sumOf {
        cardRank(it) * 15.0.pow((hand.length - it)).toInt()
    }

    override fun compareTo(other: Hand): Int {
        return compareBy(Hand::handScoreWithJoker, Hand::cardsScore).compare(this, other)
    }

    private fun cardRank(i: Int): Int {
        val cardsMap = if (pimped) cardsWithJoker else cards
        return cardsMap[this.hand[i]] ?: this.hand[i].digitToInt()
    }
}