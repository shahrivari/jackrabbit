package io.github.shahrivari.jackrabbit

import org.assertj.core.api.Assertions
import org.junit.Test

class LongGraphTest {
    @Test
    fun basicTest() {
        val edges = listOf(Edge(2, 1),
                           Edge(1, 2),
                           Edge(1, 2),
                           Edge(2, 1))
        val graph = LongGraph(edges)
        Assertions.assertThat(graph.nodes).isEqualTo(arrayOf(1L, 2L))
        graph.nodes.forEach { Assertions.assertThat(graph.neighbors(it).size).isEqualTo(1) }
    }
}