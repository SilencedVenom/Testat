package models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.hsw.Main;
import Repository.UserRepository;
import db.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private UserRepository userRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException{
        connection = DatabaseConnection.getInstance().getConnection();
        connection.setAutoCommit(false); //
        user = new User(1, "testuser@uni.de", "password123", 1000.00, new Timestamp(System.currentTimeMillis()));
        userRepository = new UserRepository();
    }
    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.rollback(); // Roll back any changes made during the test
            connection.setAutoCommit(true); // Restore auto-commit mode
        }
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

    @Test
    void testRegisterNewUser() {
        // Register a new user
        boolean result = Main.register("newuser@uni.de", "Password123");
        assertTrue(result, "Registration should be successful");

        // Verify that the user is in the repository
        User user = userRepository.findUserByEmail("newuser@uni.de");
        assertNotNull(user, "User should be found after successful registration");
        assertEquals("newuser@uni.de", user.getEmail(), "Email should match the registered email");
    }

    @Test
    void testRegisterExistingUser() {
        // Register a user for the first time
        Main.register("existinguser@uni.de", "Password123");

        // Try to register the same user again
        boolean result = Main.register("existinguser@uni.de", "Password123");
        assertFalse(result, "Registration should fail for an existing user");
    }

    @Test
    void testRegisterInvalidEmail() {
        // Attempt to register with an invalid email
        boolean result = Main.register("invalid-email", "Password123");
        assertFalse(result, "Registration should fail for an invalid email");
    }

    @Test
    void testRegisterInvalidPassword() {
        // Attempt to register with an invalid password
        boolean result = Main.register("validemail@uni.de", "pass");
        assertFalse(result, "Registration should fail for an invalid password");
    }
}
