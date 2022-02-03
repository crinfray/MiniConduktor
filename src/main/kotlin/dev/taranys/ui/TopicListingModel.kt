package dev.taranys.ui

import javafx.beans.property.SimpleStringProperty
import org.apache.kafka.clients.admin.TopicListing

/**
 * Model to display [TopicListing] in a table view
 */
class TopicListingModel(name: String, topicId: String) {
    val name = SimpleStringProperty(name)
    val topicId = SimpleStringProperty(topicId)
    companion object {
        fun TopicListing.toModel() = TopicListingModel(name(), topicId().toString())
    }
}