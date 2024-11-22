package models;

import java.sql.Timestamp;

// Transaction class representing the transactions table
public class Transaction {
    private int id;
    private int senderId;
    private int receiverId;
    private double amount;
    private String description;
    private Timestamp createdAt;

    // Constructor
    public Transaction(int id, int senderId, int receiverId, double amount, String description, Timestamp createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
