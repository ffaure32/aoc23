class Day16 {
    private val input = readInput(16)
    fun part1() : Int {
        val beamer = Beamer(input)
        return beamer.ticks()
    }
    fun part2() : Int {
        val allBeamers = mutableListOf<Beamer>()
        IntRange(0, input.size-1).forEach {
            allBeamers.add(Beamer(input, Beam(Pair(0, it), 'S')))
            allBeamers.add(Beamer(input, Beam(Pair(input.size-1, it), 'N')))
            allBeamers.add(Beamer(input, Beam(Pair(it, 0), 'R')))
            allBeamers.add(Beamer(input, Beam(Pair(it, input.size-1), 'L')))
        }
        return allBeamers.maxOf { it.ticks() }
    }
}

class Beamer(val input : List<String>, start: Beam = Beam(Pair(0, 0), 'R')) {
    private val energizedBeams = mutableSetOf<Beam>()
    private var beams = mutableSetOf<Beam>()
    init {
        beams.add(start)
        energizedBeams.add(start)
    }

    fun ticks(): Int {
        while(tick()) {
            continue
        }
        return energizedCount()
    }
    fun tick(): Boolean {
        val newBeams = mutableSetOf<Beam>()
        beams.forEach {
            val createdBeams = it.move(input)
            createdBeams.filter { b -> keepBeam(b) }.forEach { b -> newBeams.add(b) }
        }
        beams = newBeams
        return energizedBeams.addAll(newBeams)
    }

    fun print() {
        for(i in input.indices) {
            for(j in input.indices) {
                if(energizedBeams.any { it.coords.first == i && it.coords.second == j }) print('#') else print ('.')
            }
            println()
        }
    }
    private fun energizedCount(): Int {
        return energizedBeams.map { it.coords }.toSet().size
    }
    private fun keepBeam(beam : Beam) : Boolean {
        return !(energizedBeams.contains(beam) || outsideMatrix(beam.coords))
    }

    private fun outsideMatrix(coords: Pair<Int, Int>): Boolean {
        return coords.first<0 || coords.first>=input.size || coords.second<0 || coords.second>=input.size
    }
}

data class Beam(var coords : Pair<Int, Int>, val direction : Char) {
    fun move(input: List<String>): Set<Beam> {
        val encountered = input[coords.first][coords.second]
        val newInstances = mutableSetOf<Beam>()
        when (direction) {
            'R' -> {
                when (encountered) {
                    '\\' -> addNew(newInstances,'S')
                    '/' -> addNew(newInstances,'N')
                    '|' -> {
                        addNew(newInstances, 'S')
                        addNew(newInstances,'N')
                    }
                    else -> addNew(newInstances,'R')
                }
            }
            'L' -> {
                when (encountered) {
                    '\\' -> addNew(newInstances,'N')
                    '/' -> addNew(newInstances,'S')
                    '|' -> {
                        addNew(newInstances, 'S')
                        addNew(newInstances,'N')
                    }
                    else -> addNew(newInstances,'L')
                }
            }
            'N' -> {
                when (encountered) {
                    '\\' -> addNew(newInstances,'L')
                    '/' -> addNew(newInstances,'R')
                    '-' -> {
                        addNew(newInstances, 'L')
                        addNew(newInstances,'R')
                    }
                    else -> addNew(newInstances,'N')
                }
            }
            'S' -> {
                when (encountered) {
                    '.' -> addNew(newInstances,'S')
                    '\\' -> addNew(newInstances,'R')
                    '/' -> addNew(newInstances,'L')
                    '-' -> {
                        addNew(newInstances, 'L')
                        addNew(newInstances,'R')
                    }
                    else -> addNew(newInstances, 'S')
                }
            }
        }
        newInstances.forEach { it.nextMove() }
        return newInstances
    }

    private fun addNew(newInstances: MutableSet<Beam>, direction: Char) {
        newInstances.add(newInstance(direction))
    }

    private fun newInstance(direction : Char): Beam {
        return Beam(Pair(coords.first, coords.second), direction)
    }
    private fun nextMove() {
        when (direction) {
            'L' -> {
                coords = Pair(coords.first, coords.second-1)
            }
            'R' -> {
                coords = Pair(coords.first, coords.second+1)
            }
            'N' -> {
                coords = Pair(coords.first-1, coords.second)
            }
            'S' -> {
                coords = Pair(coords.first+1, coords.second)
            }
        }
    }
}
