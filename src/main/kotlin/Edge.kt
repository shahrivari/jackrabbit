data class Edge(val src: Long, val dst: Long) {
    override fun toString() = "($src,$dst)"
    val nodes get() = listOf(src, dst)

    companion object {
        fun parse(s: String): Edge {
            require(s.contains("\t")) { "String is not well formatted: $s" }
            val tokens = s.split("\t")
            return Edge(tokens[0].toLong(), tokens[1].toLong())
        }
    }
}