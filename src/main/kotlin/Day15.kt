class Day15 {
    private val input = readInput(15)
    fun part1() : Int {
        return input[0].split(",").sumOf{
                l -> reindeerHash(l)
        }
    }
    fun part2() : Int {
        val boxes = Boxes()
        input[0].split(",").forEach {
            instruction -> boxes.updateBox(instruction)
        }
        return boxes.focusingPower()
    }
}

fun reindeerHash(toHash: String): Int {
    return toHash.fold(0) { acc, c -> (acc+c.code) * 17 % 256 }

}

class Boxes {
    private val boxes = Array(256) { mutableListOf<Lens>() }
    fun updateBox(instruction : String) {
        if(instruction.contains('=')) {
            updateLens(instruction)
        } else {
            removeLens(instruction)
        }
    }

    private fun removeLens(instruction: String) {
        val label = instruction.removeSuffix("-")
        val box = reindeerHash(label)
        boxes[box].removeIf { l -> l.label == label }
    }

    private fun updateLens(instruction: String) {
        val split = instruction.split("=")
        val label = split[0]
        val focal = split[1].toInt()
        val box = reindeerHash(label)
        val existingLens = boxes[box].firstOrNull { l -> l.label == label }
        existingLens?.replaceFocal(focal) ?: boxes[box].add(Lens(label, focal))
    }

    fun focusingPower(): Int {
        return boxes.mapIndexed { index, lenses ->
            (index+1) * lenses.mapIndexed { lensIndex, lens -> (lensIndex+1) * lens.focal }.sum()
        }.sum()
    }
}

class Lens(val label : String, var focal : Int) {
    fun replaceFocal(newFocal : Int) {
        focal = newFocal
    }
}