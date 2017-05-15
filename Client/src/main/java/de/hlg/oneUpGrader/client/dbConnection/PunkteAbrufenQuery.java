package de.hlg.oneUpGrader.client.dbConnection;

import javafx.concurrent.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Michael on 12.05.2017.
 */
public class PunkteAbrufenQuery extends Task<Integer> {

    String query1 = "SELECT Punkte FROM Anwender WHERE Email = '";
    String query2 = "';";
    String username;

    public PunkteAbrufenQuery(String benutzer)
    {
        username = benutzer;
    }

    protected Integer call() throws SQLException {
        DbConnection datenbank = DbConnection.getInstance();

        StringBuilder queryComplete = new StringBuilder();
        queryComplete.append(query1);
        queryComplete.append(username);
        queryComplete.append(query2);                               //SQL Abfrage zusammenbauen

        Optional<PreparedStatement> optStatement = datenbank.getPreparedStatement(queryComplete.toString());
        PreparedStatement statement = optStatement.get();

        try {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }                                                           //Abfrage ausführen oder Fehler

        ResultSet ergebnis = statement.getResultSet();              //Ergebnis abspeichern

        if(ergebnis.next())
        {
            int punkte = ergebnis.getInt(0);
            return Integer.valueOf(punkte);                         //Ergebnis(Anzahl der Punkte des Users) zurückgeben
        }

        return 0;                                                   //Wenn Fehler -> immer 0
    }



}
