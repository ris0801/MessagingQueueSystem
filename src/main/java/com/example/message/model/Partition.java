package com.example.message.model;

import java.util.ArrayList;
import java.util.List;

public class Partition {
    private String topicName;
    private int partitionId;

    private List<String> replicas; // List of replica paths

    public Partition(String topicName, int partitionId) {
        this.topicName = topicName;
        this.partitionId = partitionId;
        this.replicas = new ArrayList<>();
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

    public void addReplica(String replicaPath) {
        replicas.add(replicaPath);
    }

    public List<String> getReplicas() {
        return replicas;
    }

    public String getPartitionPath() {
        return topicName + "/partition" + partitionId;
    }
}
