fun List<Int>.multiply(): Int {
    return this.reduce{acc, next -> acc * next}
}