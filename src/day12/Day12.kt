package day12

import java.io.File

//val initialStr = "#..#.#..##......###...###"
val initialStr = "##..##....#.#.####........##.#.#####.##..#.#..#.#...##.#####.###.##...#....##....#..###.#...#.#.#.#"

val plantRules = mutableListOf<String>()

val pots = Pots(initialStr)

fun main(args:Array<String>) {
    val list = File("/Users/sten/dev/adventofcode2018/src/day12/day12.txt").readLines()
    //val list = File("/Users/sten/dev/adventofcode2018/src/day12/day12sample.txt").readLines()

    list.forEach {
        val split = it.split(" => ")
        if (split[1] == "#") {
            plantRules.add(split[0])
        }
    }

    println(plantRules)

    println("                 1         2         3     ")
    println("       0         0         0         0     ")
    println(" 0: $pots")
    val startTime = System.currentTimeMillis()
    var prev = ""
    for (i in 1L..300L) {
    //for (i in 1L..50000000000L) {
        pots.nextGeneration()
        //val padding = if (i < 10) " " else ""
        //if (i % 1000000000 == 0L) {
        //if (i % 1000 == 0L) {
            val str = pots.toString()
            val curTime = (System.currentTimeMillis() - startTime) / 1000
            println("$i: (${str.length}) (${pots.findFirstPlant()}) $str")
            if (prev == str) {
                println("<< string not changing >>")
                //break
            }
            prev = str
            //println("curTime: $curTime")
        //}
    }
    val base = 50000000000L - 98L
    //val base = 300L - 98L
    println("Sum ${pots.sum()}")
    println("Sum Pattern ${pots.sumPattern(base)}")
}

class Pots(initialState:String) {

    val map = mutableMapOf<Long, Char>()

    init {
        for (i in 0L until initialState.length) {
            map[i] = initialState[i.toInt()]
        }
    }

    fun get(index:Long):Char =
        map.getOrDefault(index, '.')


    override fun toString(): String {
        val builder = StringBuilder()
        val first = findFirst()
        val last = findLast()
        for (i in first..last) {
            builder.append(get(i))
        }
        return builder.toString()
    }

    fun nextGeneration() {
        pad()
        val first = findFirst()
        val last = findLast()
        val start = first + 2
        val end = last - 2
        //println("$first, $last, $start, $end")
        val builder = StringBuilder()
        for (i in start until end) {
            val from = i - 2
            val to = i + 3
            val region = getRegion(from, to)
            //println("($from, $to) $region")
            if (plantRules.contains(region)) {
                builder.append("#")
                //println("Found: $region ($from, $to)")
            } else {
                builder.append(".")
            }
        }
        applyChanges(builder.toString(), start)
    }

    fun applyChanges(str:String, start:Long) {
        for (i in 0 until str.length) {
            map[start + i] = str[i]
        }
    }

    fun getRegion(from:Long, to:Long):String {
        val builder = StringBuilder()

        for (i in from until to) {
            builder.append("${map[i]}")
        }

        return builder.toString()
    }

    fun pad() {
        val firstPlant = findFirstPlant()
        val start = firstPlant - 4
        //println("padding from $start")
        for (i in start until firstPlant) {
            map[i] = '.'
        }
        val first = findFirst()
        if (first < start) {
            //println("removing $first until $start")
            for (i in first until start) {
                map.remove(i)
            }
        }
        val last = findLastPlant()
        val end = last + 5
        for (i in (last + 1)..end) {
            //println("setting $i to .")
            map[i] = '.'
        }
    }

    fun findFirst():Long {
        val sorted = map.toSortedMap()
        return sorted.firstKey()
    }

    fun findLast():Long {
        val sorted = map.toSortedMap()
        return sorted.lastKey()
    }

    fun findFirstPlant():Long {
        val sorted = map.toSortedMap()
        val start = sorted.firstKey()
        for (i in start until sorted.lastKey()) {
            //println("Found first plant at $i")
            if (sorted[i] == '#') return i
        }
        println("<<<<Shouldn't be here findFirstPlant>>>")
        return sorted.firstKey()
    }

    fun findLastPlant():Long {
        val sorted = map.toSortedMap()
        val start = sorted.lastKey()
        for (i in start downTo sorted.firstKey()) {
            if (sorted[i] == '#') return i
        }
        println("<<<<Shouldn't be here findLastPlant>>>")
        return sorted.lastKey()
    }

    fun sum():Long {
        val first = findFirst()
        val last = findLast()
        var sum = 0L
        for (i in first..last) {
            if (map[i] == '#') {
                sum += i
                print("$i ")
            }
        }
        println()
        return sum
    }

    fun sumPattern(base:Long):Long {
        val first = findFirst()
        val last = findLast()
        var sum = 0L
        for (i in first..last) {
            if (map[i] == '#') {
                sum = sum + (i - first + base)
                print("$i ")
            }
        }
        println()
        return sum
    }
}