package models;

import Repository.UserRepository;

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
    public User login(String email, String password) {
        UserRepository userRepository = new UserRepository();
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            String storedPassword = user.getPassword();
            if (storedPassword.equals(password)) {
                return user;
            } else {
                System.out.println("Falsches Passwort.");
            }
        } else {
            System.out.println("Benutzer nicht gefunden.");
        }
        return null;
    }

    public void showBalance(User user){
        System.out.println("Ihr aktueller Kontostand beträgt: " + user.getBalance());
    }
}

