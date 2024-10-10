import java.util.*

class Day20 {
    private val input = readInput(20)
    fun part1() : Int {
        initialize()
        IntRange(1,1000).forEach{
            pressButton()
        }
        return count.count()
    }

    private fun initialize() {
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
            modules[moduleName] = module
        }
        modules.values.forEach { it.initModules() }
        modules.values.forEach { it.initInputModules() }
    }

    fun part2() : Int {
        initialize()
        var pressButtonCount = 0
        while(!pressButton()) {
            pressButtonCount +=1
            if(pressButtonCount % 100 == 0) {
                println(pressButtonCount)
            }
        }
        return pressButtonCount
    }
}

val modules = mutableMapOf<String, Module>()
val moduleQueue = ArrayDeque<Pair<Module,Pulse?>>()
val count = Count()

fun pressButton(): Boolean {
    var rxActivated = false
    modules["broadcast"]!!.receivePulse("button", Pulse.LOW)
    while(moduleQueue.isNotEmpty()) {
        val toSpread = moduleQueue.removeFirst()
        if(toSpread.first.name == "rx" && toSpread.second == Pulse.LOW) {
            println("*****************************************************************************************")
            rxActivated = true
        }
        if(toSpread.second != null)
            toSpread.first.spreadPulse(toSpread.second!!)
    }
    return rxActivated
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
        // println("$input -$pulse -> $name")
        count.add(pulse)
    }
    open fun initInputModules() {}
    fun initModuleNames(moduleNames: List<String>) {
        destinationModulesNames = moduleNames
    }

    fun initModules() {
        destinationModulesNames.forEach {
            destinationModules.add(modules[it]?:Module(it))
        }
    }
    fun spread(pulse: Pulse?) {
        moduleQueue.addLast(Pair(this, pulse))
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
        val inputNames = modules.values.filter {
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