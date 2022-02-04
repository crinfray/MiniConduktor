package dev.taranys.kafka

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import org.apache.kafka.clients.admin.Admin
import org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration.ofMillis
import java.util.Properties
import kotlin.reflect.jvm.jvmName

/**
 * Connect to Broker and retrieve global information
 */
fun connect(properties: Properties): Broker {
    val admin = Admin.create(properties)
    val topicsMap = admin.listTopics().namesToListings().get()
    admin.close()
    return Broker(topicsMap)
}

/**
 * Returns a [KafkaConsumer] configured to listen to the provided topic
 */
fun consumer(topicName: String, properties: Properties) = kafkaConsumer(topicName, properties.consumerConfig())

/**
 * Polls for new [ConsumerRecord] and emit received elements in a [Flow]
 */
fun <K, V> KafkaConsumer<K, V>.flow() = poll(ofMillis(100)).asFlow().flowOn(Dispatchers.IO)

private fun Properties.consumerConfig() = Properties().apply {
    putAll(this@consumerConfig)
    put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.jvmName)
    put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.jvmName)
    put(AUTO_OFFSET_RESET_CONFIG, "earliest")
}

private fun kafkaConsumer(topicName: String, properties: Properties) = KafkaConsumer<String, String>(properties).apply {
    assign(partitions(topicName))
}

private fun <K, V> KafkaConsumer<K, V>.partitions(topicName: String): List<TopicPartition> {
    return partitionsFor(topicName).map { TopicPartition(it.topic(), it.partition()) }
}