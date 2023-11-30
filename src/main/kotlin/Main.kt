import java.io.File

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}

fun readInput(day: Int)
        = File(ClassLoader.getSystemResource("day${day}.txt").file).readLines(Charsets.UTF_8)