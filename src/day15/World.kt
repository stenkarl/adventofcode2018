package day15


class World(val elements:List<Element>, val width:Int, val height:Int) {

    private val entities:List<Entity> = elements.filter {it is Entity }.map {it as Entity }

    fun tick() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val cur = entities.find { it.x == x && it.y == y }
                if (cur != null) {
                    cur.move(entities)
                    println(this)
                }
            }
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (y in 0 until height) {
            for (x in 0 until width) {
                val cur = elements.find { it.x == x && it.y == y}
                if (cur != null) {
                    builder.append(cur.toString())
                }
            }
            builder.append("\n")
        }
        return builder.toString()
    }

}

abstract class Element(val initialX:Int, val initialY:Int) {

    var x:Int = initialX
    var y:Int = initialY
}

class Wall(initialX:Int,initialY:Int): Element(initialX, initialY) {

    override fun toString():String = "#"
}

class Open(initialX:Int,initialY:Int): Element(initialX, initialY) {

    var decoration = "."

    override fun toString():String = decoration
}

abstract class Entity(initialX:Int, initialY:Int, val elements:List<Element>): Element(initialX, initialY) {

    var hp = 200
    var attackPower = 3

    fun move(entities:List<Entity>):Boolean {
        val target = findTargetSpace(getTargets(entities))
        if (target == null) {
            return false
        }
        decorateOpen(setOf(target), "+")
        moveTowards(target)
        return true
    }

    private fun moveTowards(target:Point) {
        // implement findShortestDistance
        // #4E212#
        // #32101#
        // #432G2#
    }

    private fun moveTo(toX:Int, toY:Int) {
        x = toX
        y = toY
    }

    private fun findTargetSpace(targets:List<Entity>):Point? {
        if (targets.isEmpty()) {
            return null
        }
        println("${this.info()} targets are $targets")
        val ranged = inRange(targets)
        if (ranged.isEmpty()) {
            return null
        }
        decorateOpen(ranged, "?")
        val reachables = reachable(ranged)
        if (reachables.isEmpty()) {
            println("No points are reachable for ${this.info()}")
            return null
        }
        decorateOpen(reachables, "@")
        val nearests = nearest(reachables)
        if (nearests.isEmpty()) {
            return null
        }
        decorateOpen(nearests, "!")
        return chosen(nearests)
    }

    private fun inRange(targets:List<Entity>):Set<Point> {
        val ranged = mutableSetOf<Point>()
        targets.forEach {
            ranged.addAll(getNeighboringFreePoints(Point(it.x, it.y)))
        }
        return ranged
    }

    private fun reachable(points:Set<Point>):Set<Point> {
        return points.filter { isReachable(it) }.toSet()
    }

    private fun isReachable(point:Point):Boolean {
        val queue = mutableListOf<Point>()
        val seen = mutableSetOf<Point>()
        queue.add(point)

        while (!queue.isEmpty()) {
            val cur = queue.removeAt(0)
            //println("isR (${this.info()} $cur")
            if (isAdjacent(cur)) return true
            seen.add(cur)
            val neighbors = getNeighboringFreePoints(cur)
            neighbors.forEach {
                if (!seen.contains(it)) {
                    queue.add(it)
                    //println("  adding $it for queue size of ${queue.size}")
                }
            }
        }
        //println("  $point is not reachable")
        return false
    }

    private fun isAdjacent(p:Point):Boolean {
        return p.x + 1 == x && p.y == y ||
                p.x - 1 == x && p.y == y ||
                p.x == x && p.y - 1 == y ||
                p.x == x && p.y + 1 == y
    }

    private fun nearest(points:Set<Point>):Set<Point> {
        var curMinDist = Int.MAX_VALUE
        points.forEach {
            val curDist = findDistance(it)
            if (curDist < curMinDist) {
                curMinDist = curDist
            }
        }
        return points.filter { findDistance(it) == curMinDist}.toSet()
    }

    private fun findDistance(point:Point):Int {
        return Math.abs(point.x - x) + Math.abs(point.y - y)
    }

    private fun chosen(points:Set<Point>):Point {
        val topLeft = points.find { it.x < x && it.y < y}
        if (topLeft != null) return topLeft
        val top = points.find { it.x == x && it.y < y}
        if (top != null) return top
        val topRight = points.find { it.x > x && it.y < y}
        if (topRight != null) return topRight
        val right = points.find { it.x > x && it.y == y}
        if (right != null) return right
        val bottomRight = points.find { it.x > x && it.y > y}
        if (bottomRight != null) return bottomRight
        val bottom = points.find { it.x == x && it.y > y}
        if (bottom != null) return bottom
        val bottomLeft = points.find { it.x < x && it.y > y}
        if (bottomLeft != null) return bottomLeft
        val left = points.find { it.x < x && it.y == y}
        if (left != null) return left
        println("Unknown chosen for $this $points")
        return points.first()
    }

    private fun getNeighboringFreePoints(point:Point):Set<Point> {
        val neighbors = mutableSetOf<Point>()

        addIfFree(Point(point.x, point.y - 1), neighbors)
        addIfFree(Point(point.x - 1, point.y), neighbors)
        addIfFree(Point(point.x + 1, point.y), neighbors)
        addIfFree(Point(point.x, point.y + 1), neighbors)

        return neighbors
    }

    private fun addIfFree(point:Point, set:MutableSet<Point>) {
        if (isFree(point)) {
            set.add(point)
        }
    }

    private fun isFree(point:Point):Boolean {
        val element = elements.find { it.x == point.x && it.y == point.y }

        return element is Open
    }

    private fun decorateOpen(points:Set<Point>, str:String) {
        elements.forEach {
            if (it is Open) {
                it.decoration = "."
            }
        }
        points.forEach { p ->
            val open = elements.find { it.x == p.x && it.y == p.y}
            if (open is Open) {
                open.decoration = str
            }
        }
    }

    fun attack() {}

    abstract fun getTargets(entities: List<Entity>):List<Entity>

    fun info() = "$this [$x, $y]"

}

class Elf(initialX:Int, initialY:Int, elements:List<Element>) : Entity(initialX, initialY, elements) {

    override fun toString():String = "E"

    override fun getTargets(entities: List<Entity>):List<Entity> =
            entities.filter { it is Goblin }

}

class Goblin(initialX:Int, initialY:Int, elements: List<Element>) : Entity(initialX, initialY, elements) {

    override fun toString():String = "G"

    override fun getTargets(entities: List<Entity>):List<Entity> =
        entities.filter { it is Elf }
}

data class Point(val x:Int, val y:Int)

