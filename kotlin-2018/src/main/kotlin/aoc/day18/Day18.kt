package aoc.day18

import java.io.File


class Day18 {


    init {
        val lines = File("/Users/sten/dev/adventofcode2018/kotlin-2018/src/main/kotlin/aoc/day18/day18.txt").readLines()
        var grid = Array(lines[0].length) {CharArray(lines.size)}

        for (y in 0 until lines.size) {
            val line = lines[y]
            for (x in 0 until line.length) {
                grid[x][y] = line[x]
            }
        }
        //printArray(grid)
        //println()
        val total = 1000000000
        for (i in 1..total) {
            if (i % 100000 == 0) {
                val percent = (i / (total * 1.0)) * 100.0
                println("-- Day $i ($percent) --")
            }
            grid = nextDay(grid)
            //printArray(grid)
        }

        val trees = count('|', grid)
        val lumber = count('#', grid)
        val product = trees * lumber

        println("Trees $trees, Lumber $lumber, Resources $product")

    }

    fun nextDay(array:Array<CharArray>):Array<CharArray> {
        val next = Array(array.size) {CharArray(array[0].size)}

        for (y in 0 until array.size) {
            for (x in 0 until array[0].size) {
                if (array[x][y] == '.') {
                    next[x][y] = if (count(x, y, '|', array) >= 3) '|' else '.'
                } else if (array[x][y] == '|') {
                    next[x][y] = if (count(x, y, '#', array) >= 3) '#' else '|'
                } else if (array[x][y] == '#') {
                    next[x][y] = if (count(x, y, '#', array) >= 1 &&
                            count(x, y, '|', array) >= 1) '#' else '.'
                }
            }
        }

        return next
    }

    fun count(which:Char, array:Array<CharArray>):Int {
        var c = 0
        for (y in 0 until array[0].size) {
            for (x in 0 until array.size) {
                if (array[x][y] == which) c++
            }
        }
        return c
    }

    fun count(x:Int, y:Int, which:Char, array:Array<CharArray>):Int {
        var c = 0

        if (y - 1 >= 0) {
            if (x - 1 >= 0) {
                if (array[x - 1][y - 1] == which) c++
            }
            if (array[x][y - 1] == which) c++
            if (x + 1 < array[0].size) {
                if (array[x + 1][y - 1] == which) c++
            }
        }
        if (x - 1 >= 0) {
            if (array[x - 1][y] == which) c++
        }
        if (x + 1 < array[0].size) {
            if (array[x + 1][y] == which) c++
        }
        if (y + 1 < array.size) {
            if (x - 1 >= 0) {
                if (array[x - 1][y + 1] == which) c++
            }
            if (array[x][y + 1] == which) c++
            if (x + 1 < array[0].size) {
                if (array[x + 1][y + 1] == which) c++
            }
        }

        return c
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
}

fun main(args:Array<String>) {
    Day18()
}