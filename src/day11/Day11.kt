package day11


fun main(args:Array<String>) {


    val grid = Grid()

    println(grid.largestPower())
}


class Grid {

    val sn = 7989
    val size = 300
    val grid  = Array(size) {IntArray(size)}

    init {
        for (i in 0 until size) {
            for (j in 0 until size) {
                calculatePower(j, i)
            }
        }

    }

    fun calculatePower(x:Int, y:Int) {
        val rackId = x + 10
        var powerLevel = rackId * y
        powerLevel += sn
        powerLevel *= rackId

        val hundreds = getHundreds(powerLevel)

        grid[x][y] = hundreds - 5
    }

    fun getHundreds(num:Int):Int {
        if (num < 100) return 0
        val str = num.toString()

        return str[str.length - 3].toString().toInt()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (i in 0 until size) {
            for (j in 0 until size) {
                builder.append("${grid[j][i]} ")
            }
            builder.append("\n")
        }
        return builder.toString()
    }

    fun powerAt(x:Int, y:Int) = grid[x][y]

    fun largestPower():Pair<Int, Int> {
        var largestPower = Int.MIN_VALUE
        var pair = Pair(0, 0)
        var curS = 1
        for (s in 1..size) {
        //val s = 3
            // s = 1, until 300
            // s = 300, until 1
            // 0 until size - s-1
            val range = size - s
            for (x in 0..range) {
                for (y in 0..range) {
                    val power = totalPower(x, y, s)
                    if (power > largestPower) {
                        largestPower = power
                        pair = Pair(x, y)
                        curS = s
                    }
                }
            }
        }
        println(largestPower)
        println(curS)

        return pair
    }

    fun totalPower(x:Int, y:Int, s:Int):Int {
        var power = 0
        for (i in x until x+s) {
            for (j in y until y+s) {

                power += grid[i][j]
            }
        }
        if (s == 300) {
            println("s is 300, power is $power")
        }
        return power
    }

}