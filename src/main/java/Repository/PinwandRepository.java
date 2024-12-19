package Repository;

import db.DatabaseConnection;
import models.PinwandBeitrag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PinwandRepository {

    /**
     * Fügt einen Beitrag der Pinnwand hinzu.
     *
     * @param email     vom Verfasser
     * @param beitrag   Nachricht des Beitrags
     * @param verfasser Verfasser des Beitrags
     * @throws RuntimeException bei DB Fehlern
     */
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

    /**
     * Läd alle Beiträge vom User.
     *
     * @param email Email vom User
     * @return List<PinwandBeitrag> enthält alle Beiträge vom User
     * @throws RuntimeException bei DB Fehlern
     */
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
    /**
     * Holt alle Beiträge, die der aktuelle Benutzer auf die Pinnwand eines Kontakts geschrieben hat.
     * @param contactEmail Die E-Mail-Adresse des Kontakts
     * @param verfasserEmail Die E-Mail-Adresse des aktuellen Benutzers (Verfasser)
     * @return Liste der Pinnwandbeiträge
     */
    public List<PinwandBeitrag> getBeitraegeByVerfasser(String contactEmail, String verfasserEmail) {
        String query = "SELECT * FROM pinwand WHERE email = ? AND verfasser = ? ORDER BY timestamp DESC";
        List<PinwandBeitrag> beitraege = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, contactEmail);
            statement.setString(2, verfasserEmail);
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
            throw new RuntimeException("Fehler beim Abrufen der Pinnwandbeiträge des Verfassers", e);
        }

        return beitraege;
    }

}
