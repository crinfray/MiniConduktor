package dev.taranys.ui

import dev.taranys.kafka.Broker
import dev.taranys.kafka.KafkaService
import org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG
import tornadofx.Controller
import java.io.StringReader
import java.util.Properties

/**
 * [Controller] class for [BrokerView]
 */
class BrokerController : Controller() {
    private val kafkaService = KafkaService()

    fun connectToBroker(bootstrapServers: String, props: String): Broker {
        return kafkaService.connect(brokerConfig(bootstrapServers, props))
    }

    private fun brokerConfig(bootstrapServers: String, props: String) = Properties().apply {
        load(StringReader(props))
        put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    }
}