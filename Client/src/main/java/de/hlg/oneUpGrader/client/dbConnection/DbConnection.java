package de.hlg.oneUpGrader.client.dbConnection;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.Optional;

/**
 * Created by nico on 18.04.17.
 */
public class DbConnection {

    private static DbConnection instance;
    private boolean working = false;


    private Connection connection;
    private String url = "jdbc:mysql://raspberrypi/oneUpGrader" +
            "?verifyServerCertificate=false&useSSL=true&requireSSL=true&useTimezone=false";
    private String user = "javadebuguser";
    private String password = "java";
    private String hlgUrl = "jdbc:mysql://hlg-win.de/11i1617bomi1_1" +
            "?verifyServerCertificate=false&useSSL=true&requireSSL=true&useTimezone=false";
    private String hlgUser = "11i1617bomi1";
    private String hlgPassword = "rqavyu";


    private static int whichServer = 2; // 1 = DNS:raspberrypi, 2 = root@localhost, 3 gregers.ddns.net
                                        // 4 = hlg-win.de, 5 = NAS

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
            switch (whichServer) {
                case 1:
                    connection = DriverManager.getConnection(url, user, password);
                    break;
                case 2:
                    connection = DriverManager.getConnection("jdbc:mysql://localhost/oneUpGrader", "root", "");
                    break;
                case 3:
                    connection = DriverManager.getConnection("jdbc:mysql://gregers.ddns.net:50000/oneUpGrader?verifyServerCertificate=false&useSSL=true&requireSSL=true&useTimezone=false", user, password);
                    break;
                case 4:
                    connection = DriverManager.getConnection(hlgUrl, hlgUser, hlgPassword);
                    break;
                case 5:
                    connection = DriverManager.getConnection("jdbc:mysql://mariusbushido.ddns.net:6033/oneUpGrader?verifyServerCertificate=false&useSSL=true&requireSSL=true&useTimezone=false", "java", "javapassword123");
            }
            working = true;
            System.out.println("Connected...");
        } catch (SQLException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Datenbankverbindung klappt nicht!", ButtonType.OK);
            a.show();
        }


    }

    Optional<ResultSet> executeQuery(String q) {

        if(!working)
            return Optional.empty();

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
        if (!working)
            return Optional.empty();

        try {
            return Optional.of(connection.prepareStatement(q));
        } catch (SQLException e) {
            System.out.println("Creating PreparedStatement out of: " + q + " failed");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
            System.out.println("Datenbankverbindung beendet");
        } catch (SQLException e) {
            System.out.println("Konnte Datenbankverbindung nicht ordnungsgemäß beenden!");
            e.printStackTrace();
        }
    }

}
