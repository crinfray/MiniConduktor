package dev.taranys.ui

import dev.taranys.kafka.consumer
import dev.taranys.kafka.flow
import javafx.beans.property.SimpleBooleanProperty
import javafx.concurrent.ScheduledService
import javafx.concurrent.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.ConsumerRecord
import tornadofx.Controller
import tornadofx.observableListOf
import java.util.Properties

/**
 * [Controller] for the [TopicConsumerView]
 */
class TopicConsumerController(val topicName: String, brokerProperties: Properties) : Controller() {

    val records = observableListOf<ConsumerRecord<String, String>>()
    val consumer = consumer(topicName, brokerProperties)
    val stopped = SimpleBooleanProperty(true)

    private val scheduledService = object : ScheduledService<Unit>() {
        init {
            period = javafx.util.Duration.seconds(1.0)
        }

        override fun createTask() = ConsumerPollTask()
    }

    fun start() {
        println("Starting polling")
        scheduledService.restart()
        stopped.value = false
    }

    fun stop() {
        println("Stopping polling")
        scheduledService.cancel()
        stopped.value = true
    }

    inner class ConsumerPollTask : Task<Unit>() {
        override fun call() {
            GlobalScope.launch(Dispatchers.Main) {
                consumer.flow().collect { records.add(it) }
            }
        }
    }
}