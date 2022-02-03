package dev.taranys.kafka

import org.apache.kafka.clients.admin.Admin
import java.util.Properties

/**
 * Service connecting to Kafka Broker
 */
class KafkaService {

    /**
     * Connect to Broker and retrieve global information
     */
    fun connect(properties: Properties): Broker {
        val admin = Admin.create(properties)
        val topicsMap = admin.listTopics().namesToListings().get()
        admin.close()
        return Broker(topicsMap)
    }
}