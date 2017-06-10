package de.hlg.oneUpGrader.client.dbConnection;

import javafx.concurrent.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Michael on 12.05.2017.

    Übergabe von Benutername und Passwort im Konstruktur
    Rückgabe, ob Richtig oder Falsch als Boolean

*/
public class AnmeldenQuery extends Task<Boolean> {

    private String query1 = "SELECT * FROM Anwender WHERE Email = ? AND Passwort = ?;";

    private String username;
    private String passwort;

    public AnmeldenQuery(String benutzer, String password)
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
            String userergebnis = ergebnis.getString("Email");
            String passwortergebnis = ergebnis.getString("Passwort");        //Auslesen des Ergebnisses

            if(userergebnis.equals(username) && passwortergebnis.equals(passwort))
            {
                return true;                                                    //Wenn user und passwort richtig -> true
            }

        }
        return false;                                                           // sonst -> false
    }
    
    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
