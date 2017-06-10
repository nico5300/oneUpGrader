package de.hlg.oneUpGrader.client.dbConnection;

import com.sun.xml.internal.ws.util.ReadAllStream;
import javafx.concurrent.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Michael on 01.06.2017.
 
 Übergabe von Emailadresse und Passwort
 Rückgabe ob erfolgreich registriert oder nicht als Boolean
 */

public class RegistrierenQuery extends Task<Boolean> {

    String query1 = "SELECT * FROM Anwender WHERE Email = ?";
    String query2 = "INSERT INTO Anwender (Email, Passwort) VALUES (?, ?)";
    String email;
    String passwort;

    public RegistrierenQuery(String e, String p)
    {
        email = e;
        passwort = p;
    }

    protected Boolean call() throws SQLException {
        DbConnection datenbank = DbConnection.getInstance();

        Optional<PreparedStatement> opt = datenbank.getPreparedStatement(query1);
        PreparedStatement queryComplete = opt.get();

        queryComplete.setString(1, email);


        try {
            queryComplete.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet ergebnis = queryComplete.getResultSet();          //Abfrage ob User mit dieser Email bereits vorhanden

        if(!ergebnis.next())        //wenn nicht bereits vorhanden
        {
            Optional<PreparedStatement> opt2 = datenbank.getPreparedStatement(query2);
            PreparedStatement queryComplete2 = opt.get();

            queryComplete2.setString(1, email);
            queryComplete2.setString(2, passwort);

            try {
                queryComplete2.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }                                                             //Neuen User in Datenbank eintragen
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
