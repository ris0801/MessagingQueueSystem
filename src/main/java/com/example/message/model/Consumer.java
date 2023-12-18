package com.example.message.model;

import java.util.HashMap;
import java.util.Map;

public class Consumer {
    private String consumerId;
    private String groupId;
    private Map<String, Long> topicPartitionOffset;  // Key: "topicName-partitionId", Value: offset

    public Consumer(String consumerId, String groupId) {
        this.consumerId = consumerId;
        this.groupId = groupId;
        this.topicPartitionOffset = new HashMap<>();
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Map<String, Long> getTopicPartitionOffset() {
        return topicPartitionOffset;
    }

    public void setTopicPartitionOffset(Map<String, Long> topicPartitionOffset) {
        this.topicPartitionOffset = topicPartitionOffset;
    }

    public void setOffsetForPartition(String topicName, int partitionId, long offset) {
        this.topicPartitionOffset.put(topicName + "-" + partitionId, offset);
    }

    public long getOffsetForPartition(String topicName, int partitionId) {
        return this.topicPartitionOffset.getOrDefault(topicName + "-" + partitionId, (long) 0);
    }

}
