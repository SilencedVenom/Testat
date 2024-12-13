package models;

import java.sql.Date;

// Transaction class representing the transactions table
public class Transaction {
    private final int senderId;
    private final int receiverId;
    private final double amount;
    private final String description;
    private final Date createdAt;
    private int id;

    // Constructor
    public Transaction(int id, int senderId, int receiverId, double amount, String description, Date createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    //Constructor
    public Transaction(int senderId, int receiverId, double amount, String description, Date createdAt) {
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

    public Date getCreatedAt() {
        return createdAt;
    }
}
