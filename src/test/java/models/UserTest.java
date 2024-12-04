package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertTrue(user.getPassword().equals(inputPassword), "Login should be successful with correct password");

        inputPassword = "wrongpassword";
        assertFalse(user.getPassword().equals(inputPassword), "Login should fail with incorrect password");
    }
}
