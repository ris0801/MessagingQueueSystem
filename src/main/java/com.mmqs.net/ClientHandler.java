package com.mmqs.net;

import com.mmqs.core.ConsumerManager;
import com.mmqs.core.TopicManager;
import com.mmqs.model.Partition;
import com.mmqs.model.Topic;
import com.mmqs.storage.MessageStore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private TopicManager topicManager = new TopicManager();
    private MessageStore messageStore = new MessageStore();
    private ConsumerManager consumerManager = new ConsumerManager();

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("SEND")) {
                    // Format: SEND [topic] [message]
                    String[] parts = inputLine.split(" ", 3);
                    String topicName = parts[1];
                    String message = parts[2];

                    // Save message logic
                    Topic topic = topicManager.getTopic(topicName);
                    if (topic == null) {
                        out.println("Topic not found: " + topicName);
                        continue;
                    }
                    Partition partition = topicManager.getNextPartitionForTopic(topic);
                    messageStore.appendMessage(partition.getPartitionPath(), message);

                    out.println("Message sent to topic: " + topicName);

                } else if (inputLine.startsWith("CREATE")) {
                    // Format: CREATE [topic] [numPartitions]
                    String[] parts = inputLine.split(" ");
                    String topicName = parts[1];
                    int numPartitions = Integer.parseInt(parts[2]);

                    // Create topic logic
                    topicManager.createTopic(topicName, numPartitions);

                    out.println("Topic created: " + topicName);

                } else if (inputLine.startsWith("CGROUP")) {
                    // Format: CGROUP [groupId]
                    String groupId = inputLine.split(" ")[1];
                    consumerManager.createConsumerGroup(groupId);
                    out.println("Consumer group created: " + groupId);

                } else if (inputLine.startsWith("ADD_CONSUMER")) {
                    // Format: ADD_CONSUMER [consumerId] [groupId]
                    String[] parts = inputLine.split(" ");
                    String consumerId = parts[1];
                    String groupId = parts[2];
                    consumerManager.addConsumerToGroup(consumerId, groupId);
                    out.println("Consumer added to group.");

                } else if (inputLine.startsWith("FETCH")) {
                    // Format: FETCH [consumerId] [groupId] [topic]
                    String[] parts = inputLine.split(" ");
                    String consumerId = parts[1];
                    String groupId = parts[2];
                    String topic = parts[3];
                    String message = consumerManager.fetchMessage(consumerId, groupId, topic);
                    out.println("Fetched: " + message);
                } else {
                    out.println("Unknown command.");
                }
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
