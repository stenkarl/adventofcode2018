import java.io.File

fun main(args:Array<String>) {
    println("hello. welcome to day 6 of AoC, Jakobi")
    val list = File("/Users/sten/dev/adventofcode2018/day6.txt").readLines()

    val points = list.map {
        val split = it.split(", ")

        Point(split[0].toInt(), split[1].toInt())
    }

    val maxX = points.maxBy { it.x }
    val maxY = points.maxBy { it.x }
    println("max x $maxX")
    println("max y $maxY")

    println(points)
    //findMaxDistance(points, maxX!!.x, maxY!!.y)
    partTwo(points, maxX!!.x, maxY!!.y)
}

private fun findMaxDistance(points:List<Point>, maxX:Int, maxY:Int) {
    val excluded = mutableSetOf<Point>()
    val map = mutableMapOf<Point, Int>()
    for (i in (0 until maxX)) {
        for (j in (0 until maxY)) {
            val closest = findClosest(Point(i, j), points)
            if (closest == null) {
                continue
            } else if (i == 0 || i == maxX - 1 || j == 0 || j == maxY - 1) {
                excluded += closest
            } else if (!excluded.contains(closest)) {
                val count = map.getOrPut(closest) { 0 }
                map.put(closest, count + 1)
            }

        }
    }

    println(map)

    val max = map.maxBy { it.value }

    println(max)
}

private fun findClosest(p:Point, points:List<Point>):Point? {
    var cur = points[0]
    var curDist = findDist(p, cur)
    points.forEach {
        val dist = findDist(p, it)

        if (dist < curDist) {
            cur = it
            curDist = dist
        }
    }
    var foundEqual = false
    points.forEach {
        val dist = findDist(p, it)
        if (dist == curDist && cur != it) {
            foundEqual = true
            //println("Filtering out $it for $p")
        }
    }
    return if (foundEqual) null else cur
    //return cur
}

private fun findDist(p1:Point, p2:Point):Int {
    val xDist = Math.abs(p1.x - p2.x)
    val yDist = Math.abs(p1.y - p2.y)

    return xDist + yDist
}

private fun partTwo(points:List<Point>, maxX:Int, maxY:Int) {
    val map = mutableMapOf<Point, Int>()
    val minX = points.minBy { it.x }
    val minY = points.minBy { it.y }
    var count = 0
    println("$minX $minY")
    //for (i in (minX!!.x until maxX)) {
        //for (j in (minY!!.y until maxY)) {

    for (i in (0 until 400)) {
        for (j in (0 until 400)) {
            count++
            val p = Point(i, j)
            val totalDist = findTotalDistance(p, points)
            if (totalDist < 10000) {
                map.put(p, totalDist)
            }
        }
    }
    //println(map)
    println(count)
    println("map size ${map.size}")
}

private fun findTotalDistance(p:Point, points:List<Point>):Int {
    var total = 0
    points.forEach {
        total += findDist(p, it)
    }

    return total
}

data class Point(val x:Int, val y:Int)