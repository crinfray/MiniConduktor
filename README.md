# MiniConduktor

Implementation of a mini-conduktor for the [Conduktor challenge](https://github.com/conduktor/conduktor-coding-challenge/tree/main/kotlin-engineers)

Written in kotlin (1.6.0), targeting JVM 11.  
UI is using [tornadofx](https://github.com/edvin/tornadofx)

### Using this app you will be able to

- set a Kafka bootstrap address, and be able to set additional properties (security etc.)
- list the topics
- select a topic and consume its data from the beginning
- see the records flowing without the UI to be stuck in real-time