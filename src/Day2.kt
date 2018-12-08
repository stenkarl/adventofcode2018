import java.io.File

fun main(args:Array<String>) {
    val list = File("/Users/sten/dev/adventofcode2018/day2.txt").readLines()

    for (s in list) {
        for (s2 in list) {
            val b = oneDiff(s, s2)
            if (b) {
                println(s)
                println(s2)
            }
        }
    }
}

fun partOne(list:List<String>) {
    var twoCount = 0
    var threeCount = 0

    for (s in list) {
        for (ch in s) {
            val c = count(s, ch)
            if (c == 2) {
                twoCount++
                break
            }

        }
    }
    for (s in list) {
        for (ch in s) {
            val c = count(s, ch)
            if (c == 3) {
                threeCount++
                break
            }

        }
    }

    println("Two count $twoCount, $threeCount ${twoCount * threeCount}")
}

fun count(str:String, char:Char):Int {
    var c = 0
    for (ch in str) {
        if (ch == char) {
            c++
        }
    }
    return c
}

fun oneDiff(first:String, second:String):Boolean {
    var c = 0
    for (i in (0 until first.length)) {
        if (first.get(i) != second.get(i)) {
            c++
        }
        if (c > 2) break
    }
    return c == 1
}
