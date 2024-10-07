class Day23 {
    private val input = readInput(23)
    fun part1(): Int {
        val maze = Maze(input)
        maze.findPaths()
        return maze.longestPath()-1
    }

    fun part2(): Int {
        return 0
    }
}

class MazePath(startPath : List<Coords2D>) {

    constructor(uniquePath : Coords2D) : this(mutableListOf<Coords2D>(uniquePath))

    val steps = startPath.toMutableList()

    fun length(): Int {
        return steps.size
    }

    fun last(): Coords2D {
        return steps.last()
    }

    fun isFree(pos: Coords2D): Boolean {
        return !steps.contains(pos)
    }


}

class Maze(val input: List<String>) {
    val size = input.size
    var paths = listOf(MazePath(start()))
    val exit = exit()

    fun findPaths() {
        while(paths.any { it.last() != exit }) {
            val newPaths = paths.filter{
                it.last() != exit
            }.flatMap { buildNewPaths(it) }
                .toMutableList()

/*
            newPaths.removeIf {
                newPath ->
                    newPath.steps.filterIndexed { index, _ -> index < newPath.steps.size-90 }.any { step ->
                        newPaths.any { otherPath -> step == otherPath.last() }
                    }
            }
*/

            println(newPaths.size)
            paths = newPaths
        }
    }

    fun longestPath(): Int {
        return paths.map {it.length()}.max()
    }

    private fun buildNewPaths(mazePath: MazePath): List<MazePath> {
        val last = mazePath.last()
        val possiblePath = nextPath(last)
        return possiblePath
            .filter { value(it) != '#' }
            //.filter { next -> slipping(next, last) }
            .filter { pos -> mazePath.isFree(pos) }
            .map {
                val newPath = mazePath.steps.toMutableList()
                newPath.add(it)
                MazePath(newPath)
            }
    }


    private fun slipping(next: Coords2D, last: Coords2D): Boolean {
        val nextValue = value(next)
        return nextValue == '.'
                || (nextValue == '>' && next.x > last.x)
                || (nextValue == 'v' && next.y > last.y)
    }

    fun start(): Coords2D {
        return Coords2D(input[0].indexOf('.'), 0)
    }

    fun exit(): Coords2D {
        return Coords2D(input[input.size - 1].indexOf('.'), input.size - 1)
    }

    fun nextPath(pos: Coords2D): List<Coords2D> {
        return pos.neighbours(size)
    }

    fun value(pos : Coords2D) : Char {
        return input[pos.y][pos.x]
    }
}

data class Coords2D(val x: Int, val y: Int) {
    fun neighbours(size : Int): List<Coords2D> {
        val neighbours = mutableListOf<Coords2D>()
        if(x>1) neighbours.add(Coords2D(x - 1, y))
        if(x<size-1) neighbours.add(Coords2D(x + 1, y))
        if(y>1) neighbours.add(Coords2D(x, y - 1))
        if(y<size-1) neighbours.add(Coords2D(x, y + 1))
        return neighbours
    }
}
