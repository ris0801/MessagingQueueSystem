package com.example.message.core;

import com.example.message.model.Consumer;
import com.example.message.model.ConsumerGroup;
import com.example.message.model.Partition;
import com.example.message.model.Topic;
import com.example.message.storage.MessageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConsumerManager {

    private Map<String, ConsumerGroup> consumerGroups = new HashMap<>();

    @Autowired
    private MessageStore messageStore;

    @Autowired
    private TopicManager topicManager;

    public synchronized void createConsumerGroup(String groupId) {
        if (!consumerGroups.containsKey(groupId)) {
            consumerGroups.put(groupId, new ConsumerGroup(groupId));
        }
    }

    public synchronized void addConsumerToGroup(String consumerId, String groupId) {
        ConsumerGroup group = consumerGroups.get(groupId);
        if (group == null) {
            throw new RuntimeException("Consumer Group not found: " + groupId);
        }
        if (!group.getConsumers().containsKey(consumerId)) {
            group.getConsumers().put(consumerId, new Consumer(consumerId, groupId));
        }
    }

    public String fetchMessage(String consumerId, String groupId, String topicName) {
        ConsumerGroup group = consumerGroups.get(groupId);
        Consumer consumer = group.getConsumers().get(consumerId);

        Topic topic = topicManager.getTopic(topicName);

        if (topic == null) {
            throw new RuntimeException("Topic not found: " + topicName);
        }

        int partitionId = assignPartitionToConsumer(consumer, topic);
        long currentOffset = consumer.getOffsetForPartition(topicName, partitionId);

        String message;
        try {
            // Check if partition is accessible before reading
            if (!isPartitionAccessible(topicName, partitionId)) {
                throw new PartitionFailureException("Partition is not accessible");
            }
            message = messageStore.readMessage(topicName, partitionId, currentOffset);
        } catch (Exception e) {
            // Handle partition failure or any other exception
            message = readFromReplica(topic, partitionId, currentOffset);
            System.out.println("Reading from replica");
        }
        //Logic for offset is handled here
        if (message != null) {
            long messageLength = message.length() + 1; // + length of delimiter if applicable
            consumer.setOffsetForPartition(topicName, partitionId, currentOffset + messageLength);
        }
        return message;
    }


//
    private boolean isPartitionAccessible(String topicName, int partitionId) {
        //TODO: This is working, but path cannot be hardcoded
        System.out.println(System.getProperty("user.dir"));
        String baseDir = System.getProperty("user.dir");
        Path partitionPath = Paths.get(baseDir,"/data/testTopic/partition0");
        return Files.exists(partitionPath);
    }

    private boolean isPathAccessible(String path) {
        Path partitionPath = Paths.get(path);
        return Files.exists(partitionPath);
    }


    private String readFromReplica(Topic topic, int partitionId, long offset) {
        Partition partition = topic.getPartitions().get(partitionId);
        for (String replicaPath : partition.getReplicas()) {
            try {
                return messageStore.readMessageAtOffset(replicaPath, offset);
            } catch (IOException e) {
                //TODO: 1. Logging
                //2: Trying to read from next replica, if reading from a replica fails
                System.err.println("Error reading from replica " + replicaPath + ": " + e.getMessage());
            }
        }
        //TODO: Logging can be added instead incase there is failure to read from any of the replicas
        System.err.println("Failed to read message from any replica for partition " + partitionId);
        return null;
    }
    private int assignPartitionToConsumer(Consumer consumer, Topic topic) {
        //Simple logic for assigning Partition to Consumer
        return consumer.getConsumerId().hashCode() % topic.getPartitions().size();
    }

}
