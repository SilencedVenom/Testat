package models;

import java.sql.Timestamp;

// User class representing the users table
public class User {
    private int id;
    private String email;
    private String password;
    private double balance;
    private Timestamp createdAt;

    // Constructor
    public User(int id, String email, String password, double balance, Timestamp createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}

