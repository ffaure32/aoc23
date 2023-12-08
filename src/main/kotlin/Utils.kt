fun List<Int>.multiply(): Int {
    return this.reduce{acc, next -> acc * next}
}
fun List<Long>.multiply(): Long {
    return this.reduce{acc, next -> acc * next}
}