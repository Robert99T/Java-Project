package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/tema1tp";
    private static final String USER = "root";
    private static final String PASSWORD = "erublut";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conectare reusita la baza de date!");
            } catch (SQLException e) {
                System.err.println("Eroare la conectarea la baza de date: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Conexiunea la baza de date a fost inchisa.");
            }
        } catch (SQLException e) {
            System.err.println("Eroare la inchiderea conexiunii: " + e.getMessage());
        }
    }
}