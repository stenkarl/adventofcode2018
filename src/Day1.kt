import java.io.File

fun main(args:Array<String>) {
    var sum = 0
    var set = mutableSetOf<Int>()
    var seen = false
    while (!seen) {
        File("/Users/sten/dev/adventofcode2018/day1.txt").forEachLine {
            val num = it.toInt()

            sum += num
            //println(sum)
            if (set.contains(sum)) {
                println("seen " + sum)
                seen = true
            }
            set.add(sum)
        }
    }

    //println(sum)
}
