import java.io.File

fun main(args:Array<String>) {
    println("hello. welcome to day 5 of AoC, Jakobi")
    val list = File("/Users/sten/dev/adventofcode2018/day5.txt").readLines()
    println(list[0])

    val reduced = fullReduce(list[0])
    println(reduced)
    println("which is ${reduced.length}")

    val range = CharRange('a', 'z')
    var curShortest = reduced.length
    for (ch in range) {
        val upCase = ch.toUpperCase()
        val noLower = reduced.replace(ch + "", "")
        val noUpper = noLower.replace(upCase + "", "")

        val newReduced = fullReduce(noUpper)
        if (newReduced.length < curShortest) {
            println("after removing all $ch and $upCase, the new reduced string is " +
                    "${newReduced.length} which is shorter than $curShortest")
            curShortest = newReduced.length

        }
    }
}

fun fullReduce(str:String):String {
    var lastStr = str
    var newStr = reduce(str)
    while (lastStr != newStr) {
        lastStr = newStr
        newStr = reduce(newStr)
    }
    return newStr
}

fun reduce(str:String):String {
    var index = -1
    for (i in (0 until str.length-1)) {
        val first = str[i]
        val second = str[i+1]
        // if first and second are equal, but different case, tell me that
        if (first != second && first.equals(second, ignoreCase = true)) {
            //println("Good news! $first and $second are equal!")
            index = i
            break
        }

        //println("the character at $i is ${first}. it's right neighbor is ${second}")
    }
    if (index == -1) {
        return str
    }
    val reduced = str.removeRange(index, index + 2)
    //println("The index we will remove is $index to ${index+1}. the new reduced string is $reduced")

    return reduced
}