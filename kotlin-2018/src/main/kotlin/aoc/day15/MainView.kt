package aoc.day15

import javafx.application.Platform
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.*
import java.io.File
import kotlin.concurrent.fixedRateTimer

class MainView : View("Day 15") {

    var width = 0
    var height = 0
    var group = group {}
    val goblins = mutableListOf<Rectangle>()
    val elves = mutableListOf<Rectangle>()
    val scale = 25.0

    val world = initWorld()

    override val root = borderpane {
        center {
            scrollpane {
                prefWidth = 800.0
                prefHeight = 800.0
                group = group {
                    rectangle {
                        width = 800.0
                        height = 800.0
                    }
                }
            }
        }
        bottom {
            flowpane {
                button("Next") {
                    setOnAction {
                        click()
                        refresh()
                    }
                }
                button("Auto") {
                    setOnAction {
                        auto()
                    }
                }
            }
        }
    }

    init {
        val walls = world.elements.filter { it is Wall}
        for (w in walls) {
            val r = Rectangle(w.x * scale, w.y * scale, scale, scale)
            r.fill = Color.DARKGRAY

            group.children.add(r)
        }
        refresh()
    }

    fun click() {
        world.tick()
        refresh()
    }


    fun refresh() {
        val goblinList = world.elements.filter { it is Goblin}

        for (g in goblins) {
            if (group.children.contains(g)) {
                group.children.remove(g)
            }
        }
        for (g in goblinList) {
            val r = Rectangle(g.x * scale, g.y * scale, scale, scale)
            r.fill = Color.DARKOLIVEGREEN

            group.children.add(r)
        }

        val elfList = world.elements.filter { it is Elf}
        for (e in elves) {
            if (group.children.contains(e)) {
                group.children.remove(e)
            }
        }
        for (e in elfList) {
            val r = Rectangle(e.x * scale, e.y * scale, scale, scale)
            r.fill = Color.DARKBLUE

            group.children.add(r)
        }
    }

    fun auto() {
        fixedRateTimer("autorun", false, 100, 10) {
            click()

            //if (mine.numTicks == 1003) {
                Platform.runLater {
                    refresh()
                }
                this.cancel()
                println("Stopping")

            //}
        }
    }

    fun initWorld():World {
        val list = File("/Users/sten/dev/adventofcode2018/kotlin-2018/src/main/kotlin/aoc/day15/day15.txt").readLines()

        width = list[0].length
        height = list.size
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
        return World(elements, width, height)
    }

}