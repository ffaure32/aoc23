import java.util.*

class Day20 {
    private val input = readInput(20)
    fun part1() : Int {
        initialize()
        IntRange(1,1000).forEach{
            pressButton(it)
        }
        return count.count()
    }

    fun part2() : Long {
        initialize()
        var index = 0
        while(compute.isNotOver()) {
            pressButton(++index)
        }
        return compute.count()
    }

    private fun initialize() {
        compute.clear()
        input.map {
            val split = it.split(" -> ")
            val destinationModuleNames = split[1].split(", ")
            val moduleInfo = split[0]
            val moduleType = moduleInfo[0]
            var moduleName = moduleInfo.substring(1)
            val module = when (moduleType) {
                '%' -> {
                    FlipFlopModule(moduleName)
                }

                '&' -> {
                    ConjunctionModule(moduleName)
                }

                else -> {
                    moduleName = "broadcast"
                    BroadcastModule(moduleName)
                }
            }
            module.initModuleNames(destinationModuleNames)
            compute.modules[moduleName] = module
        }
        compute.modules.values.forEach { it.initModules() }
        compute.modules.values.forEach { it.initInputModules() }
        val rxTargetModule = compute.modules.values.first { it.destinationModulesNames.contains("rx") }
        compute.endWriters.addAll(compute.modules.values.filter { it.destinationModulesNames.contains(rxTargetModule.name) })

    }
}

class Compute() {
    val modules = mutableMapOf<String, Module>()
    val moduleQueue = ArrayDeque<Pair<Module,Pulse?>>()
    val endWriters = mutableSetOf<Module>()
    val endWritersIndexes = mutableSetOf<Int>()
    fun clear() {
        modules.clear()
        moduleQueue.clear()
        endWriters.clear()
        endWritersIndexes.clear()
    }

    fun isNotOver(): Boolean {
        return endWriters.size != endWritersIndexes.size
    }

    fun count() : Long {
        return findLCMOfListOfNumbers(compute.endWritersIndexes.toList().map{it.toLong()})
    }

}

var count = Count()
val compute = Compute()

fun pressButton(i: Int) {
    compute.modules["broadcast"]!!.receivePulse("button", Pulse.LOW)
    while(compute.moduleQueue.isNotEmpty()) {
        val toSpread = compute.moduleQueue.removeFirst()
        if(compute.endWriters.contains(toSpread.first) && toSpread.second == Pulse.HIGH) {
            compute.endWritersIndexes.add(i)
        }
        if(toSpread.second != null)
            toSpread.first.spreadPulse(toSpread.second!!)
    }
}

class Count(var low: Int = 0, var high: Int = 0) {
    fun add(pulse: Pulse) {
        if(pulse == Pulse.HIGH) {
            high+=1
        } else {
            low+=1
        }
    }
    fun count(): Int {
        return high*low
    }
}

open class Module(val name : String) {
    var destinationModulesNames: List<String> = listOf()
    val destinationModules: MutableList<Module> = mutableListOf()

    fun spreadPulse(pulseToSpread: Pulse) {
            destinationModules.forEach { it.receivePulse(name, pulseToSpread) }
    }

    open fun receivePulse(input: String, pulse: Pulse) {
        count.add(pulse)
    }
    open fun initInputModules() {}
    fun initModuleNames(moduleNames: List<String>) {
        destinationModulesNames = moduleNames
    }

    fun initModules() {
        destinationModulesNames.forEach {
            destinationModules.add(compute.modules[it]?:Module(it))
        }
    }
    fun spread(pulse: Pulse?) {
        compute.moduleQueue.addLast(Pair(this, pulse))
    }

    override fun toString(): String {
        return name
    }
}

class FlipFlopModule(name: String, var status : Boolean = false) : Module(name) {
    override fun receivePulse(input: String, pulse: Pulse) {
        super.receivePulse(input, pulse)
        if(pulse == Pulse.LOW) {
            if(status) {
                spread(Pulse.LOW)
            } else {
                spread(Pulse.HIGH)
            }
            status = status.not()
        } else {
            spread(null)
        }
    }
}

class ConjunctionModule(name: String) : Module(name) {
    val inputModules: MutableMap<String, Pulse> = mutableMapOf()
    override fun receivePulse(input: String, pulse: Pulse) {
        super.receivePulse(input, pulse)
        inputModules[input] = pulse
        spread(if (inputModules.values.all { it == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH)
    }

    override fun initInputModules() {
        val inputNames = compute.modules.values.filter {
            it.destinationModules.contains(this)
        }.map { it.name }
        inputNames.forEach{ inputModules[it] = Pulse.LOW }
    }
}

class BroadcastModule(name: String) : Module(name) {
    override fun receivePulse(input: String, pulse: Pulse) {
        super.receivePulse(input, pulse)
        spread(pulse)
    }
}

enum class Pulse {
    HIGH,
    LOW
}