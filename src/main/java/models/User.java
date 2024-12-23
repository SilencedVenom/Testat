package models;

import Repository.MessageRepository;
import Repository.PinwandRepository;
import Repository.UserRepository;
import java.sql.Timestamp;
import java.util.List;

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

    public User() {

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

    public void showMyMessages(){
        UserRepository userRepository = new UserRepository();
        userRepository.showMyMessages(this.email);
    }
    public void showLastTenTransactions(){
        UserRepository userRepository = new UserRepository();
        userRepository.printLastTenTransactions(this.getId());
    }
    /**
     * Zeigt den Aktuellen Kontostand an
     */
    public void showMyBalance() {
        System.out.println("Ihr aktueller Kontostand: " + this.getBalance());
    }
    /**
     * Returned einen User
     * @param email
     */
    public User findUser(String email) {
        UserRepository userRepository = new UserRepository();

        return userRepository.findUserByEmail(email);
    }
    /**
     * zeigt die Pinwand des Users an
     */
    public void showPinwand() {
        PinwandRepository pinwandRepository = new PinwandRepository();
        List<PinwandBeitrag> beitraege = pinwandRepository.getBeitraege(this.email);

        if (beitraege.isEmpty()) {
            System.out.println("Keine Beiträge auf der Pinnwand.");
        } else {
            System.out.println("Pinnwand von " + this.email + ":");
            for (PinwandBeitrag beitrag : beitraege) {
                System.out.println(beitrag.getTimestamp() + " - " + beitrag.getVerfasser() + ": " + beitrag.getBeitrag());
            }
        }
    }
    /**
     * sendet eine Nachricht
     * @param receiver
     * @param messageText
     */
    public void sendMessage(User receiver, String messageText) {
        if (receiver != null) {
            // Erstellen der Nachricht
            Message message = new Message(
                    0, // ID wird in der Datenbank generiert
                    this.id, // Sender-ID (aktueller Benutzer)
                    receiver.getId(), // Empfänger-ID
                    messageText, // Nachrichtentext
                    false, // Kein Pinnwand-Beitrag
                    new Timestamp(System.currentTimeMillis()) // Aktuelles Datum/Zeit
            );

            // Nachricht in die Datenbank schreiben
            MessageRepository messageRepository = new MessageRepository();
            messageRepository.addMessage(message);

            System.out.println("Nachricht erfolgreich an " + receiver.getEmail() + " gesendet.");
        } else {
            System.out.println("Empfänger ist null. Nachricht konnte nicht gesendet werden.");
        }
    }
}

