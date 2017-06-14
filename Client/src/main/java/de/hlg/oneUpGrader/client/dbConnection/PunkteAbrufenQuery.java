package de.hlg.oneUpGrader.client.dbConnection;

import javafx.concurrent.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


/**
 * Created by Michael on 12.05.2017.
 
 Übergabe der Emailadresse
 Rückgabe der Punktezahl als Integer
 */

public class PunkteAbrufenQuery extends Task<Integer> {

    String query1 = "SELECT Punkte FROM Anwender WHERE Email = ?";
    String username;

    public PunkteAbrufenQuery(String benutzer)
    {
        username = benutzer;
    }

    protected Integer call() {
        DbConnection datenbank = DbConnection.getInstance();

                                     //SQL Abfrage zusammenbauen

        Optional<PreparedStatement> optStatement = datenbank.getPreparedStatement(query1.toString());
        PreparedStatement statement;

        if (optStatement.isPresent()) {
            statement = optStatement.get();

        } else {
            System.out.println("Konnte PreparedStatement im PunkteAbrufenQuery nicht erstellen");
            return -1;
        }


        try {
            statement.setString(1, username);
            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }                                                           //Abfrage ausführen oder Fehler

        try {
            ResultSet ergebnis = statement.getResultSet();              //Ergebnis abspeichern

            if(ergebnis.next())
            {
                int punkte = ergebnis.getInt(1);
                return Integer.valueOf(punkte);                         //Ergebnis(Anzahl der Punkte des Users) zurückgeben
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Verarbeiten der Informationen im PunkteAbrufenQuery");
            e.printStackTrace();
            return -1;
        }

        return 0;                                                   //Wenn Fehler
    }

    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

}
