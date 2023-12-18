package com.example.message.net;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ProducerGUI {

    public ProducerGUI() {
        JFrame frame = new JFrame("Producer Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        placeProducerComponents(frame.getContentPane());
        frame.setVisible(true);
    }

    private void placeProducerComponents(Container pane) {
        pane.setLayout(null);

        // Components for createTopic
        JLabel topicLabel = new JLabel("Topic:");
        topicLabel.setBounds(10, 20, 80, 25);
        pane.add(topicLabel);

        JTextField topicText = new JTextField(20);
        topicText.setBounds(100, 20, 165, 25);
        pane.add(topicText);

        JLabel partitionLabel = new JLabel("Partitions:");
        partitionLabel.setBounds(10, 50, 80, 25);
        pane.add(partitionLabel);

        JTextField partitionText = new JTextField(20);
        partitionText.setBounds(100, 50, 165, 25);
        pane.add(partitionText);

        JButton createTopicButton = new JButton("Create Topic");
        createTopicButton.setBounds(10, 80, 150, 25);
        pane.add(createTopicButton);

        createTopicButton.addActionListener(e -> sendPostRequest(
                "http://localhost:9090/api/createTopic",
                "topicName=" + topicText.getText() + "&numPartitions=" + partitionText.getText()));

        // Components for sendMessage
        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setBounds(10, 110, 80, 25);
        pane.add(messageLabel);

        JTextField messageText = new JTextField(20);
        messageText.setBounds(100, 110, 165, 25);
        pane.add(messageText);

        JButton sendMessageButton = new JButton("Send Message");
        sendMessageButton.setBounds(10, 140, 150, 25);
        pane.add(sendMessageButton);

        sendMessageButton.addActionListener(e -> sendPostRequest(
                "http://localhost:9090/api/sendMessage",
                "topicName=" + topicText.getText() + "&message=" + messageText.getText()));
    }

    private static void sendPostRequest(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Send request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get Response
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProducerGUI());
    }
}
