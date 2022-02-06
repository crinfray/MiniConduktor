package dev.taranys.ui

import dev.taranys.kafka.Topic
import javafx.beans.property.SimpleStringProperty

/**
 * Model to display [Topic] in a table view
 */
class TopicModel(name: String, topicId: String) {
    val name = SimpleStringProperty(name)
    val topicId = SimpleStringProperty(topicId)
}