package dev.taranys.ui

import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.Alert
import tornadofx.Scope
import tornadofx.View
import tornadofx.action
import tornadofx.addClass
import tornadofx.alert
import tornadofx.box
import tornadofx.button
import tornadofx.column
import tornadofx.fail
import tornadofx.field
import tornadofx.fieldset
import tornadofx.find
import tornadofx.form
import tornadofx.hbox
import tornadofx.label
import tornadofx.prefWidth
import tornadofx.progressindicator
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
    private val loading = SimpleBooleanProperty(false)

    override val root = vbox {
        label("Broker Setup") {
            addClass(Styles.h2)
        }
        form {
            fieldset {
                field("Bootstrap servers:") {
                    textfield(controller.bootstrapServers)
                }
                field("Additional Properties:") {
                    textarea(controller.additionalProperties)
                }

                button("Connect to broker") {
                    action {
                        loading.set(true)
                        runAsync {
                            controller.connectToBroker()
                        } ui {
                            loading.set(false)
                        } fail {
                            loading.set(false)
                            alert(Alert.AlertType.ERROR, "Broker connexion error", "${it.message}\n${it.cause?.message}")
                        }
                    }
                }
            }
        }
        label("Topics") {
            addClass(Styles.h2)
        }
        hbox(alignment = Pos.CENTER) {
            progressindicator()
            removeWhen { loading.not() }
        }
        tableview(controller.topics) {
            style {
                maxHeight = 150.px
            }
            column("ID", TopicModel::topicId).prefWidth(200.0)
            column("Name", TopicModel::name)
            column("View Records", TopicModel::name).cellFormat {
                graphic = hbox {
                    button("View Records") {
                        action {
                            val scope = Scope()
                            val controller = TopicConsumerController(it, controller.brokerConfig.value)
                            setInScope(controller, scope)
                            find(TopicConsumerView::class, scope).openModal()
                        }
                    }
                }
            }
            smartResize()
            removeWhen { controller.brokerLoaded.not().or(loading) }
        }
        label("Connect to broker to view topics list") {
            style {
                padding = box(10.px)
            }
            removeWhen { controller.brokerLoaded.or(loading) }
        }
    }
}