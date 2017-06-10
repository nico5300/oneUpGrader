package de.hlg.oneUpGrader.client.dbConnection;

import javafx.concurrent.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Michael on 01.06.2017.
 * 
 * Übergabe von Useremail und Passwort
 * Rückgabe ob Registrierung erfolgreich
 */

public class RegistrierenQuery extends Task<Boolean> {

    private String query1 = "SELECT * FROM Anwender WHERE Email = ?;";
    private String query2 = "INSERT INTO Anwender (Email, Passwort) VALUES (?, ?);";
    private String email;
    private String passwort;

    public RegistrierenQuery(String e, String p)
    {
        email = e;
        passwort = p;
    }

    protected Boolean call()  {
        DbConnection datenbank = DbConnection.getInstance();

        Optional<PreparedStatement> opt = datenbank.getPreparedStatement(query1);

        PreparedStatement queryComplete;
        if (opt.isPresent()) {
            queryComplete = opt.get();
        } else {
            System.out.println("Konnte das Prepared Statement nicht erzeugen!");
            return Boolean.FALSE;
        }


        try {
            queryComplete.setString(1, email);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            queryComplete.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet ergebnis = queryComplete.getResultSet();          //Abfrage ob User mit dieser Email bereits vorhanden

            if(!ergebnis.next())        //wenn nicht bereits vorhanden
            {
                Optional<PreparedStatement> opt2 = datenbank.getPreparedStatement(query2);
                PreparedStatement queryComplete2 = opt2.get();

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
        } catch (SQLException e) {
            System.out.println("Fehler beim Auswerten und Ausführen von Abfrage 1 & 2");
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
