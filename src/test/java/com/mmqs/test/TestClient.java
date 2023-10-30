package com.mmqs.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send a create topic command
            out.println("CREATE myTopic 3");
            System.out.println("Server Response: " + in.readLine());

            // Send a message
            out.println("SEND myTopic Hello");
            System.out.println("Server Response: " + in.readLine());

            // Fetch a message (assuming you've implemented this)
            // Note: Implement appropriate consumer group and consumer before fetching
            // out.println("FETCH ...");
            // System.out.println("Server Response: " + in.readLine());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
