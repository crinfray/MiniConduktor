package dev.taranys.ui

import dev.taranys.kafka.Broker
import dev.taranys.kafka.Topic
import dev.taranys.kafka.brokerConfig
import dev.taranys.kafka.connect
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.Controller
import tornadofx.observableListOf
import java.util.Properties

/**
 * [Controller] class for [BrokerView]
 */
class BrokerController : Controller() {
    val brokerConfig = SimpleObjectProperty<Properties>()
    val brokerLoaded = SimpleBooleanProperty(false)
    val bootstrapServers = SimpleStringProperty("")
    val additionalProperties = SimpleStringProperty("")
    val topics = observableListOf<TopicModel>()

    fun connectToBroker(): Broker {
        brokerLoaded.set(false)
        topics.setAll()
        brokerConfig.value = brokerConfig(bootstrapServers.value, additionalProperties.value)
        return connect(brokerConfig.value).also {
            updateTopics(it.topics)
            brokerLoaded.set(true)
        }
    }

    private fun updateTopics(brokerTopics: List<Topic>) {
        topics.setAll(brokerTopics.map { it.toModel() })
    }

    private fun Topic.toModel() = TopicModel(name, id)
}