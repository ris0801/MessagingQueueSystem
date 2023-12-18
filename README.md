# Description
MessagingQueueSystem is a simplified implementation of a messaging queue system inspired by Apache Kafka. The project is created for educational purposes to understand the inner workings of Kafka-like systems, including message persistence, topic and partitioning, consumer groups, offset management, and basic replication.

Note: Note all of these functionalities do not meet the actual Kafka implementation. This is just an inspiration and a personal project to understand the concepts.

## Table of Contents
- [Installation](#installation)
- [Getting Started](#getting-started)
- [Features](#features)
- [Usage](#usage)
- [Testing](#testing)

## Installation

The project is developed using Java and Springboot. Ensure you have Java installed on your machine before running the application. Clone the repository to your local machine to get started.

## Getting Started

Navigate to the project directory.
It is springboot application. 
There are 3 classes that need to be running for functionalities of this application
1. MessageApplication : com/example/message/MessageApplication.java - Springboot application startup
2. ProducerGUI : com/example/message/net/ProducerGUI.java - Producer to create messages and write to a topic
3. ConsumerGUI : com/example/message/net/ConsumerGUI.java

## Features:
Implemented Features: 
- Message Persistence: Messages sent to a topic are appended to a respective file (partition), and can be read from a specific offset. 
- Topic and Partitioning: Support for creating topics with a specified number of partitions and distributing incoming messages across partitions.
- Consumer Groups and Offset Management: Support for multiple consumers forming a group and reading from a topic, with each consumer reading from a unique partition. 
- Basic Replication: Each partition can have one or more replicas, with messages replicated to all replicas. 
- Fault Tolerance: Handling node failures by redirecting traffic to replicas and syncing missed messages when a failed node comes back online.
 
Features to be added in future: 
- ZooKeeper Integration: Use of ZooKeeper for storing metadata and handling node failures and leader election for partitions.
- Basic Security: Basic authentication for producers and consumers, with message encryption in transit.

## Basic concepts (Will also help in validating the functioning of the application)

*Kafka Topics* : 
A Kafka topic is a category for broadcasting messages, akin to an email folder for sorting your emails. Topics are divided into partitions, similar to sub-folders, each on a different server. This structure is beneficial for managing and scaling large data volumes. Producers send messages to topics, and consumers retrieve them.

*Partitions in Kafka* : 
Partitions are sequences within a topic, like chapters in a book, keeping messages in chronological order. They enable parallel processing, as each partition can be independently accessed and processed across multiple servers. For data safety, partitions are replicated across servers, protecting against data loss during server failures.

*Kafka Consumers* :
Consumers in Kafka are like viewers choosing a TV channel, selecting topics to read messages from. They process the incoming data and can be part of consumer groups for better message management.

*Consumer Groups* : 
A consumer group is a collection of consumers collaboratively processing messages from a topic. Each consumer reads from specific partitions, ensuring workload distribution and efficient message processing. If a consumer fails, its workload is reassigned within the group, maintaining continuous data flow.

## Usage

1. Bring up the Springboot startup application - MessageApplication
2. Bring up ProducerGUI (com/example/message/net/ProducerGUI.java)
3. Bring up ConsumerGUI (com/example/message/net/ConsumerGUI.java)

## Example: 

1. **Producer and Consumer Interaction**
   - In `ProducerGUI`, fill in the fields:
     - Topic: `testTopic`
     - Partitions: `1`
     - Message: `hello`
   - In `ConsumerGUI`, fill in the fields:
     - GroupId: `100`
     - Consumer: `2`
     - Topic: `testTopic`
   - Then, click on `Fetch Message`. 
   - You should be able to fetch the message. 
   - Continue producing and fetching messages. This should work seamlessly.

2. **Group Subscription and Consumer Behavior**
   - This application allows a group to subscribe to a topic, with all consumers within that group able to read from the topic.
     - Note: This differs from Kafka's typical configuration where multiple groups can read from a single topic.
     - All consumers can read messages from the start of a topic, which is also a configurable feature in Kafka.
   - To test this:
     - Add `Consumer: 5` to the existing `Group: 100`.
     - Fetch messages; new consumers should be able to read from the start.
     - Change to `Group: 105`, `Consumer: 10`.
     - You should not be able to read messages from the topic.

3. **Experimentation with Topics and Groups**
   - Try writing messages to different topics.
   - Experiment with different combinations, such as the same consumer and group, or a new consumer and new group, to read from the new topic.
   - All combinations should function correctly.

4. **Simulating Partition Failure**
   - Notice the hardcoded 2 replicas for each partition, providing redundancy.
   - To simulate a partition failure:
     - Delete the partition file from the `data` folder.
     - Continue reading messages from the same topic; they should be accessible from the replicas.

5. **Data Persistence and Application Restart**
   - The application maintains data persistence in files.
   - If you restart the application, create the topic again, and continue reading, it should function correctly.
   - Note: Messages will be read from the beginning due to the current implementation.
   - Maintaining the offset across restarts would require storing the offset in a persistent data store and using it to read messages from a specific offset.

