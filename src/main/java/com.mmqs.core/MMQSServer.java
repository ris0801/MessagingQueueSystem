package com.mmqs.core;

import com.mmqs.net.ClientHandler;

import java.io.*;
import java.net.*;

public class MMQSServer {

    private static final int PORT = 9090;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("MMQS Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
1. CREATE testTopic 1
2. SEND testTopic Hello World!
3. CGROUP 2
4. ADD_CONSUMER 100 2
5. FETCH 100 2 testTopic
* **/
