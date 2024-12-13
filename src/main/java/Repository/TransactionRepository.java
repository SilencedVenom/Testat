package Repository;

import db.DatabaseConnection;
import models.Transaction;

import java.sql.*;
import java.util.ArrayList;
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

    public List<Transaction> getTransactionsBySenderId(int senderId) {
        String query = "Select * from transactions where sender_id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, senderId);
            List<Transaction> transactions = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction item = new Transaction(
                            resultSet.getInt("id"),
                            resultSet.getInt("sender_id"),
                            resultSet.getInt("receiver_id"),
                            resultSet.getDouble("amount"),
                            resultSet.getString("description"),
                            resultSet.getDate("created_at")
                    );
                    transactions.add(item);
                }
            }
            return transactions;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);

        }

    }
}
