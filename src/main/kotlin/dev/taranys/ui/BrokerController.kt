package dev.taranys.ui

import dev.taranys.kafka.Broker
import dev.taranys.kafka.connect
import dev.taranys.ui.TopicListingModel.Companion.toModel
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.admin.TopicListing
import tornadofx.Controller
import tornadofx.observableListOf
import java.io.StringReader
import java.util.Properties

/**
 * [Controller] class for [BrokerView]
 */
class BrokerController : Controller() {
    val brokerConfig = SimpleObjectProperty<Properties>()
    val bootstrapServers = SimpleStringProperty("localhost:9092")
    val additionalProperties = SimpleStringProperty("")
    val topics = observableListOf<TopicListingModel>()

    fun connectToBroker(): Broker {
        brokerConfig.value = brokerConfig(bootstrapServers.value, additionalProperties.value)
        return connect(brokerConfig.value).also { updateTopics(it.topics) }
    }

    private fun updateTopics(brokerTopics: Map<String, TopicListing>) {
        topics.setAll(brokerTopics.map { it.value.toModel() })
    }

    private fun brokerConfig(bootstrapServers: String, props: String) = Properties().apply {
        load(StringReader(props))
        put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    }
}