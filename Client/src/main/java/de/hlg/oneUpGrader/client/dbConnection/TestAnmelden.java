package de.hlg.oneUpGrader.client.dbConnection;

import javafx.concurrent.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Michael on 12.05.2017.
 */
public class TestAnmelden extends Task<Boolean> {

    private String query1 = "SELECT * FROM Anwender WHERE Email = ? AND Passwort = ?;";

    private String username;
    private String passwort;

    public TestAnmelden(String benutzer, String password)
    {
        username = benutzer;
        passwort = password;
    }

    protected Boolean call() throws SQLException {
        DbConnection datenbank = DbConnection.getInstance();
        
        Optional<PreparedStatement> opt = datenbank.getPreparedStatement(query1);
        PreparedStatement queryComplete = opt.get();
                
        queryComplete.setString(1, username);
        queryComplete.setString(2, passwort);
        

        try {
            queryComplete.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }                                                   //Abfrage ausführen oder Fehler

        ResultSet ergebnis = queryComplete.getResultSet();      //Ergebnistabelle der Abfrage

        if(ergebnis.next())
        {
            String userergebnis = ergebnis.getString(0);
            String passwortergebnis = ergebnis.getString(1);        //Auslesen des Ergebnisses

            if(userergebnis == username && passwortergebnis == passwort)
            {
                return true;                                                    //Wenn user und passwort richtig -> true
            }

        }
        return false;                                                           // sonst -> false
    }
}