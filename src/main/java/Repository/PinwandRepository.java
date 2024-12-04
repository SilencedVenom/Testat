package Repository;

import db.DatabaseConnection;
import models.PinwandBeitrag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PinwandRepository {

    public void addBeitrag(String email, String beitrag, String verfasser) {
        String query = "INSERT INTO pinwand (email, beitrag, timestamp, verfasser) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, beitrag);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setString(4, verfasser);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Es gab ein Fehler beim Hinzufügen des Beitrags zur Pinnwand", e);
        }
    }

    public List<PinwandBeitrag> getBeitraege(String email) {
        String query = "SELECT * FROM pinwand WHERE email = ? ORDER BY timestamp DESC";
        List<PinwandBeitrag> beitraege = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PinwandBeitrag beitrag = new PinwandBeitrag(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("beitrag"),
                        resultSet.getTimestamp("timestamp"),
                        resultSet.getString("verfasser")
                );
                beitraege.add(beitrag);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Es gab ein Fehler beim Abrufen der Beiträge", e);
        }

        return beitraege;
    }


}
