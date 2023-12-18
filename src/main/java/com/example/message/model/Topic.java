package com.example.message.model;

import java.util.List;

public class Topic {
    private String name;
    private List<Partition> partitions;

    public Topic(String name, List<Partition> partitions) {
        this.name = name;
        this.partitions = partitions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Partition> getPartitions() {
        return partitions;
    }

    public void setPartitions(List<Partition> partitions) {
        this.partitions = partitions;
    }
}

