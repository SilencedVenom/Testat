package de.hsw.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance = null;
    private Connection connection = null;

    private DatabaseConnection() {}

    private void init() throws SQLException {
        //TODO Die Werte müssen noch angepasst werden
        final String DB_URL = "jdbc:postgresql://localhost:5432/socialmedia_testatd";
        final String USER = "postgres";
        final String PASS = "postgres";

        connection = DriverManager.getConnection(DB_URL, USER, PASS);

        System.out.println("Connection successful.");
    }

    public Connection getConnection() {
        return connection;
    }

    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection() == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
            instance.init();
        }
        return instance;
    }
}

