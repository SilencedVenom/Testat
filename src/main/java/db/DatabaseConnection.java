package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance = null;
    private Connection connection = null;

    private DatabaseConnection() {
    }

    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection() == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
            instance.init();
        }
        return instance;
    }

    private void init() throws SQLException {
        //TODO Die Werte müssen noch angepasst werden
        final String DB_URL = "jdbc:postgresql://localhost:5432/socialmedia_testat";
        final String USER = "postgres";
        final String PASS = "postgres";

        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        
    }

    public Connection getConnection() {
        return connection;
    }
}

