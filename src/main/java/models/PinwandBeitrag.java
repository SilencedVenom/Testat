package models;

import java.sql.Timestamp;

public class PinwandBeitrag {
    private int id;
    private String email;
    private String beitrag;
    private Timestamp timestamp;
    private String verfasser;

    // Constructor
    public PinwandBeitrag(int id, String email, String beitrag, Timestamp timestamp, String verfasser) {
        this.id = id;
        this.email = email;
        this.beitrag = beitrag;
        this.timestamp = timestamp;
        this.verfasser = verfasser;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBeitrag() {
        return beitrag;
    }

    public void setBeitrag(String beitrag) {
        this.beitrag = beitrag;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getVerfasser() {
        return verfasser;
    }

    public void setVerfasser(String verfasser) {
        this.verfasser = verfasser;
    }
}
