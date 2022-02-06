package dev.taranys.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import java.time.LocalDateTime

/**
 * Representation of a Kafka [ConsumerRecord]
 */
data class Record(val key: String, val value: String, val partition: Int, val date: LocalDateTime)
