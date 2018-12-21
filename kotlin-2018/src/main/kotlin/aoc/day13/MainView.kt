package aoc.day13

import aoc.Styles
import javafx.application.Platform
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import tornadofx.*
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MainView : View("Hello TornadoFX") {

    var group = group {}
    val circles = mutableListOf<Circle>()

    override val root = borderpane {
        //addClass(Styles.welcomeScreen)
        center {
            //stackpane {
            scrollpane {
                prefWidth = 1000.0
                group = group {
                    //rectangle {
                    //    width = 1000.0
                    //    height = 800.0
                    //}
                }
            }
        }
        bottom {
            stackpane {
                addClass(Styles.content)
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

    val mine = Mine()
    val rects = mutableListOf<Rectangle>()
    val scale = 10.0

    init {
        val tracks = mine.tracks

        for (t in tracks) {
            val r = Rectangle(t.x * scale, t.y * scale, t.w * scale,
                            t.h * scale)
            r.fill = Color.TRANSPARENT
            r.stroke = Color.BLACK

            group.children.add(r)
        }
        val intersections = mine.intersections
        for (i in intersections) {
            val circle = Circle(i.x * scale, i.y * scale, scale / 4,
                    Color.CRIMSON)

            group.children.add(circle)
        }
        refresh()
    }

    fun click() {
        mine.tick()
        //refresh()
    }


    fun refresh() {

        for (c in circles) {
            if (group.getChildren().contains(c)) {
                group.getChildren().remove(c)
            }
        }

        val cars = mine.cars
        for (c in cars) {

            val circle = Circle(c.x * scale, c.y * scale, scale / 2,
                    Color.CORNFLOWERBLUE)

            circles.add(circle)

            group.children.add(circle)
        }

    }

    fun auto() {
        fixedRateTimer("autorun", false, 100, 10) {
            click()

            if (mine.numTicks == 1003) {
                Platform.runLater {
                    refresh()
                }
                this.cancel()
                println("Stopping")

            }
        }
    }

}