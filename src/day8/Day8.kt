package day8

import java.io.File

fun main(args:Array<String>) {
    println("hello. welcome to day 8 of AoC, Jakobi")
    val line = File("/Users/sten/dev/adventofcode2018/src/day8/day8.txt").readLines()
    val list = line[0].split(" ").map { it.toInt() }

    println(list)

    val root = createNode(list, Counter())

    println (root)

    println (root.getValue())
}

private fun createNode(list:List<Int>, counter:Counter):Node {
    val numChildren = list[counter.index]
    counter.inc()
    val numMetadata = list[counter.index]
    counter.inc()
    val node = Node(numChildren, numMetadata)
    if (numChildren > 0) {
        for (i in (0 until numChildren)) {
            node.children.add(createNode(list, counter))
        }
    }
    for (i in (0 until numMetadata)) {
        node.metadata.add(list[counter.index])
        counter.inc()
    }

    return node
}

private data class Node(val numChildren:Int, val numMetadata:Int) {

    val metadata = mutableListOf<Int>()
    val children = mutableListOf<Node>()

    fun sumMetadata():Int {
        var sum = metadata.sum()
        children.forEach { sum += it.sumMetadata()}

        return sum
    }

    fun getValue():Int {
        var value = 0
        if (numChildren == 0) {
            return sumMetadata()
        }
        for (i in metadata) {
            if (i <= children.size) {
                value += children[i - 1].getValue()
            }
        }
        return value
    }

    override fun toString(): String = "[Node (c:$children) (m:$metadata)]"
}

private class Counter {

    var index = 0

    fun inc(amount:Int = 1) {
        index += amount
    }

}