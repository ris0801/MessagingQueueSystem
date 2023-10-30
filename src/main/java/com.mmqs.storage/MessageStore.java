package com.mmqs.storage;

import java.io.*;

public class MessageStore {

    private static final String DATA_DIR = "data";

    public synchronized void appendMessage(String topic, String message) throws IOException {
        File topicDir = new File(DATA_DIR, topic);
        if (!topicDir.exists()) {
            topicDir.mkdirs();
        }

        File partitionFile = new File(topicDir, "partition0");
        try (FileWriter fw = new FileWriter(partitionFile, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(message);
            bw.newLine();
        }
    }

    public String readMessageAtOffset(String topic, long offset) throws IOException {
        File topicDir = new File(DATA_DIR, topic);
        File partitionFile = new File(topicDir, "partition0");

        try (RandomAccessFile raf = new RandomAccessFile(partitionFile, "r")) {
            raf.seek(offset);
            return raf.readLine();
        }
    }

    public String readMessage(String topicName, int partitionId, long offset) {
        String partitionPath = getPartitionPath(topicName, partitionId);

        try (RandomAccessFile raf = new RandomAccessFile(partitionPath, "r")) {
            raf.seek(offset);
            String message = raf.readLine();
            return message;
        } catch (Exception e) {
            // Handle exceptions appropriately, e.g., logging, rethrowing, etc.
            e.printStackTrace();
            return null;
        }
    }

    private String getPartitionPath(String topicName, int partitionId) {
        return "./data/" + topicName + "/partition" + partitionId + "/" +  "partition" + partitionId;
    }

}

