package models;

import Repository.PinwandRepository;
import Repository.UserRepository;

import java.sql.Timestamp;
import java.util.List;

// User class representing the users table
public class User {
    private final int id;
    private final String email;
    private final String password;
    private final Timestamp createdAt;
    private double balance;

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

    public void showBalance(User user) {
        System.out.println("Ihr aktueller Kontostand: " + user.getBalance());
    }

    public void showMyPinboard() {
        PinwandRepository pinwandRepository = new PinwandRepository();
        List<PinwandBeitrag> list = pinwandRepository.getBeitraege(this.email);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("\n" + "###\nVerfasser: " + list.get(i).getVerfasser() + "\n" + list.get(i).getBeitrag() + "\n" + list.get(i).getTimestamp() + "\n###");
        }
    }
}

