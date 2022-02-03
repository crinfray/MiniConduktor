package dev.taranys.ui

import javafx.stage.Stage
import tornadofx.*

class MyApp: App(MainView::class, Styles::class) {
    override fun start(stage: Stage) {
        with(stage) {
            width = 800.0
            height = 600.0
        }
        super.start(stage)
    }
}