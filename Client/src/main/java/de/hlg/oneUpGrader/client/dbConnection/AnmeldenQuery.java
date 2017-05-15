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

    private String query1 = "SELECT * FROM Anwender WHERE Email = '";
    private String query2 = "' AND Passwort = '";
    private String query3 = "';";
    private String username;
    private String passwort;

    public AnmeldenQuery(String benutzer, String password)
    {
        username = benutzer;
        passwort = password;
    }

    protected Boolean call() throws SQLException {
        DbConnection datenbank = DbConnection.getInstance();

        StringBuilder queryComplete = new StringBuilder();
        queryComplete.append(query1);
        queryComplete.append(username);
        queryComplete.append(query2);
        queryComplete.append(passwort);
        queryComplete.append(query3);                       //Datenbank Abfrage aus Variablen und SQL zusammenbauen

        Optional<PreparedStatement> optStatement = datenbank.getPreparedStatement(queryComplete.toString());
        PreparedStatement statement = optStatement.get();
        try {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }                                                   //Abfrage ausführen oder Fehler

        ResultSet ergebnis = statement.getResultSet();      //Ergebnistabelle der Abfrage

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
