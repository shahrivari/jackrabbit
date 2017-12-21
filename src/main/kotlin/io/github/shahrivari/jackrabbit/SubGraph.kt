package io.github.shahrivari.jackrabbit

import java.util.*

class SubGraph(val nodes: IntArray) {
    val adjacency = BitSet(nodes.size * nodes.size)
}