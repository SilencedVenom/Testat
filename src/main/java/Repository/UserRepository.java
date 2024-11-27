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
            throw new RuntimeException("Es gab ein Fehler beim finden der Email");
        }
        return null;
    }

    /** Fügt einen User der Datenbank hinzu.
     * @param user {@link User}
     * @throws RuntimeException
     * @throws SQLException
     */
    public void addUser(User user) {
        String query = "INSERT INTO users (email, password, balance, created_at) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setDouble(3, user.getBalance());
            statement.setTimestamp(4, new Timestamp(user.getCreatedAt().getTime()));
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Es gab ein Fehler beim hinzufügen des Users zur Datenbank");
        }
    }
}
