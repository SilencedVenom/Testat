package Repository;

import db.DatabaseConnection;
import models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TransactionRepository {

    public TransactionRepository() {
    }

    public void sendTransaction(List<Transaction> transactions) {
        String query = "Insert INTO transactions values(?,?,?,?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (Transaction transaction : transactions) {
                statement.setString();
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
