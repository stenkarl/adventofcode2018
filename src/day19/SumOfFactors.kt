package day19


val num = 10551287

fun main(args:Array<String>) {

    var result = num + 1

    for (i in 2 until num) {
        if (num % i == 0) {
            println("Found $i")
            result += i
        }
    }
    println(result)
}

fun sum() {
    var result = num + 1
    for (i in (2 until Math.sqrt(num.toDouble()).toInt())) {
        if (num % i == 0) {
            if (i == (num / i)) {
                println("found $i")
                result += i
            } else {
                println("found $i")
                result += (i + num/i)
            }
        }
    }
}