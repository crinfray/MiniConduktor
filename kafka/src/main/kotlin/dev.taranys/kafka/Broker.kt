package dev.taranys.kafka

/**
 * Model wrapping broker information
 */
data class Broker(val topics: List<Topic>)