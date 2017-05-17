package de.hlg.oneUpGrader.client.dbConnection;

import javafx.concurrent.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Michael on 12.05.2017.
 */
public class AnmeldenQuery extends Task<Boolean> {

    private String query1 = "SELECT Email, Passwort FROM Anwender WHERE Email = ? AND Passwort = ?;";
    private String username;
    private String passwort;

    public AnmeldenQuery(String benutzer, String password)
    {
        username = benutzer;
        passwort = password;
    }

    protected Boolean call() throws SQLException {
        DbConnection datenbank = DbConnection.getInstance();

                              //Datenbank Abfrage aus Variablen und SQL zusammenbauen

        Optional<PreparedStatement> optStatement = datenbank.getPreparedStatement(query1);
        if(!optStatement.isPresent()) {
            System.out.println("Konnte das PreparedStatement nicht erzeugen im AnmeldenQuery");
            return false;
        }
        PreparedStatement statement = optStatement.get();
        statement.setString(1, username);
        statement.setString(2, passwort);
        try {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }                                                   //Abfrage ausführen oder Fehler

        ResultSet ergebnis = statement.getResultSet();      //Ergebnistabelle der Abfrage

        if(ergebnis.next())
        {
            String userergebnis = ergebnis.getString("Email");
            String passwortergebnis = ergebnis.getString("Passwort");        //Auslesen des Ergebnisses

            if(userergebnis == username && passwortergebnis == passwort)
            {
                return true;                                                    //Wenn user und passwort richtig -> true
            }

        }
        return false;                                                           // sonst -> false
    }


    public void execute() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
}
