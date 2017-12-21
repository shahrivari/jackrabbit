package io.github.shahrivari.jackrabbit

import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import java.util.*

class LongAdjacency(val node: Long) {
    val ins = LongArrayList()
    val outs = LongArrayList()
    val neighs = LongArrayList()

    fun addEdge(e: LongEdge) {
        require(node == e.src || node == e.dst) { "$e does not relate to $node" }
        if (node == e.src) {
            outs.add(e.dst)
            neighs.add(e.dst)
        }
        if (node == e.dst) {
            ins.add(e.src)
            neighs.add(e.src)
        }
    }

    fun sortAndTrim() {
        val set = LongOpenHashSet(neighs.size)
        for (list in listOf(ins, outs, neighs)) {
            set.clear()
            set.addAll(list)
            require(set.size <= list.size) { "The set size cannot be bigger than array!" }
            if (set.size < list.size) {
                list.clear()
                list.addAll(set)
            }
            list.trim()
            val array = list.elements()
            Arrays.sort(array)

        }
    }
}

class IntAdjacency(val node: Int) {
    val ins = IntArrayList()
    val outs = IntArrayList()
    val neighs = IntArrayList()

    fun addEdge(e: IntEdge) {
        require(node == e.src || node == e.dst) { "$e does not relate to $node" }
        if (node == e.src) {
            outs.add(e.dst)
            neighs.add(e.dst)
        }
        if (node == e.dst) {
            ins.add(e.src)
            neighs.add(e.src)
        }
    }

    fun sortAndTrim() {
        val set = IntOpenHashSet(neighs.size)
        for (list in listOf(ins, outs, neighs)) {
            set.clear()
            set.addAll(list)
            require(set.size <= list.size) { "The set size cannot be bigger than array!" }
            if (set.size < list.size) {
                list.clear()
                list.addAll(set)
            }
            list.trim()
            val array = list.elements()
            Arrays.sort(array)
        }
    }
}