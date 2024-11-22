package models;

import java.sql.Timestamp;

// AuditLog class representing the audit_log table
public class AuditLog {
    private int id;
    private int userId;
    private String action;
    private Timestamp createdAt;

    // Constructor
    public AuditLog(int id, int userId, String action, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
