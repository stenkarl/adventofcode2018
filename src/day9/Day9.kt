package day9


val numMarbles = 7162600 //5807 //6111 //1104 //7999 //1618 //25
val numPlayers = 438 //30 //21 //17 //13 //10 //9
val marbles = mutableListOf(0)

val players = mutableMapOf<Int, Long>()

var curPlayer = 1
var curMarble = 0

fun main(args:Array<String>) {

    print()
    for (i in (1..numMarbles)) {
        takeTurn(i)
        //print()
        curPlayer++
        if (curPlayer > numPlayers) curPlayer = 1

        if (i % 250000 == 0) {
            val perc = (i.toDouble() / numMarbles.toDouble()) * 100.0
            println ("$i, $perc%")
        }
    }
    println(players)
    println(players.maxBy { it.value })
}

fun takeTurn(marble:Int) {
    if (marble % 23 == 0) {
        score(marble)
        return
    }
    val index = findNextIndex()
    if (index == -1) {
        marbles.add(marble)
    } else {
        marbles.add(index, marble)
    }
    curMarble = marbles.indexOf(marble)
}

fun score(marble:Int) {
    var curScore = players.getOrPut(curPlayer) {0L}
    curScore += marble
    curScore += removeMarble()

    players.put(curPlayer, curScore)
}

fun removeMarble():Int {
    var index = curMarble - 7
    if (index < 0) index = (marbles.size + index)

    curMarble = if (index == marbles.size -1) 0 else index
    return marbles.removeAt(index)
}

fun findNextIndex():Int {
    if (curMarble == 0) return -1
    if (curMarble == marbles.size - 2) return -1
    if (curMarble == 1 && marbles.size == 2) return 1
    if (curMarble == marbles.size - 1) return 1

    return curMarble + 2
}

fun print() {
    println("[$curPlayer] $marbles")
    println("curMarble: $curMarble")
}
