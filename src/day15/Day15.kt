package day15


import java.io.File

fun main(args:Array<String>) {
    println("hello. welcome to day 15 of AoC, Jakobi")
    val list = File("/Users/sten/dev/adventofcode2018/src/day15/day15.txt").readLines()

    val width = list[0].length
    val height = list.size
    val elements = mutableListOf<Element>()
    for (y in 0 until height) {
        val line = list[y]
        for (x in 0 until width) {
            val ch = line[x]
            val element = when (ch) {
                '#' -> Wall(x, y)
                'E' -> Elf(x, y, elements)
                'G' -> Goblin(x, y, elements)
                else -> Open(x, y)
            }
            elements.add(element)
        }
    }
    val world = World(elements, width, height)

    println(world)

    world.tick()

    println(world)
}