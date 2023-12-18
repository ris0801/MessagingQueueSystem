package com.example.message.model;

import java.util.HashMap;
import java.util.Map;

public class ConsumerGroup {
    private String groupId;
    private Map<String, Consumer> consumers; // Key: consumerId, Value: Consumer

    public ConsumerGroup(String groupId) {
        this.groupId = groupId;
        this.consumers = new HashMap<>();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Map<String, Consumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(Map<String, Consumer> consumers) {
        this.consumers = consumers;
    }
}