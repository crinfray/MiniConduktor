package dev.taranys.ui

import dev.taranys.kafka.Record
import javafx.geometry.Pos
import tornadofx.Fragment
import tornadofx.action
import tornadofx.addClass
import tornadofx.borderpane
import tornadofx.cache
import tornadofx.hbox
import tornadofx.hiddenWhen
import tornadofx.label
import tornadofx.listview
import tornadofx.progressindicator
import tornadofx.togglebutton
import tornadofx.vbox
import java.time.format.DateTimeFormatter

/**
 * View for real time topic records visualization
 */
class TopicConsumerView : Fragment() {
    private val controller: TopicConsumerController by inject()

    init {
        titleProperty.set("Consumer for topic: ${controller.topicName}")
        controller.start()
    }

    override val root = borderpane {
        top = label(controller.topicName) {
            addClass(Styles.h2)
        }
        center = listview(controller.records) {
            cellFormat {
                graphic = cache {
                    vbox {
                        hbox(spacing = 5) {
                            label("Date: ${item.viewDate()}")
                            label("Partition: ${item.partition}")
                        }
                        hbox(spacing = 5) {
                            label("Key: ${item.key}")
                            label("Value: ${item.value}")
                        }
                    }
                }
            }
        }
        bottom = hbox(spacing = 25, alignment = Pos.CENTER) {
            minHeight = 100.0
            togglebutton {
                text = if (isSelected) "Stop recording" else "Start recording"
                action {
                    if (isSelected) {
                        text = "Stop recording"
                        controller.start()
                    } else {
                        text = "Start recording"
                        controller.stop()
                    }
                }
            }
            progressindicator {
                hiddenWhen { controller.stopped }
            }
        }
    }

    override fun onDock() {
        setWindowMinSize(600, 600)
        super.onDock()
    }

    override fun onUndock() {
        controller.stop()
        super.onUndock()
    }
}

private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
private fun Record.viewDate() = date.format(formatter)
