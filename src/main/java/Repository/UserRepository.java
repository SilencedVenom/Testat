package Repository;

import db.DatabaseConnection;
import models.User;

import java.sql.*;

public class UserRepository {

    public UserRepository() {}

    /** Findet einen User anhand seiner E-Mail.
     *
     * @param email Email des Users
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

    /**
     * Läd einen User anhand seiner ID.
     *
     * @param id UserID
     * @return {@link User}
     * @throws RuntimeException bei DB Fehlern
     */
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

    /**
     * Fügt einen User der DB hinzu.
     *
     * @param email      Email
     * @param password   Passwort
     * @param balance    Geldbetrag
     * @param created_at Zeitpunkt
     * @throws RuntimeException bei DB Fehlern
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

    /**
     * Aktualisiert das Guthaben eines Users.
     *
     * @param email   Email
     * @param balance Geldbetrag
     * @throws RuntimeException bei DB Fehlern
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

    /**
     * Löscht einen User anhand der Email.
     *
     * @param email Email
     */
    public void deleteUserByEmail(String email) {
        String query = "DELETE FROM users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Überprüft, ob der User existiert.
     *
     * @param email Email
     * @return {@code true} wenn der User existiert
     */
    public boolean checkIfAccountExists(String email) {
        User user = findUserByEmail(email);
        return user != null;
    }

    /**
     * Zeigt die Nachricht anhand der Nachricht an.
     *
     * @param emailMessage Nachricht
     */
    public void showMyMessages(String emailMessage) {
        String queryMessage = "SELECT email_verfasser, nachricht FROM direktnachrichten WHERE ? IN(email_erhalter, email_verfasser) ORDER BY timestamp DESC";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(queryMessage)) {


            statement.setString(1, emailMessage);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String sender = resultSet.getString("email_verfasser");
                    String message = resultSet.getString("nachricht");
                    System.out.println("Von: " + sender + " | Nachricht: " + message);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen der Nachrichten", e);
        }

    }

    public void printLastTenTransactions(int senderId) {
        String query = "SELECT amount, created_at, description FROM transactions WHERE sender_id = ? ORDER BY created_at DESC LIMIT 10";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, senderId);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Letzte 10 Transaktionen:");

            while (resultSet.next()) {
                double amount = resultSet.getDouble("amount");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                String description = resultSet.getString("description");

                System.out.printf("Betrag: %.2f, Datum: %s, Beschreibung: %s%n",
                        amount, createdAt, description);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen der Transaktionen: " + e.getMessage(), e);
        }
    }

}
