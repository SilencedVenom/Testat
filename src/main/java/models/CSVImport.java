package models;

import java.sql.Timestamp;

// CSVImport class representing the csv_imports table
public class CSVImport {
    private int id;
    private int userId;
    private String filePath;
    private String status;
    private Timestamp createdAt;

    // Constructor
    public CSVImport(int id, int userId, String filePath, String status, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.filePath = filePath;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
