package dev.taranys.kafka

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.admin.Admin
import org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import java.io.StringReader
import java.time.Duration.ofMillis
import java.time.Instant.ofEpochMilli
import java.time.LocalDateTime
import java.time.LocalDateTime.ofInstant
import java.time.ZoneId.systemDefault
import java.util.Properties

/**
 * Connect to Broker and retrieve global information
 */
fun connect(properties: Properties): Broker {
    val admin = Admin.create(properties)
    val topics = admin.listTopics().listings().get().map { Topic(it.name(), it.topicId().toString()) }
    admin.close()
    return Broker(topics)
}

/**
 * Returns a [KafkaConsumer] configured to listen to the provided topic
 */
fun consumer(topicName: String, properties: Properties) = kafkaConsumer(topicName, properties.consumerConfig())

/**
 * Polls for new [ConsumerRecord] and emit received elements in a [Flow]
 */
fun <K, V> KafkaConsumer<K, V>.flow() = poll(ofMillis(100)).map { it.toRecord() }.asFlow().flowOn(Dispatchers.IO)

/**
 * Creates a [Properties] object that can be used to connect to the broker using the [connect] method.
 * @param bootstrapServers The servers urls
 * @param props Other connection properties in the form: propName=propValue separated by new lines
 */
fun brokerConfig(bootstrapServers: String, props: String) = Properties().apply {
    load(StringReader(props))
    put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
}

private fun Properties.consumerConfig() = Properties().apply {
    putAll(this@consumerConfig)
    put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.qualifiedName)
    put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.qualifiedName)
    put(AUTO_OFFSET_RESET_CONFIG, "earliest")
}

private fun kafkaConsumer(topicName: String, properties: Properties) = KafkaConsumer<String, String>(properties).apply {
    assign(partitions(topicName))
}

private fun <K, V> KafkaConsumer<K, V>.partitions(topicName: String): List<TopicPartition> {
    return partitionsFor(topicName).map { TopicPartition(it.topic(), it.partition()) }
}

private fun <K, V> ConsumerRecord<K, V>.toRecord() = Record(key().toString(), value().toString(), partition(), timestamp().toLocalDateTime())

private fun Long.toLocalDateTime(): LocalDateTime = ofInstant(ofEpochMilli(this), systemDefault())