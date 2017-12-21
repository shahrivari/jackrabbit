package io.github.shahrivari.jackrabbit

import com.google.common.base.CharMatcher

val WHITE_REGEX = Regex("\\s+")

data class LongEdge(val src: Long, val dst: Long) {
    override fun toString() = "($src,$dst)"
    val nodes get() = listOf(src, dst)

    companion object {
        fun parse(s: String): LongEdge {
            require(s.contains("\t")) { "String is not well formatted: $s" }
            val tokens = s.split("\t")
            return LongEdge(tokens[0].toLong(), tokens[1].toLong())
        }
    }
}

data class IntEdge(val src: Int, val dst: Int) {
    override fun toString() = "($src,$dst)"
    val nodes get() = listOf(src, dst)

    companion object {
        fun parse(s: String): IntEdge {
            requireNotNull(s) { "String is null!" }
            var tokens = listOf<String>()
            if (CharMatcher.anyOf(s).matches(','))
                tokens = s.split(",")
            else if (CharMatcher.whitespace().matchesAnyOf(s))
                tokens = s.split(WHITE_REGEX)
            require(tokens.size == 2) { "String is not well formatted: $s" }
            return IntEdge(tokens[0].toInt(), tokens[1].toInt())
        }
    }
}