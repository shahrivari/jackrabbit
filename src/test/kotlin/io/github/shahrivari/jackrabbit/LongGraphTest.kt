package io.github.shahrivari.jackrabbit

import org.assertj.core.api.Assertions
import org.junit.Test


class LongGraphTest {
    @Test
    fun basicTest() {
        val edges = listOf(LongEdge(2, 1), LongEdge(1, 2), LongEdge(1, 2), LongEdge(2, 1))
        val graph = LongGraph(edges)
        Assertions.assertThat(graph.nodes).isEqualTo(arrayOf(1L, 2L))
        graph.nodes.forEach { Assertions.assertThat(graph.neighbors(it).size).isEqualTo(1) }
    }
}