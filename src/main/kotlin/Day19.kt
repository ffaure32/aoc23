import kotlin.math.*

private const val INF_SIGN = '<'
private const val SUP_SIGN = '>'
private const val INPUT_WORKFLOW_NAME = "in"
private const val ACCEPTED_VALUE = "A"
val xmas = listOf('x', 'm', 'a', 's')
private const val RANGE_MIN = 1
private const val RANGE_MAX = 4000

class Day19 {
    private val input = readInput(19)
    fun part1(): Int {
        // forgotten commit on a lost machine, had to code from scratch for part 2...
        return 0
    }

    fun part2(): Long {
        val workflows = input.filter { it.indexOf('{') > 0 }.map { parsePart(it) }
        val ranges = findValidRanges(workflows)
        return ranges.flatten().map { it.combinations()}.sum() //.reduce { acc, range -> acc.merge(range) })
    }

    private fun findValidRanges(workflows: List<Workflow>): List<List<AcceptedRange>> {
        val ranges = workflows.map {
            val acceptedChildRanges = it.acceptedRanges()
            acceptedRanges(it, acceptedChildRanges, workflows)
        }
        return ranges
    }

    private fun findParentRanges(workflows: List<Workflow>, targetWorkflow: Workflow, childRanges: List<AcceptedRange>) : List<AcceptedRange>{
        val sourceWorkflow = workflows.first { w ->
            w.rules.any { r -> r.target == targetWorkflow.name }
        }
        val sourceRule = sourceWorkflow.rules.first { r -> r.target == targetWorkflow.name }
        val sourceAcceptedRange = sourceWorkflow.acceptedRange(sourceRule)

        val mergedRanges = childRanges.map { (it.merge(sourceAcceptedRange)) }
        return acceptedRanges(sourceWorkflow, mergedRanges, workflows)

    }

    private fun acceptedRanges(currentWorkflow: Workflow, acceptedRanges: List<AcceptedRange>, workflows: List<Workflow>): List<AcceptedRange> {
        return if (currentWorkflow.isRootWorkflow()) {
            acceptedRanges
        } else {
            findParentRanges(workflows, currentWorkflow, acceptedRanges)
        }
    }

}

fun parsePart(input: String): Workflow {
    val (name, rules) = input.split("{")
    val splitRules = rules.dropLast(1).split(",")
    return Workflow(name, splitRules.map {
        val rule = it.split(":")
        if (rule.size == 1) {
            Rule(TrueCondition(), rule[0])
        } else {
            Rule(buildPartCondition(rule[0]), rule[1])
        }
    }.reversed())
}

fun buildPartCondition(partString: String): Condition {
    return if (partString.contains(INF_SIGN)) {
        val (part, limit) = partString.split(INF_SIGN)
        PartCondition(part[0], INF_SIGN, limit.toInt())
    } else {
        val (part, limit) = partString.split(SUP_SIGN)
        PartCondition(part[0], SUP_SIGN, limit.toInt())
    }
}

data class Workflow(val name: String, val rules: List<Rule>) {
    fun acceptedRanges(): List<AcceptedRange> {
        val toReturn = rules.filter { it.target == ACCEPTED_VALUE }
            .map(::acceptedRange)
        return toReturn
    }

    fun acceptedRange(it: Rule): AcceptedRange {
        val acceptedRange = AcceptedRange(it)
        rules.subList(rules.indexOf(it) + 1, rules.size).forEach { r ->
            acceptedRange.applyRule(r)
        }
        return acceptedRange
    }

    fun isRootWorkflow(): Boolean {
        return name == INPUT_WORKFLOW_NAME
    }
}

class AcceptedRange(acceptedRule: Rule) {
    val ranges = xmas.associateWith { IntRange(RANGE_MIN, RANGE_MAX) }.toMutableMap()

    init {
        acceptedRule.condition.validRange(ranges)
    }

    fun applyRule(rule: Rule) {
        rule.condition.validReverseRange(ranges)
    }

    fun merge(otherRanges: AcceptedRange): AcceptedRange {
        xmas.forEach {
            val firstRange = ranges.getValue(it)
            val otherRange = otherRanges.ranges.getValue(it)
            mergeRanges(firstRange, otherRange, it, ranges)
        }
        return this
    }

    fun combinations(): Long {
        return ranges.values.map { (it.count()).toLong() }.multiply()
    }
}

data class Rule(val condition: Condition, val target: String)

interface Condition {
    fun validRange(ranges: MutableMap<Char, IntRange>)
    fun validReverseRange(ranges: MutableMap<Char, IntRange>)
}

data class PartCondition(val target: Char, val sign: Char, val limit: Int) : Condition {

    override fun validRange(ranges: MutableMap<Char, IntRange>) {
        val intRange = ranges.getValue(target)
        val otherRange =
            if (sign == INF_SIGN) {
                IntRange(RANGE_MIN, limit - 1)
            } else {
                IntRange(limit + 1, RANGE_MAX)
            }
        mergeRanges(intRange, otherRange, target, ranges)
    }

    override fun validReverseRange(ranges: MutableMap<Char, IntRange>) {
        val intRange = ranges.getValue(target)
        val otherRange =
            if (sign == INF_SIGN) {
                IntRange(limit, RANGE_MAX)
            } else {
                IntRange(RANGE_MIN, limit)
            }
        mergeRanges(intRange, otherRange, target, ranges)
    }

}

fun mergeRanges(firstRange: IntRange, otherRange: IntRange, target: Char, ranges: MutableMap<Char, IntRange>) {
    val newMin = max(firstRange.first, otherRange.first)
    val newMax = min(firstRange.last, otherRange.last)
    ranges[target] = IntRange(newMin, newMax)
}

class TrueCondition : Condition {
    override fun validRange(ranges: MutableMap<Char, IntRange>) = Unit
    override fun validReverseRange(ranges: MutableMap<Char, IntRange>) = Unit
}


