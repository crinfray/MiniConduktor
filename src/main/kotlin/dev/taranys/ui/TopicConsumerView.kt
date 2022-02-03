//package dev.taranys.ui
//
//import javafx.scene.Parent
//import tornadofx.*
//
//class TopicConsumerView : Fragment("My View") {
//    val consumer: TopicConsumer by inject()
//    override val root = listview(consumer.events) {
//cellFragment(EventFragment::class)
//    }
//}
//
//class EventFragment : ListCellFragment<Any>() {
//    override val root = hbox {
//        label("Replace by event value")
//    }
//
//}
//
//class TopicConsumer : Controller() {
//    val events: List<Any> = emptyList()
//}
