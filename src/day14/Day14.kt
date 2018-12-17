package day14

val recipes = mutableListOf<Int>()

var first = 0
var second = 1

fun main(args:Array<String>) {
    println("Welcome to day 14")

    recipes.add(3)
    recipes.add(7)

    //prettyPrint()
    val target = "920831"
    val targetLength = target + 10
    //while (recipes.size < targetLength) {
    for (i in 0 until 100000000) {
        generateNextRecipe()
    }

    while (findSequence(target) == 0) {
        generateNextRecipe()
        //prettyPrint()
        println("${recipes.size}")
    }
    //println("${recipes.size}")
    //println("for $target recipes, the last ten are ${findLastTen(target)}")
}

fun findSequence(seq:String):Int {
    for (i in 0 until recipes.size) {
        if (recipes[i] == seq[0].toString().toInt()) {
            if (doesMatch(i, seq)) {
                println("found $seq at $i")
                return i
            }
        }
    }
    return 0
}

var soFar = 1

fun doesMatch(index:Int, seq:String):Boolean {
    if (index + seq.length > recipes.size) return false
    val builder = StringBuilder()
    for (i in 0 until seq.length) {
        if (seq[i].toString().toInt() == recipes[i + index]) {
            builder.append(seq[i])
        } else {
            if (builder.toString().length > soFar) {
                println("matched ${builder.toString()}")
                soFar++
            }
            return false
        }
    }
    println("matched all of ${builder.toString()}")
    return true
}

fun findLastTen(from:Int):String {
    val builder = StringBuilder()
    val to = from + 10
    for (i in from until to) {
        builder.append(recipes[i].toString())
    }
    return builder.toString()
}

fun generateNextRecipe() {
    val firstScore = recipes[first]
    val secondScore = recipes[second]
    val sum = firstScore + secondScore

    if (sum < 10) {
        recipes.add(sum)
    } else {
        val str = sum.toString()
        recipes.add(str[0].toString().toInt())
        recipes.add(str[1].toString().toInt())
    }
    first = findNewIndex(first, firstScore)
    second = findNewIndex(second, secondScore)
}

fun findNewIndex(from:Int, to:Int):Int {
    val newIndex = (from + to + 1) % recipes.size

    //println("$from -> $newIndex")

    return newIndex
}

fun prettyPrint() {
    val builder = StringBuilder()
    recipes.forEachIndexed {i, r ->
        if (i == first + 1) {
            builder.append(")")
        }
        if (i == second + 1) {
            builder.append("]")
        }
        if (i == first) {
            builder.append("(")
        }

        if (i == second) {
            builder.append("[")
        }

        builder.append("$r ")

    }

    println(builder.toString())
}