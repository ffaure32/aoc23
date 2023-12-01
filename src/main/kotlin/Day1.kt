class Day1 {
    fun part1() : Int {
        return compute(::firstDigit, ::firstDigit, String::reversed)
    }

    fun part2() : Int {
        return compute(::firstLitteralDigit, ::lastLitteralDigit, String::toString)
    }

    fun compute(firstDigitFun: (String) -> Int, secondDigitFun: (String) -> Int, transformFun: (String) -> String): Int {
        val readInput = readInput(1)
        return readInput.map {
            val firstDigit = firstDigitFun(it)
            val lastDigit = secondDigitFun(transformFun(it))
            val toInt = "${firstDigit}${lastDigit}".toInt()
            toInt
        }.sum()
    }

    fun firstDigit(str : String) : Int {
        return str.first { it.isDigit() }.digitToInt()
    }

    val numbers = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7,
        "eight" to 8, "nine" to 9,
        "1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9)



    fun lastLitteralDigit(str: String): Int {
        return numbers.map {
            Pair(str.lastIndexOf(it.key), it.value)
        }.filter { it.first >=0 }.maxByOrNull { it.first }!!.second
    }

    fun firstLitteralDigit(str: String): Int {
        return numbers.map {
            Pair(str.indexOf(it.key), it.value)
        }.filter { it.first >= 0 }.minByOrNull { it.first }!!.second
    }


}