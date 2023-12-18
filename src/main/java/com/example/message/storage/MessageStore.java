package com.example.message.storage;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class MessageStore {

    private static final String DATA_DIR = "data";

    public synchronized void appendMessage(String path, String message) throws IOException {
        File file = new File(DATA_DIR, path);
        ensureDirectoryExists(file.getParentFile());

        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(message);
            bw.newLine();
        }
    }

    public String readMessageAtOffset(String path, long offset) throws IOException {
        File file = new File(DATA_DIR, path);

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(offset);
            return raf.readLine();
        }
    }

    private void ensureDirectoryExists(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
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
        return "./data/" + topicName + "/partition" + partitionId;
    }

}

