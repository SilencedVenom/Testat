package Repository;

import db.DatabaseConnection;
import models.Transaction;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TransactionRepository {

    public TransactionRepository() {
    }

    public void sendTransaction(List<Transaction> transactions) {
        String query = "Insert INTO transactions (sender_id, receiver_id, amount, description, created_at) values(?,?,?,?,?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (Transaction transaction : transactions) {
                statement.setInt(1, transaction.getSenderId());
                statement.setInt(2, transaction.getReceiverId());
                statement.setDouble(3, transaction.getAmount());
                statement.setString(4, transaction.getDescription());
                statement.setDate(5, new Date(System.currentTimeMillis()));
                statement.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
