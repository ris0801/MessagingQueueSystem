package com.mmqs.core;

import com.mmqs.model.Partition;
import com.mmqs.model.Topic;
import com.mmqs.storage.MessageStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicManager {

    private static Map<String, Topic> topics = new HashMap<>();

    private Map<String, Integer> topicPartitionIndices = new HashMap<>();
    private MessageStore messageStore = new MessageStore();

    public synchronized void createTopic(String topicName, int numPartitions) {
        List<Partition> partitions = new ArrayList<>();
        for (int i = 0; i < numPartitions; i++) {
            partitions.add(new Partition(topicName, i));
        }
        topics.put(topicName, new Topic(topicName, partitions));
    }

    public void sendMessageToTopic(String topicName, String message, String key) {
        Topic topic = topics.get(topicName);
        if (topic == null) {
            throw new RuntimeException("Topic not found: " + topicName);
        }

        Partition partition;
        if (key == null) {
            // Round-robin distribution
            partition = roundRobinPartitionSelection(topic);
        } else {
            // Key-based distribution
            partition = keyBasedPartitionSelection(topic, key);
        }

        // Store the message in the selected partition
        try {
            messageStore.appendMessage(partition.getPartitionPath(), message);
        } catch (IOException e) {
            /**
             * TODO: This needs to be handled properly
             * */
            throw new RuntimeException(e);
        }
    }

    private Partition roundRobinPartitionSelection(Topic topic) {
        // Logic for round-robin selection across partitions
        // This is a simplistic example
        List<Partition> partitions = topic.getPartitions();
        long currentTime = System.currentTimeMillis();
        int index = (int) (currentTime % partitions.size());
        return partitions.get(index);
    }

    private Partition keyBasedPartitionSelection(Topic topic, String key) {
        // Logic for key-based selection. Hash the key and modulo with partition count.
        List<Partition> partitions = topic.getPartitions();
        int index = key.hashCode() % partitions.size();
        return partitions.get(index);
    }

    public Partition getNextPartitionForTopic(Topic topic) {
        String topicName = topic.getName();

        int currentPartitionIndex = topicPartitionIndices.getOrDefault(topicName, 0);
        Partition nextPartition = topic.getPartitions().get(currentPartitionIndex);

        // Update the index for next time.
        currentPartitionIndex = (currentPartitionIndex + 1) % topic.getPartitions().size();
        topicPartitionIndices.put(topicName, currentPartitionIndex);

        return nextPartition;
    }

    public Topic getTopic(String topicName) {
        return topics.get(topicName);
    }
}
