package aoc.day13

import java.io.File


class Mine {

    val tracks = mutableListOf<Track>()
    val cars = mutableListOf<Car>()

    val intersections = mutableListOf<Intersection>()
    var numTicks = 0

    init {
        fileTrack()
    }

    fun fileTrack() {
        //val lines = File("/Users/sten/dev/adventofcode2018/kotlin-2018/src/main/kotlin/aoc/day13/day13sample.txt").readLines()
        val lines = File("/Users/sten/dev/adventofcode2018/kotlin-2018/src/main/kotlin/aoc/day13/day13.txt").readLines()

        val maxStr = lines.maxBy { it.length }
        val max = if (maxStr != null) maxStr.length else 0
        val array = Array(max) {CharArray(lines.size)}

        for (y in 0 until lines.size) {
            val line = lines[y]
            for (x in 0 until max) {
                array[x][y] = if (x < line.length) line[x] else ' '
            }
        }

        //printArray(array)
        createTracks(array)
    }

    fun createTracks(array:Array<CharArray>) {
        for (y in 0 until array[0].size) {
            for (x in 0 until array.size) {
                if (array[x][y] == '/') {
                    createTrack(x, y, array)
                }
            }
        }
    }

    fun createTrack(x:Int, y:Int, array:Array<CharArray>) {
        //println("create track checking $x")
        if (x == array.size - 1) {
            //println("$x is at the right side of the array, no track will be created")
            return
        }
        if (array[x+1][y] != '-' && array[x+1][y] != '<' &&
                array[x+1][y] != '>' && array[x+1][y] != '+') {
            //println("($x, $y) is not a track top left corner")
            return
        }
        //println("creating track at ($x, $y)")
        var leftX = x
        val carList = mutableListOf<Car>()
        val intersectionList = mutableListOf<Intersection>()
        while (array[leftX][y] != '\\') {
            checkForCars(leftX, y, array, carList)
            checkForIntersections(leftX, y, array, intersectionList)
            leftX++
        }
        //println("right edge at $leftX")
        var bottomY = y
        while (array[leftX][bottomY] != '/') {
            checkForCars(leftX, bottomY, array, carList)
            checkForIntersections(leftX, bottomY, array, intersectionList)
            bottomY++
        }

        val width = leftX - x
        val height = bottomY - y
        //println("bottom right at ($leftX, $bottomY)")
        while (array[leftX][bottomY] != '\\') {
            checkForCars(leftX, bottomY, array, carList)
            checkForIntersections(leftX, bottomY, array, intersectionList)
            leftX--
        }
        //println("bottom left at ($leftX, $bottomY)")
        while (array[leftX][bottomY] != '/') {
            checkForCars(leftX, bottomY, array, carList)
            checkForIntersections(leftX, bottomY, array, intersectionList)
            bottomY--
        }
        if (leftX != x && bottomY != y) {
            println("<< ERROR $leftX != $x and $bottomY != $y")
        }
        //println("Back to ($leftX, $bottomY)")
        //println("width is $width, height is $height")

        val t = Track(x, y, width, height)
        tracks.add(t)

        // add the track to the cars and intersections
        for (c in carList) {
            c.track = t
            cars.add(c)
            //println("adding car $c")
        }
        for (i in intersectionList) {
            val existing = intersections.find { it.x == i.x && it.y == i.y}
            if (existing != null) {
                //println("Adding to existing intersection ($x, $y)")
                existing.second = t
                t.intersections.add(existing)
            } else {
                //println("Adding new intersection (${i.x}, ${i.y})")
                i.first = t
                intersections.add(i)
                t.intersections.add(i)
            }
        }
    }

    fun checkForCars(x:Int, y:Int, array:Array<CharArray>, cars:MutableList<Car>) {
        //println("checkForCars ${array[x][y]}")
        val car = when (array[x][y]) {
            '>' -> Car('E', x, y)
            '<' -> Car('W', x, y)
            'v' -> Car('S', x, y)
            '^' -> Car('N', x, y)
            else -> null
        }
        if (car != null) {
            cars.add(car)
        }
    }

    fun checkForIntersections(x:Int, y:Int, array:Array<CharArray>, inters:MutableList<Intersection>) {
        if (array[x][y] == '+') {
            //println("Adding intersection at ($x, $y)")
            inters.add(Intersection(x, y))
        }
    }

    fun dump() {
        for (c in cars) {
            println(c)
        }
    }


    fun printArray(array:Array<CharArray>) {
        //println("array size = ${array.size}, ${array[0].size}")
        for (y in 0 until array[0].size) {
            for (x in 0 until array.size) {
                print("${array[x][y]}")
            }
            println()

        }
    }

    fun testTrack() {
        val first = Track(0, 0, 6, 4)
        val second = Track(3, 2, 6, 4)
        val intersection = Intersection(6, 2)
        intersection.first = first
        intersection.second = second
        first.intersections.add(intersection)
        second.intersections.add(intersection)

        val i2 = Intersection(3, 4)
        i2.first = first
        i2.second = second
        first.intersections.add(i2)
        second.intersections.add(i2)
        tracks.add(first)
        tracks.add(second)

        val c1 = Car('E', 2, 0)
        c1.track = first
        val c2 = Car('N', 3, 4)
        c2.track = second
        cars.add(c1)
        cars.add(c2)

    }

    fun tick() {
        numTicks++
        //println("tick")
        val collisionList = mutableListOf<Car>()

        val sorted = cars.sortedWith(compareBy( { it.y}, {it.x}))

        for (c in sorted) {
            //println("Update: $c")
            if (collisionList.contains(c)) {
                continue
            }
            c.update()
            val collision = checkCollision(c)
            if (collision != null) {
                collisionList.add(c)
                collisionList.add(collision)
            }
        }
        for (c in collisionList) {
            cars.remove(c)
        }
        if (!collisionList.isEmpty()) {
            //println ("only ${cars.size} cars remain.")
            if (cars.size == 1) {
                println("It is done. The final car at ${cars.first()}")
            }
        }
    }

    fun checkCollision(car:Car):Car? {
        for (c in cars) {
            if (c == car) {
                continue
            }
            if (c.x == car.x && c.y == car.y) {
                println("<< Collision at (${c.x}, ${c.y}) after $numTicks >>")
                return c
            }
        }
        return null
    }

}

data class Car(val initialDirection:Char, val initialX:Int, val initialY:Int) {

    var x = initialX
    var y = initialY
    var track = Track()
    var direction = initialDirection

    var turn = 0

    fun update() {
        move()
        checkIntersections()
    }

    fun move() {
        if (direction == 'E') {
            if (x + 1 <= track.x + track.w) {
                x++
            } else if (y == track.y) {
                //println("top right at ($x, $y) of $track")
                direction = 'S'
                move()

            } else {
                //println("bottom right at ($x, $y) of $track")
                direction = 'N'
                move()
            }
        } else if (direction == 'S') {
            if (y + 1 <= track.y + track.h) {
                y++
            } else if (x == track.x) {
                //println("bottom left at ($x, $y) of $track")
                direction = 'E'
                move()
            } else {
                //println("bottom right at ($x, $y) of $track")
                direction = 'W'
                move()
            }
        } else if (direction == 'W') {
            if (x - 1 >= track.x) {
                x--
            } else if (y == track.y) {
                //println("top left at ($x, $y) of $track")
                direction = 'S'
                move()
            } else {
                //println("bottom left at ($x, $y) of $track")
                direction = 'N'
                move()
            }
        } else if (direction == 'N') {
            if (y - 1 >= track.y) {
                y--
            } else if (x == track.x) {
                //println("top left at ($x, $y) of $track")
                direction = 'E'
                move()
            } else {
                //println("top right at ($x, $y) of $track")
                direction = 'W'
                move()
            }
        }
    }

    fun checkIntersections() {
        for (i in track.intersections) {
            if (x == i.x && y == i.y) {
                //println("intersection!")
                onIntersect(i)
            }
        }
    }

    fun onIntersect(i:Intersection) {
        if (turn == 0) {
            switchTracks(i)
            turnLeft()
        } else if (turn == 2) {
            switchTracks(i)
            turnRight()
        } else {
            //println("Continuing straight")
        }
        nextTurn()
    }

    fun switchTracks(i:Intersection) {
        track = if (i.first == track) i.second else i.first
    }

    fun turnLeft() {
        //println("Turning Left")
        direction = when (direction) {
            'N' -> 'W'
            'W' -> 'S'
            'S' -> 'E'
            else -> 'N'
        }
    }

    fun turnRight() {
        //println("Turning Right")
        direction = when (direction) {
            'N' -> 'E'
            'E' -> 'S'
            'S' -> 'W'
            else -> 'N'
        }
    }

    fun nextTurn() {
        turn++
        if (turn >= turns.size) {
            turn = 0
        }
    }

    companion object {
        val turns = listOf('L', 'S', 'R')
    }

    override fun toString(): String = "[Car $direction ($x, $y)]"

}

class Track(val x:Int, val y:Int, val w:Int, val h:Int) {

    constructor() : this(0, 0, 0, 0)

    val intersections = mutableListOf<Intersection>()

    override fun toString(): String =
                "[Track ($x, $y), (${x + w}, $y), ($x, ${y + h}), (${x + w}, ${y + h})"


}

class Intersection(val x:Int, val y:Int) {

    var first = Track()
    var second =Track()
}

fun main(args:Array<String>) {
    val mine = Mine()

    while (mine.cars.size > 1) {
    //while (mine.numTicks < 1003) {
        mine.tick()
    }
    mine.dump()
    //mine.tick()
    println(mine.numTicks)
    //mine.dump()

    //println(mine.cars.first())
}