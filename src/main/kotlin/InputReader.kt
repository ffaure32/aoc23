import java.io.File

fun readInput(day: Int)
        = File(ClassLoader.getSystemResource("day${day}.txt").file).readLines(Charsets.UTF_8)