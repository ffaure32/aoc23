class Day24 {
    private val hail = Hail(readInput(24), 200000000000000, 400000000000000)
    fun part1() : Int {
        return hail.countIntersections()
    }
    fun part2() : Long {
        val h0 = hail.hailstones[2]
        val h1 = hail.hailstones[3]
        val h2 = hail.hailstones[4]

        val p1 = h1.position - h0.position
        val v1 = h1.vector - h0.vector
        val p2 = h2.position - h0.position
        val v2 = h2.vector - h0.vector
        // 549873212220116
        // 549873212220117
        // 549873212220168
        val t1 = -(p1.cross(p2) * v2)/(v1.cross(p2) * v2)
        val t2 = -(p1.cross(p2) * v1)/(p1.cross(v2) * v1)

        val c1 = h1.position + t1 * h1.vector
        val c2 = h2.position + t2 * h2.vector

        val v = 1/(t2 - t1) * (c2 - c1)
        val p = c1 - t1 * v
        return (p.x + p.y + p.z).toLong()
    }
}

private operator fun Double.times(vector: Coords3DD): Coords3DD {
    return Coords3DD(this*vector.x, this*vector.y, this*vector.z)
}

class Hail(input: List<String>, val min : Long, val max : Long) {
    val hailstones = input.map { Hailstone(it) }

    fun countIntersections() : Int {
        var count = 0
        for(i in 0..< hailstones.size-1) {
            for (j in i+1..<hailstones.size) {
                if(hailstones.get(i).intersect(hailstones.get(j), min, max)) {
                    count++
                }
            }
        }
        return count
    }
}

data class Coords3DD( val x: Double, val y: Double, val z: Double) {
    operator fun minus(other: Coords3DD): Coords3DD {
        return Coords3DD(this.x - other.x, this.y -other.y, this.z - other.z)
    }

    operator fun plus(other: Coords3DD): Coords3DD {
        return Coords3DD(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    fun cross(other: Coords3DD): Coords3DD {
        return Coords3DD(this.y*other.z - this.z*other.y,
            this.z*other.x - this.x*other.z,
            this.x*other.y - this.y*other.x)
    }

    operator fun times(other: Coords3DD): Double {
        return (this.x * other.x+ this.y * other.y+ this.z * other.z)
    }
}

class Hailstone(input: String) {
    val position : Coords3DD
    val vector : Coords3DD
    val a : Float
    val b : Float

    init {
        val split = input.split("@")
        position = splitToCoords(split[0])
        vector = splitToCoords(split[1])
        a = vector.y.toFloat() / vector.x.toFloat()
        b = position.y.toFloat() - (a * position.x.toFloat())
    }

    fun intersect(other: Hailstone, min: Long, max: Long): Boolean {
        if(this.a == other.a) {
            return false
        }
        val x = (other.b - this.b) / (this.a - other.a)
        val y = this.a * x + this.b

        if(this.pastIntersection(x) || other.pastIntersection(x)) {
            return false
        }
        return (inTarget(x, min, max) && inTarget(y, min, max))
    }

    private fun inTarget(coord : Float, min: Long, max: Long) : Boolean {
        return coord>=min && coord<= max
    }

    private fun pastIntersection(intersection: Float): Boolean {
        return ((intersection-this.position.x.toFloat()) * this.vector.x.toFloat() <0)
    }


    companion object {
        fun splitToCoords(input : String) : Coords3DD {
            val posSplit = input.split(",")
                .map { it.trim() }
                .map { it.toDouble() }
            return Coords3DD(posSplit[0], posSplit[1], posSplit[2])
        }
    }
}
