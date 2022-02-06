package dev.taranys.ui

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val h2 by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }
        h2 {
            padding = box(0.px, 0.px, 10.px, 0.px)
            fontSize = 15.px
            fontWeight = FontWeight.BOLD
        }
    }
}