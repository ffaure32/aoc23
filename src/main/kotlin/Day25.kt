
import org.jgrapht.Graph
import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph


class Day25 {
    private val input = readInput(25)
    fun part1() : Int {
        return findProductOfDisconnectedComponents(input)
    }

    private fun buildConnectedGraph(input: List<String>): Graph<String, DefaultEdge> {
        val structuredData = input.map {
            val parts = it.split(": ")
            val parent = parts[0]
            val children = parts[1].split(" ")
            parent to children
        }
        val graph: Graph<String, DefaultEdge> = SimpleGraph(DefaultEdge::class.java)
        structuredData.forEach {
            graph.addVertex(it.first)
            for (child in it.second) {
                graph.addVertex(child)
                graph.addEdge(it.first, child)
            }
        }
        return graph
    }

    fun findProductOfDisconnectedComponents(input: List<String>): Int {
        val graph = buildConnectedGraph(input)
        val swMinCut = StoerWagnerMinimumCut(graph)
        val minCut = swMinCut.minCut().size
        return minCut * (graph.vertexSet().size - minCut)
    }
}
