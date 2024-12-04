package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "testuser@uni.de", "password123", 1000.00, new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void getId() {
        assertEquals(1, user.getId(), "User ID should be 1");
    }

    @Test
    void getEmail() {
        assertEquals("testuser@uni.de", user.getEmail(), "User email should match");
    }

    @Test
    void getPassword() {
        assertEquals("password123", user.getPassword(), "User password should match");
    }

    @Test
    void getBalance() {
        assertEquals(1000.00, user.getBalance(), "User balance should be 1000.00");
    }

    @Test
    void setBalance() {
        user.setBalance(1500.00);
        assertEquals(1500.00, user.getBalance(), "User balance should be updated to 1500.00");
    }

    @Test
    void getCreatedAt() {
        assertNotNull(user.getCreatedAt(), "User creation timestamp should not be null");
    }

    @Test
    void login() {
        String inputPassword = "password123";
        assertEquals(user.getPassword(), inputPassword, "Login should be successful with correct password");

        inputPassword = "wrongpassword";
        assertNotEquals(user.getPassword(), inputPassword, "Login should fail with incorrect password");
    }

    @Test
    void testShowBalance() {
        //Beispieluser erstellen
        User user = new User(1, "testuser@uni.de", "password123", 1000.00, new Timestamp(System.currentTimeMillis()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        user.showBalance(user);

        // Assert
        String expectedOutput = "Ihr aktueller Kontostand: 1000.0" + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString(), "Die Ausgabe des Kontostands sollte korrekt sein");
    }


//    @Test
//    void testShowMyPinboard() throws SQLException {
//
//
//        // Testdaten einfügen
//        statement.execute("INSERT INTO pinwand (id, email, beitrag, timestamp, verfasser) " +
//                "VALUES (10, 'testuser@uni.de', 'Hallo Welt!', CURRENT_TIMESTAMP, 'testuser@uni.de')");
//
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outputStream));
//
//        User user = new User(1, "testuser@uni.de", "password123", 1000.00, new Timestamp(System.currentTimeMillis()));
//        user.showMyPinboard();
//
//        String actualOutput = outputStream.toString();
//        assertTrue(actualOutput.contains("Hallo Welt!"), "Die Pinwand sollte den Beitrag 'Hallo Welt!' enthalten");
//        assertTrue(actualOutput.contains("testuser@uni.de"), "Die Pinwand sollte den Verfasser 'testuser@uni.de' enthalten");
//    }


}
