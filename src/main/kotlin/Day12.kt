class Day12 {
    private val input = readInput(12)
    fun part1() : Int {
        return input.map {
            val split = it.split(" ")
            Row(split[0], split[1])
        }.sumOf {
            r -> r.unknownPermutations()
        }
    }
    fun part2() : Int {
        return 0
    }


}
class Row(val initialSprings : String, val initialRecords : String, multiply : Int = 1) {
    val springs = IntRange(1, multiply-1).fold(initialSprings) { acc, _ -> "$acc?$initialSprings" }
    val records = IntRange(1, multiply-1).fold(initialRecords) { acc, _ -> "$acc,$initialRecords" }
    val unknowns = springs.count { it=='?'}
    val totalPlaces = records.split(',').sumOf { it.toInt() }
    val existingPlaces = springs.count { it=='#'}
    val validArrangement = ("\\.*#{"+records.replace(",", "}\\.+#{")+"}\\.*").toRegex()
    fun unknownPermutations(): Int {
        println(springs)
        println(records)

        val toPlace = totalPlaces-existingPlaces
        val empty = unknowns-toPlace
        val init = mutableListOf<Char>()
        IntRange(1, toPlace).forEach{init.add('#')}
        IntRange(1, empty).forEach{init.add('.')}
        val possiblePermutations = permutations(init)
        println(validArrangement)
        return possiblePermutations.map {
            var possiblePermutation = springs
            it.forEach {
                c -> possiblePermutation = possiblePermutation.replaceFirst('?', c)
            }
            possiblePermutation
        }.count {
            validArrangement.matches(it)
        }
    }

}

fun <T> permutations(list: List<T>): Set<List<T>> = when {
    list.size > 100 -> throw Exception("You probably dont have enough memory to keep all those permutations")
    list.size <= 1 -> setOf(list)
    else ->
        permutations(list.drop(1)).map { perm ->
            (list.indices).map { i ->
                perm.subList(0, i) + list.first() + perm.drop(i)
            }
        }.flatten().toSet()
}
