package io.github.shahrivari.jackrabbit

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import mu.KotlinLogging
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val logger = KotlinLogging.logger {}

class IntGraph(edges: Iterator<IntEdge>) : Iterable<IntEdge> {
    val nodes: IntArray get
    private val adjList = Int2ObjectOpenHashMap<IntAdjacency>()
    private val reporter = ProgressReporter(
            logger, 1_000_000)

    init {
        logger.info { "Reading edges..." }
        for (e in edges) {
            for (v in e.nodes)
                adjList.getOrPut(v) { IntAdjacency(v) }.addEdge(e)
            reporter.progress()
        }
        logger.info { "Sorting nodes..." }
        nodes = adjList.keys.toIntArray()
        nodes.sort()
        logger.info { "Optimizing adjacencies..." }
        adjList.values.forEach { it.sortAndTrim() }
        logger.info { "Graph is built: #nodes:${nodes.size} , #edges:${edgeCount()}" }
    }

    constructor(edges: Iterable<IntEdge>) : this(edges.iterator())

    constructor(path: String) : this(Files.lines(Paths.get(path))
                                         .filter { !it.startsWith("#") }
                                         .map { it -> IntEdge.parse(it) }.iterator())

    fun edgeCount() = adjList.values.map { it.ins.size }.sum()



    fun hasEdge(src: Int, dest: Int): Boolean {
        val adj = adjList[src] ?: return false
        return Arrays.binarySearch(adj.outs.elements(), dest) >= 0
    }

    fun adjacency(v: Int): IntAdjacency {
        val adj = adjList[v]
        require(adj != null) { "The node is not present: $v" }
        return adj
    }

    fun neighbors(v: Int): IntArray = adjacency(v).neighs.elements()

    override fun iterator(): Iterator<IntEdge> = EdgeIterator()

    fun saveAsText(path: String) = FileWriter("/tmp/a.txt").use { writer ->
        this.forEach { writer.write("${it.src}\t${it.dst}\n") }
    }

    inner class EdgeIterator : Iterator<IntEdge> {
        val vIter = nodes.iterator()
        var adj = if (vIter.hasNext()) adjList[vIter.next()] else null
        var wIter = if (adj != null) adj!!.outs.iterator() else null
        override fun hasNext(): Boolean {
            if (adj == null)
                return false
            while (!wIter!!.hasNext()) {
                if (!vIter.hasNext())
                    return false
                adj = adjList[vIter.next()]
                wIter = adj!!.outs.iterator()
                continue
            }
            return true
        }

        override fun next(): IntEdge {
            require(hasNext()) { "There is no element next!" }
            return IntEdge(adj!!.node, wIter!!.nextInt())
        }

    }

}


fun main(args: Array<String>) {
    var graph = LongGraph("/home/saeed/nets/google.txt")
    println("DONE!")
}