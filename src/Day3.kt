import java.io.File

fun main(args:Array<String>) {
    val list = File("/Users/sten/dev/adventofcode2018/day3.txt").readLines()

    var rects = listOf<Rect>()
    for (s in list) {
        val first = s.split(" @ ")
        val second = first[1].split(": ")
        val third = second[0].split(",")
        val x = third[0].toInt()
        val y = third[1].toInt()
        val fourth = second[1].split("x")
        val w = fourth[0].toInt()
        val h = fourth[1].toInt()
        //println("$s -> $x, $y, $w, $h")
        rects += Rect(first[0].substring(1).toInt(), x, y, w, h)
    }

    //rects.forEach { println (it)}

    //elfTornado(rects)

    //test()

    for (r in rects) {
        if (!anyOverlap(r, rects)) {
            println("No overlap: $r")
        }
    }

}

fun test() {
    val r1 = Rect(1, 0, 0, 2, 1)
    val r2 = Rect(2, 0, 1, 1, 1)

    println("$r1 $r2 ${r1.overlap(r2)}")
}

fun anyOverlap(r:Rect, rect:List<Rect>):Boolean {
    for (c in rect) {
        if (r.overlap(c)) return true
    }
    return false
}

fun elfTornado(rect:List<Rect>) {
    val width = 1000
    val height = 1000
    var count = 0
    var set = setOf<Rect>()
    for (x in 0 until width) {
        for (y in 0 until height) {
            var overlapSet = setOf<Rect>()
            for (r in rect) {
                if (r.intersects(x, y)) {
                    if (!overlapSet.contains(r)) {
                        overlapSet += r
                    }
                }
            }
            if (overlapSet.size > 1) {
                //println(overlapSet)
                count++
                //return
            }
        }
    }
    println (count)
}


data class Rect(val id:Int, val x:Int, val y:Int, val w:Int, val h:Int) {

    val botRightX = x + (w - 1)
    val botRightY = y + (h - 1)

    fun intersects(px:Int, py:Int):Boolean =
        (px >= x && px < (w + x) && py >= y && py < (h + y))

    fun overlap(other:Rect):Boolean {
        if (this == other) return false
        if (x > other.botRightX || other.x > botRightX) return false
        if (y > other.botRightY || other.y > botRightY) return false

        return true
    }

    override fun toString(): String =
            "[Rect $id, ($x, $y) ($botRightX, $botRightY) $w, $h]"


}