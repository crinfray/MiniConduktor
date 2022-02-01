package dev.taranys.ui

import tornadofx.*

class MainView : View("Mini Conduktor") {
    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }
    }
}
