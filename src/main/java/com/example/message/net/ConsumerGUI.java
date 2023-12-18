package com.example.message.net;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConsumerGUI {

    public ConsumerGUI() {
        JFrame frame = new JFrame("Consumer Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        placeConsumerComponents(frame.getContentPane());
        frame.setVisible(true);
    }

    private void placeConsumerComponents(Container pane) {
        pane.setLayout(null);

        // Components for createConsumerGroup
        JLabel groupIdLabel = new JLabel("Group ID:");
        groupIdLabel.setBounds(10, 20, 80, 25);
        pane.add(groupIdLabel);

        JTextField groupIdText = new JTextField(20);
        groupIdText.setBounds(100, 20, 165, 25);
        pane.add(groupIdText);

        JButton createGroupButton = new JButton("Create Group");
        createGroupButton.setBounds(10, 50, 150, 25);
        pane.add(createGroupButton);

        createGroupButton.addActionListener(e -> sendPostRequest(
                "http://localhost:9090/api/createConsumerGroup",
                "groupId=" + groupIdText.getText()));

        // Components for addConsumer
        JLabel consumerIdLabel = new JLabel("Consumer ID:");
        consumerIdLabel.setBounds(10, 80, 80, 25);
        pane.add(consumerIdLabel);

        JTextField consumerIdText = new JTextField(20);
        consumerIdText.setBounds(100, 80, 165, 25);
        pane.add(consumerIdText);

        JButton addConsumerButton = new JButton("Add Consumer");
        addConsumerButton.setBounds(10, 110, 150, 25);
        pane.add(addConsumerButton);

        addConsumerButton.addActionListener(e -> sendPostRequest(
                "http://localhost:9090/api/addConsumer",
                "consumerId=" + consumerIdText.getText() + "&groupId=" + groupIdText.getText()));

        // Component for topic name input
        JLabel topicLabel = new JLabel("Topic:");
        topicLabel.setBounds(10, 170, 80, 25);
        pane.add(topicLabel);

        JTextField topicText = new JTextField(20);
        topicText.setBounds(100, 170, 165, 25);
        pane.add(topicText);

        // Components for fetchMessage
        JButton fetchMessageButton = new JButton("Fetch Message");
        fetchMessageButton.setBounds(10, 140, 150, 25);
        pane.add(fetchMessageButton);

        fetchMessageButton.addActionListener(e -> {
            String response = sendGetRequest("http://localhost:9090/api/fetchMessage?consumerId=" +
                    consumerIdText.getText() + "&groupId=" + groupIdText.getText() +
                    "&topicName=" + topicText.getText());
            JOptionPane.showMessageDialog(pane, response);
        });
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

    private static String sendGetRequest(String targetURL) {
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get Response
            InputStream is;
            if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                is = connection.getErrorStream();
            } else {
                is = connection.getInputStream();
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to fetch message";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConsumerGUI());
    }
}
