package models;

import java.sql.Timestamp;

// Message class representing the messages table
public class Message {
    private int id;
    private int senderId;
    private int receiverId;
    private String message;
    private boolean isWallPost;
    private Timestamp createdAt;

    // Constructor
    public Message(int id, int senderId, int receiverId, String message, boolean isWallPost, Timestamp createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.isWallPost = isWallPost;
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

    public String getMessage() {
        return message;
    }

    public boolean isWallPost() {
        return isWallPost;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
