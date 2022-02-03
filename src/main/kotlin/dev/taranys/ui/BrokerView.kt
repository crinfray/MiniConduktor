package dev.taranys.ui

import dev.taranys.ui.TopicListingModel.Companion.toModel
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.View
import tornadofx.action
import tornadofx.addClass
import tornadofx.asObservable
import tornadofx.box
import tornadofx.button
import tornadofx.column
import tornadofx.field
import tornadofx.fieldset
import tornadofx.form
import tornadofx.label
import tornadofx.prefWidth
import tornadofx.px
import tornadofx.removeWhen
import tornadofx.smartResize
import tornadofx.style
import tornadofx.tableview
import tornadofx.textarea
import tornadofx.textfield
import tornadofx.vbox

/**
 * View containing Broker setup/connexion form and broker topics list
 */
class BrokerView : View() {

    private val controller: BrokerController by inject()
    private val bootstrapServers = SimpleStringProperty("localhost:9092")
    private val additionalProperties = SimpleStringProperty("")

    private var topicsForTable = mutableListOf<TopicListingModel>().asObservable()
    private var brokerLoaded = SimpleBooleanProperty(false)

    override val root = vbox {
        label("Broker Setup") {
            addClass(Styles.h2)
        }
        form {
            fieldset {
                field("Bootstrap servers:") {
                    textfield(bootstrapServers)
                }
                field("Additional Properties:") {
                    textarea(additionalProperties)
                }

                button("Connect to broker") {
                    action {
                        runAsync {
                            controller.connectToBroker(bootstrapServers.value, additionalProperties.value)
                        } ui {
                            topicsForTable.apply {
                                removeAll { true }
                                addAll(it.topics.map { it.value.toModel() }) }
                            brokerLoaded.set(true)
                        }
                    }
                }
            }
        }
        label("Topics") {
            addClass(Styles.h2)
        }
        tableview(topicsForTable) {
            style {
                maxHeight = 150.px
            }
            column("ID", TopicListingModel::topicId).prefWidth(200.0)
            column("Name", TopicListingModel::name)
            smartResize()
            removeWhen { brokerLoaded.not() }
        }
        label("Connect to broker to view topics list") {
            style {
                padding = box(10.px)
            }
            removeWhen { brokerLoaded }
        }
    }
}