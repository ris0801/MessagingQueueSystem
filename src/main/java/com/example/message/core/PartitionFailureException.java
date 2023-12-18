package com.example.message.core;

public class PartitionFailureException extends Exception {
    // Constructor that accepts a message
    public PartitionFailureException(String message) {
        super(message);
    }

    // Constructor that accepts a message and a cause
    public PartitionFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    // Optional: Constructor that accepts only a cause
    public PartitionFailureException(Throwable cause) {
        super(cause);
    }
}
