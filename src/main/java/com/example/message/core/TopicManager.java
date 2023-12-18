package com.example.message.core;

import com.example.message.model.Partition;
import com.example.message.model.Topic;
import com.example.message.storage.MessageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TopicManager {

    private static Map<String, Topic> topics = new HashMap<>();

    private Map<String, Integer> topicPartitionIndices = new HashMap<>();

    @Autowired
    private MessageStore messageStore;

    public synchronized void createTopic(String topicName, int numPartitions) {
        List<Partition> partitions = new ArrayList<>();
        for (int i = 0; i < numPartitions; i++) {
            Partition temp = new Partition(topicName, i);
            temp.addReplica("testTopic/replica0");
            temp.addReplica("testTopic/replica1");
            partitions.add(temp);
        }
        topics.put(topicName, new Topic(topicName, partitions));
    }

    public void sendMessageToTopic(String topicName, String message, String key) {
        Topic topic = topics.get(topicName);
        if (topic == null) {
            throw new RuntimeException("Topic not found: " + topicName);
        }
        //testTopic/partition0
        Partition partition;
        if (key == null) {
            // Round-robin distribution
            partition = roundRobinPartitionSelection(topic);
        } else {
            // Key-based distribution
            partition = keyBasedPartitionSelection(topic, key);
        }
        // Store the message in the selected partition and its replicas
        try {
            //TODO: Message is not getting stored properly in replicas
            replicateMessage(partition, message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void replicateMessage(Partition partition, String message) throws IOException {
        // Write to the main partition
        messageStore.appendMessage(partition.getPartitionPath(), message);

        // Write to all replicas
        for (String replicaPath : partition.getReplicas()) {
            messageStore.appendMessage(replicaPath, message);
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

