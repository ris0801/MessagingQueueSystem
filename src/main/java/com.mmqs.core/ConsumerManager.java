package com.mmqs.core;

import com.mmqs.model.Consumer;
import com.mmqs.model.ConsumerGroup;
import com.mmqs.model.Partition;
import com.mmqs.model.Topic;
import com.mmqs.storage.MessageStore;

import java.util.HashMap;
import java.util.Map;

public class ConsumerManager {

    private Map<String, ConsumerGroup> consumerGroups = new HashMap<>();
    private MessageStore messageStore = new MessageStore();

    private TopicManager topicManager = new TopicManager();

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

        String message = messageStore.readMessage(topicName, partitionId, currentOffset);

        if (message != null) {
            consumer.setOffsetForPartition(topicName, partitionId, currentOffset + 1);
        }
        return message;
    }

    private int assignPartitionToConsumer(Consumer consumer, Topic topic) {
        // Here, we'll implement a simplistic assignment strategy.
        // For a real-world application, this will need to be more advanced.
        return consumer.getConsumerId().hashCode() % topic.getPartitions().size();
    }

}
