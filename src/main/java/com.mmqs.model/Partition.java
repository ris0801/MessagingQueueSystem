package com.mmqs.model;

public class Partition {
    private String topicName;
    private int partitionId;

    public Partition(String topicName, int partitionId) {
        this.topicName = topicName;
        this.partitionId = partitionId;
    }
    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
    }

    public String getPartitionPath() {
        return topicName + "/partition" + partitionId;
    }
}