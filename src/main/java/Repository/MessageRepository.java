package Repository;

import db.DatabaseConnection;
import models.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository {

    // Fügt eine neue Nachricht in die Datenbank ein
    public void addMessage(Message message) {
        String query = "INSERT INTO messages (sender_id, receiver_id, message, is_wall_post, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, message.getSenderId());
            statement.setInt(2, message.getReceiverId());
            statement.setString(3, message.getMessage());
            statement.setBoolean(4, message.isWallPost());
            statement.setTimestamp(5, message.getCreatedAt());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Hinzufügen der Nachricht", e);
        }
    }


    // Gibt alle Nachrichten zurück, die ein Benutzer gesendet hat
    public List<Message> getMessagesBySenderId(int senderId) {
        String query = "SELECT * FROM messages WHERE sender_id = ?";
        return getMessages(query, senderId);
    }

    // Gibt alle Nachrichten zurück, die ein Benutzer empfangen hat
    public List<Message> getMessagesByReceiverId(int receiverId) {
        String query = "SELECT * FROM messages WHERE receiver_id = ?";
        return getMessages(query, receiverId);
    }

    // Hilfsmethode zum Abrufen von Nachrichten
    private List<Message> getMessages(String query, int userId) {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                messages.add(new Message(
                        resultSet.getInt("id"),
                        resultSet.getInt("sender_id"),
                        resultSet.getInt("receiver_id"),
                        resultSet.getString("message"),
                        resultSet.getBoolean("is_wall_post"),
                        resultSet.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen der Nachrichten", e);
        }
        return messages;
    }
    public List<Message> getDirectMessages(int userId, int contactId) {
        String query = "SELECT * FROM messages " +
                "WHERE ((sender_id = ? AND receiver_id = ?) " +
                "   OR (sender_id = ? AND receiver_id = ?)) " +
                "AND is_wall_post = false";

        List<Message> messages = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, contactId);
            statement.setInt(3, contactId);
            statement.setInt(4, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                messages.add(new Message(
                        resultSet.getInt("id"),
                        resultSet.getInt("sender_id"),
                        resultSet.getInt("receiver_id"),
                        resultSet.getString("message"),
                        resultSet.getBoolean("is_wall_post"),
                        resultSet.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen der Direktnachrichten", e);
        }

        return messages;
    }
}
