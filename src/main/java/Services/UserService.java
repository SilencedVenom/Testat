package Services;

import Repository.UserRepository;
import models.User;

import java.sql.Timestamp;

import db.DatabaseConnection;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    private final RegexService regexService;
    private final UserRepository userRepository;

    public UserService(RegexService regexService) {
        this.regexService = regexService;
        this.userRepository = new UserRepository();
    }

    public boolean checkIfAccountExists(String email) {
        User user = userRepository.findUserByEmail(email);
        return user != null;
    }

    /**
     *
     * @param email
     * @param password
     */
    public void registerUser(String email, String password) {
        if (checkIfAccountExists(email)) {
            new RuntimeException("Benutzer existiert bereits.");
        } else {
            userRepository.addUser(email, password, 1000, new Timestamp(System.currentTimeMillis()));
        }
    }


    // Method to login a user
    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (storedPassword.equals(password)) {
                    // Return a User object if login is successful
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getDouble("balance"),
                            resultSet.getTimestamp("created_at")
                    );
                } else {
                    System.out.println("Falsches Passwort.");
                }
            } else {
                System.out.println("Benutzer nicht gefunden.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ein Fehler ist aufgetreten: " + e.getMessage());
        }

        return null;
    }
}
