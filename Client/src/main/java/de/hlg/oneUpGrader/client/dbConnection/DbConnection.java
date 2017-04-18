package de.hlg.oneUpGrader.client.dbConnection;


import java.sql.*;
import java.util.Optional;

/**
 * Created by nico on 18.04.17.
 */
public class DbConnection {

    private static DbConnection instance;

    private Connection connection;
    private String url = "jdbc:mysql://raspberrypi/oneUpGrader" +
            "?verifyServerCertificate=false&useSSL=true&requireSSL=true&useTimezone=false";
    private String user = "javadebuguser";
    private String password = "java";

    public static synchronized DbConnection getInstance() { // Mach DbConnection zu nem Singleton
        if(instance == null) {
            instance = new DbConnection();
        }
        return instance;
    }


    private DbConnection() { // privater Konstruktor wg. Singleton


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver found");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Trying to connect...");

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected...");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    Optional<ResultSet> executeQuery(String q) {

        try {
            Statement statement = connection.createStatement();
            return Optional.of(statement.executeQuery(q));
        } catch (SQLException e) {
            System.out.println("SQL Query: " + q + " failed...");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    Optional<PreparedStatement> getPreparedStatement(String q) {
        try {
            return Optional.of(connection.prepareStatement(q));
        } catch (SQLException e) {
            System.out.println("Creating PreparedStatement out of: " + q + " failed");
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
