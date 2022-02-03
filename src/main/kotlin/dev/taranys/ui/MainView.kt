package dev.taranys.ui

import javafx.geometry.Pos
import tornadofx.*

class MainView : View("Mini Conduktor") {
    override val root = vbox {
        label(title) {
            addClass(Styles.heading)
        }
        hbox(alignment = Pos.CENTER) {
            borderpane {
                center<BrokerView>()
            }
        }
    }
}
