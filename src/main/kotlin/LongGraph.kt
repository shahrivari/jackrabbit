import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Paths

private val logger = KotlinLogging.logger {}


class LongGraph(edges: Iterator<Edge>) {
    val nodes: LongArray get
    private val adjList = Long2ObjectOpenHashMap<Adjacency>()
    private val reporter = ProgressReporter(logger, 1_000_000)

    init {
        logger.info { "Reading edges..." }
        for (e in edges) {
            for (v in e.nodes)
                adjList.getOrPut(v) { Adjacency(v) }.addEdge(e)
            reporter.progress()
        }
        logger.info { "Sorting nodes..." }
        nodes = adjList.keys.toLongArray()
        nodes.sort()
        logger.info { "Optimizing adjacencies..." }
        adjList.values.forEach { it.sortAndTrim() }
        logger.info { "Graph is built: #nodes:${nodes.size} , #edges:${edgeCount()}" }
    }


    fun edgeCount() = adjList.values.map { it.ins.size }.sum()


    fun hasEdge(src: Long, dest: Long): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun neighbors(v: Long): Array<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}


fun main(args: Array<String>) {
    var iter = Files.lines(Paths.get("/home/saeed/live.txt"))
        .filter { !it.startsWith("#") }
        .map { it -> Edge.parse(it) }.iterator()

    var graph = LongGraph(iter)
}