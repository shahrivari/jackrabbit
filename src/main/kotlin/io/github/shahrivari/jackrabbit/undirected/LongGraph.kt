package io.github.shahrivari.jackrabbit.undirected

import io.github.shahrivari.jackrabbit.LongEdge
import io.github.shahrivari.jackrabbit.ProgressReporter
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import mu.KotlinLogging
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val logger = KotlinLogging.logger {}


class LongGraph(edges: Iterator<LongEdge>) : Iterable<LongEdge> {
    val nodes: LongArray get
    private val neighbors = Long2ObjectOpenHashMap<LongArrayList>()
    private val reporter = ProgressReporter(
            logger, 1_000_000)

    init {
        logger.info { "Reading edges..." }
        for (e in edges) {
            neighbors.getOrPut(e.src) { LongArrayList() }.add(e.dst)
            neighbors.getOrPut(e.dst) { LongArrayList() }.add(e.src)
            reporter.progress()
        }
        logger.info { "Sorting nodes..." }
        nodes = neighbors.keys.toLongArray()
        nodes.sort()
        logger.info { "Optimizing adjacencies..." }
        neighbors.values.forEach { sortAndTrim(it) }
        logger.info { "Graph is built: #nodes:${nodes.size} , #edges:${edgeCount()}" }
    }

    constructor(edges: Iterable<LongEdge>) : this(edges.iterator())

    constructor(path: String) : this(Files.lines(Paths.get(path))
                                         .filter { !it.startsWith("#") }
                                         .map { it -> LongEdge.parse(it) }.iterator())

    fun edgeCount() = neighbors.values.map { it.size }.sum() / 2


    fun hasEdge(src: Long, dest: Long): Boolean {
        val neighs = neighbors[src] ?: return false
        return Arrays.binarySearch(neighs.elements(), dest) >= 0
    }

    fun neighbors(v: Long): LongArray = neighbors[v].elements()

    override fun iterator(): Iterator<LongEdge> = EdgeIterator()

    fun saveAsText(path: String) = FileWriter("/tmp/a.txt").use { writer ->
        this.forEach { writer.write("${it.src}\t${it.dst}\n") }
    }

    private fun sortAndTrim(arr: LongArrayList) {
        val set = LongOpenHashSet(arr)
        require(set.size <= arr.size) { "The set size cannot be bigger than array!" }
        arr.clear()
        arr.addAll(set)
        arr.trim()
        Arrays.sort(arr.elements())
    }


    inner class EdgeIterator : Iterator<LongEdge> {
        val vIter = nodes.iterator()
        var currentV = if (vIter.hasNext()) vIter.next() else null
        var neighs = if (currentV != null) neighbors[currentV!!] else null
        var wIter = if (neighs != null) neighs!!.iterator() else null

        override fun hasNext(): Boolean {
            if (neighs == null)
                return false
            while (!wIter!!.hasNext()) {
                if (!vIter.hasNext())
                    return false
                currentV = vIter.next()
                neighs = neighbors[currentV!!]
                wIter = neighs!!.iterator()
                continue
            }
            return true
        }

        override fun next(): LongEdge {
            require(hasNext()) { "There is no element next!" }
            return LongEdge(currentV!!, wIter!!.nextLong())
        }

    }

}
