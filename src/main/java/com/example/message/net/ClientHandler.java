package com.example.message.net;


import com.example.message.core.ConsumerManager;
import com.example.message.core.TopicManager;
import com.example.message.model.Partition;
import com.example.message.model.Topic;
import com.example.message.storage.MessageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientHandler extends Thread {
    @Autowired
    private TopicManager topicManager;

    @Autowired
    private ConsumerManager consumerManager;

    @PostMapping("/createTopic")
    public String createTopic(@RequestParam String topicName, @RequestParam int numPartitions) {
        topicManager.createTopic(topicName, numPartitions);
        return "Topic created: " + topicName;
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam String topicName, @RequestParam String message) {
        // Assuming a key-less message for simplicity
        System.out.println("Request is made for " + topicName  + " and " +  message);
        topicManager.sendMessageToTopic(topicName, message, null);
        return "Message sent to topic: " + topicName;
    }

    @PostMapping("/createConsumerGroup")
    public String createConsumerGroup(@RequestParam String groupId) {
        consumerManager.createConsumerGroup(groupId);
        return "Consumer group created: " + groupId;
    }

    @PostMapping("/addConsumer")
    public String addConsumerToGroup(@RequestParam String consumerId, @RequestParam String groupId) {
        consumerManager.addConsumerToGroup(consumerId, groupId);
        return "Consumer " + consumerId + " added to group " + groupId;
    }

    @GetMapping("/fetchMessage")
    public String fetchMessage(@RequestParam String consumerId, @RequestParam String groupId, @RequestParam String topicName) {
        String message = consumerManager.fetchMessage(consumerId, groupId, topicName);
        return message != null ? message : "No new messages";
    }

}

