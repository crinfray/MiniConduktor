package dev.taranys.kafka

import org.apache.kafka.clients.admin.TopicListing

/**
 * Model wrapping broker information
 */
data class Broker(val topics: Map<String, TopicListing>)