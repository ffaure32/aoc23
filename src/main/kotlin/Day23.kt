class Day23 {
    private val input = readInput(23)
    fun part1(): Int {
        val maze = Maze(input)
        maze.findPaths()
        return maze.longestPath()-1
    }

    fun part2(): Int {
        val withoutSplip = input.map{ it.replace('>', '.').replace('v', '.')}
        val maze = SegmentChunk(withoutSplip)
        return maze.length()
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

data class Segment(val target : Coords2D, val length : Int) {}
data class SegmentPath(val intersections : List<Coords2D>, val length : Int)

class SegmentChunk(val input: List<String>) {
    val finishedSegments = mutableSetOf<SegmentPath>();
    val size = input.size
    val exit = exit()
    val intersections = buildIntersections()
    val segments = buildSegments()
    val finished = buildPath()

    fun buildSegments(): Map<Coords2D, Set<Segment>> {
        val segmentsMap = mutableMapOf<Coords2D, Set<Segment>>()
        intersections.forEach {
            computeIntersection(it, segmentsMap)
        }
        return segmentsMap
    }

    fun computeIntersection(
        it: Coords2D,
        segmentsMap: MutableMap<Coords2D, Set<Segment>>
    ) {
        val segments = mutableSetOf<Segment>()
        it.neighbours(size).filter { value(it) == '.' }.forEach { n ->
            var length = 1
            var nextPath = n
            val path = mutableListOf<Coords2D>()
            path.add(it)
            while (!intersections.contains(nextPath)) {
                path.add(nextPath)
                nextPath = nextPath.neighbours(size).first { value(it) == '.' && !path.contains(it) }
                length++
            }
            segments.add(Segment(nextPath, length))
        }
        segmentsMap[it] = segments
    }

    fun length() : Int {
        return finished.maxOf { sp -> sp.length }
    }
    fun buildPath(): MutableSet<SegmentPath> {
        var segmentPaths = mutableSetOf<SegmentPath>()
        val start = segments[start()]
        start!!.forEach {
            segmentPaths.add(SegmentPath(listOf(start(), it.target), it.length))
        }
        recSegment(segmentPaths)
/*
        while(segmentPaths.isNotEmpty()) {
            val newSegments = segmentPaths.flatMap { sp ->
                val newSegments = segments[sp.intersections.last()]
                newSegments!!.filter { s -> !sp.intersections.contains(s.target) }.map {
                    SegmentPath(sp.intersections.plus(it.target), sp.length + it.length)
                }
            }
            segmentPaths = newSegments.filter { sp -> sp.intersections.last() != exit }.toMutableSet()
            finishedSegments.addAll(newSegments.filter { sp -> sp.intersections.last() == exit })
        }
*/
        return finishedSegments
    }

    fun recSegment(segmentPaths : Set<SegmentPath>) {
        val newSegments = segmentPaths.flatMap { sp ->
            val newSegments = segments[sp.intersections.last()]
            newSegments!!.filter { s -> !sp.intersections.contains(s.target) }.map {
                SegmentPath(sp.intersections.plus(it.target), sp.length + it.length)
            }
        }
        finishedSegments.addAll(newSegments.filter { sp -> sp.intersections.last() == exit })
        val newSegmentPaths = newSegments.filter { sp -> sp.intersections.last() != exit }.toMutableSet()
        if(newSegmentPaths.isNotEmpty()) {
            recSegment(newSegmentPaths)
        }
    }
    fun buildIntersections(): MutableSet<Coords2D> {
        val intersections = mutableSetOf<Coords2D>()
        intersections.add(start())
        intersections.add(exit())
        for (x in input.indices) {
            for (y in input.indices) {
                val coords2D = Coords2D(x, y)
                if (value(Coords2D(x, y)) == '.' && coords2D.neighbours(size).filter { value(it) == '.' }.size > 2) {
                    intersections.add(coords2D)
                }
            }
        }
        return intersections
    }

    fun start(): Coords2D {
        return Coords2D(input[0].indexOf('.'), 0)
    }

    fun exit(): Coords2D {
        return Coords2D(input[input.size - 1].indexOf('.'), input.size - 1)
    }

    fun value(pos : Coords2D) : Char {
        return input[pos.y][pos.x]
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
            }.flatMap {
                buildNewPaths(it) }
                .toMutableList()
            paths = newPaths
        }
    }

    fun longestPath(): Int {
        return paths.maxOf { it.length() }
    }

    private fun buildNewPaths(mazePath: MazePath): List<MazePath> {
        val last = mazePath.last()
        val possiblePath = nextPath(last)
        return possiblePath
            .filter { value(it) != '#' }
            .filter { next -> slipping(next, last) }
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
        if(x>0) neighbours.add(Coords2D(x - 1, y))
        if(x<size-1) neighbours.add(Coords2D(x + 1, y))
        if(y>0) neighbours.add(Coords2D(x, y - 1))
        if(y<size-1) neighbours.add(Coords2D(x, y + 1))
        return neighbours
    }
}
