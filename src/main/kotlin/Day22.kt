import kotlin.math.min

class Day22 {
    private val input = readInput(22)
    fun part1() : Int {
        val pyramid = buildStablePyramid()
        return pyramid.countDisintegratable()
    }

    fun part2() : Int {
        val pyramid = buildStablePyramid()
        return pyramid.countFalls()
    }

    private fun buildStablePyramid(): Pyramid {
        val bricks = input.map {
            val coords = it.split("~")
            val leftCoords = buildCoords(coords[0])
            val rightCoords = buildCoords(coords[1])
            Brick(leftCoords, rightCoords)
        }
        val sortedBricks = bricks.sortedWith(compareBy(Brick::minZ).thenBy(Brick::hashCode))
        val pyramid = Pyramid(sortedBricks)
        pyramid.fall()
        return pyramid
    }

    private fun buildCoords(coordsString: String) : Coords3D {
        val split = coordsString.split(",")
        return Coords3D(split[0].toInt(), split[1].toInt(), split[2].toInt())
    }
}

data class Brick(val leftCoords: Coords3D, val rightCoords: Coords3D) {
    val occupiedCoords: Set<Coords3D>
    init{
        occupiedCoords = occupiedCoords()
    }
    fun minZ() : Int {
        return min(leftCoords.z, rightCoords.z)
    }
    fun down() : Brick {
        return Brick(this.leftCoords.down(), this.rightCoords.down())
    }

    private fun occupiedCoords() : Set<Coords3D> {
        val occupied = mutableSetOf<Coords3D>()
        for(i in leftCoords.x .. rightCoords.x) {
            for(j in leftCoords.y .. rightCoords.y) {
                for(k in leftCoords.z .. rightCoords.z) {
                    occupied.add(Coords3D(i, j, k))
                }
            }
        }
        return occupied.toSet()
    }
}


data class Coords3D( val x: Int, val y: Int, val z: Int) {
    fun down() : Coords3D {
        return Coords3D(x, y, z - 1)
    }
}

class Pyramid(val bricks: List<Brick>) {
    private val stableBricks = mutableListOf<Brick>()
    private val stableCoords = mutableSetOf<Coords3D>()

    fun fall() : Int {
        var count  = 0
        for (brick in bricks) {
            var fallingBrick = brick
            var hasFallen = false
            while (canFall(fallingBrick, stableCoords)) {
                fallingBrick = fallingBrick.down()
                hasFallen = true
            }
            if(hasFallen) count++
            stableBricks.add(fallingBrick)
            stableCoords.addAll(fallingBrick.occupiedCoords)
        }
        return count
    }

    fun countFalls() : Int {
        val count = stableBricks
            .map { brick -> stableBricks.filter { it != brick } }
            .sumOf { Pyramid(it).fall() }
        return count
    }

    fun countDisintegratable(): Int {
        val count = stableBricks
            .map { brick -> stableBricks.filter { it != brick } }
            .count { !Pyramid(it).canFall() }
        return count
    }

    private fun canFall() : Boolean {
        val occupiedCoords = mutableSetOf<Coords3D>()
        for (brick in bricks) {
            if (canFall(brick, occupiedCoords)) {
                return true
            }
            occupiedCoords.addAll(brick.occupiedCoords)
        }
        return false
    }
}

fun canFall(brick: Brick, stableCoords: Set<Coords3D>): Boolean {
    if (brick.minZ() == 1) {
        return false
    } else {
        val ifDown = brick.down()
        return (ifDown.occupiedCoords.intersect(stableCoords).isEmpty())
    }
}
