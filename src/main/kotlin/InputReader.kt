import java.io.File

fun readInput(day: Int)
        = File(ClassLoader.getSystemResource("day${day}.txt").file).readLines(Charsets.UTF_8)

fun lineToLongs(line : String) : List<Long> = Regex("\\d+")
    .findAll(line).map(MatchResult::value).map(String::toLong).toList()

fun lineToInts(line : String) : List<Long> = Regex("\\d+")
    .findAll(line).map(MatchResult::value).map(String::toLong).toList()
