# Description
MessagingQueueSystem is a simplified implementation of a messaging queue system inspired by Apache Kafka. The project is created for educational purposes to understand the inner workings of Kafka-like systems, including message persistence, topic and partitioning, consumer groups, offset management, and basic replication.

## Table of Contents
- [Installation](#installation)
- [Getting Started](#getting-started)
- [Features](#features)
- [Usage](#usage)
- [Testing](#testing)

## Installation

The project is developed using Java. Ensure you have Java installed on your machine before running the application. Clone the repository to your local machine to get started.

## Getting Started

Navigate to the project directory.
Compile the Java application.
Run the application, ensuring it is listening for TCP connections on port 9090.

## Features:
(Not all the below features are implemented)
- Message Persistence: Messages sent to a topic are appended to a respective file (partition), and can be read from a specific offset. (Done)
- Topic and Partitioning: Support for creating topics with a specified number of partitions and distributing incoming messages across partitions. (Done)
- Consumer Groups and Offset Management: Support for multiple consumers forming a group and reading from a topic, with each consumer reading from a unique partition. (Done)
- Basic Replication: Each partition can have one or more replicas, with messages replicated to all replicas. 
- ZooKeeper Integration: Use of ZooKeeper for storing metadata and handling node failures and leader election for partitions.
- Fault Tolerance: Handling node failures by redirecting traffic to replicas and syncing missed messages when a failed node comes back online.
- Basic Security: Basic authentication for producers and consumers, with message encryption in transit.
- Testing and Monitoring: Logging to monitor system activity and tests to simulate various system behaviors.


## Usage

Interact with the system using a TCP client like telnet. Below are some example commands and expected responses:

```sh
$ telnet localhost 9090
Trying 127.0.0.1...
Connected to localhost.
SEND topic1 Hello World
>Message sent to topic: topic1
```


## Testing

The system can be tested by connecting to it via a TCP client and executing various commands to send messages, consume messages, create topics, etc.

```sh 
$ telnet localhost 9090
Trying 127.0.0.1...
Connected to localhost.
Escape character is '^]'.
SEND Hello MMQS
>Message received: Hello MMQS
CONSUME
>No messages to consume
```
