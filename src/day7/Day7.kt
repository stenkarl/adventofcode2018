package day7

import java.io.File

fun main(args:Array<String>) {
    println("hello. welcome to day 7 of AoC, Jakobi")
    val list = File("/Users/sten/dev/adventofcode2018/src/day7/day7.txt").readLines()

    //println(list)

    val nodes = createNodes(list)
    val starting = findStarting(nodes)
    println(starting)

    //val root = starting[0]
    val root = Node('*')
    root.children.addAll(starting)
    starting.forEach {
        it.parents.add(root)
    }
    root.isVisited = true

    while (!root.complete()) {
        tick(root)
    }
    /*

    while (!findPath(root)) {

        //println("notReady size ${notReady.size}")
    }
    */
    //root.complete()
}

var time = -1
var workers = listOf(Worker(1), Worker(2), Worker(3), Worker(4), Worker(5))

private fun tick(root:Node) {
    println("Begin global tick ${time + 1}")
    for (w in workers) {
        if (w.isAvailable()) {
            val next = root.available()
            if (!next.isEmpty()) {
                w.takeWork(next[0])
            } else {
                println("Worker ${w.id} tried to take work, but nothing available")
            }
        } else {
            println("Worker ${w.id} is working on ${w.node!!.value}")
        }
    }
    workers.forEach { it.tick() }
    time++
    println("End global tick $time")
    println()
}

private fun getNext(): Node? {
    return null
}

private fun findStarting(nodes:MutableSet<Node>):List<Node> {
    val avail = mutableSetOf<Node>()
    for (n in nodes) {
        if (n.parents.isEmpty()) {
            avail.add(n)
        }
    }
    return avail.sortedBy { it.value }
}

private fun findPath(parent: Node):Boolean {
    //println(" day7.findPath $parent")
    if (!parent.isVisited) {
        print(parent.value)
        parent.isVisited = true
    } else {
        val avail = parent.available()
        if (avail.isEmpty()) {
            println("avail is empty for $parent")
            return true
        } else {
            print(avail[0].value)
            avail[0].isVisited = true
        }
    }
    return false
}

private fun createNodes(list:List<String>):MutableSet<Node> {
    val set = mutableSetOf<Node>()
    list.forEach {
        val parent = Node(it[5])
        val child = Node(it[36])

        if (!set.contains(parent)) {
            set += parent
            //println("Adding parent $parent")
        }
        if (!set.contains(child)) {
            set += child
            //println ("Adding child $child")
        }
        val p = set.find { it.equals(parent) }
        val c = set.find { it.equals(child) }
        if (p != null && c != null) {
            p.children += c
            c.parents += p
            //println("Setting ${p.value} as parent of ${c.value}")
        }

        //println("$p $c")
    }
    //println (set)
    return set
}

data class Node(val value:Char) {

    val children = mutableSetOf<Node>()
    val parents = mutableSetOf<Node>()

    var isVisited = false
    var inProgress = false

    fun complete():Boolean {
        if (!isVisited) {
            println("At least Node $value has not been visited. Work will continue.")
            return false
        }
        for (c in children) {
            if (!c.complete()) {
                return false
            }
        }
        return true
    }

    fun canVisit():Boolean {
        if (parents.isEmpty()) return true
        val parentNotVisited = parents.find { !it.isVisited }
        //if (parentNotVisited != null) {
            //println("${this.value} can't be visited because of parent ${parentNotVisited.value}")
        //    if (this.value == 'U') {
        //        println("**** U can't be visited")
        //    }
        //}

        return parentNotVisited == null
    }

    fun available():List<Node> {
        val avail = mutableListOf<Node>()
        if (inProgress && !isVisited) return avail
        if (canVisit() && !inProgress && !isVisited) {
            avail.add(this)
            return avail
        }
        for (c in children) {
            avail.addAll(c.available())
        }
        return avail.sortedBy { it.value }
    }

    override fun toString(): String =
            "[Node $value, (ch: $children)]"
}

data class Worker(val id:Int) {

    var timer = 0
    var target = 0
    var available = true

    val base = 60
    var node: Node? = null

    fun isAvailable():Boolean = available

    fun takeWork(node: Node) {
        this.node = node
        node.inProgress = true
        available = false
        timer = 0
        target = base + (node.value.toInt() - 64)
        println("Worker $id takes ${node.value}. This will take $target ticks")

    }

    fun tick() {
        if (available) return
        timer++
        println("* Worker $id ${node!!.value} ticks $timer of $target")
        if (timer == target) {
            available = true
            node!!.inProgress = false
            node!!.isVisited = true

            println("Worker $id finished ${node!!.value} in $timer ticks")

            return
        }

    }
}