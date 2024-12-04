package Repository;

import db.DatabaseConnection;
import models.User;

import java.sql.*;

public class UserRepository {

    public UserRepository() {}

    /** Findet einen User anhand seiner E-Mail.
     *
     * @param email
     * @return {@link User}
     */
    public User findUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getDouble("balance"),
                        resultSet.getTimestamp("created_at"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Es gab ein Fehler beim Finden der E-Mail", e);
        }
        return null;
    }

    // Fügt einen User der Datenbank hinzu.
      //@param email
      //@param password
      //@param balance
      //@param created_at
    public User findUserById(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getDouble("balance"),
                        resultSet.getTimestamp("created_at"));

            }

        } catch (SQLException e) {
            throw new RuntimeException("Es gab ein Fehler beim finden der Id" + e);
        }
        return null;
    }


        /** Fügt einen User der Datenbank hinzu.
     * @param  {@link User}
     * @throws RuntimeException
     */
    public void addUser(String email, String password, double balance, Timestamp created_at) {
        String query = "INSERT INTO users (email, password, balance, created_at) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, password);
            statement.setDouble(3, balance);
            statement.setTimestamp(4, created_at);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Es gab ein Fehler beim Hinzufügen des Users zur Datenbank", e);
        }
    }

    /** Aktualisiert das Guthaben eines Benutzers.
     * @param email
     * @param balance
     * @throws RuntimeException
     */
    public void updateBalance(String email, double balance) {
        String query = "UPDATE USERS SET BALANCE = ? WHERE EMAIL = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, balance);
            statement.setString(2, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Es gab ein Fehler beim Aktualisieren des Guthabens", e);
        }
    }
    public boolean checkIfAccountExists(String email) {
        User user = findUserByEmail(email);
        return user != null;
    }
}
