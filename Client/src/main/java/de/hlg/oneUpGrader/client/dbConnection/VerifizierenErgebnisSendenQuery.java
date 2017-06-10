package de.hlg.oneUpGrader.client.dbConnection;

import javafx.concurrent.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Michael on 12.05.2017.

    Übergabe von Username, PrüfungsID und Ergebnis
    Keine Rückgabe

    Methode fügt Eintrag zu Tabelle verifiziert
    Methode überprüft, ob nun bereits dreimal verifiziert
    Falls ja, Methode löscht alles aus verifiziert Tabelle
    UND trägt bei Prüfung ein ob Prüfung passt oder nicht
 */
public class VerifizierenErgebnisSendenQuery extends Task<Void> {

    String query1 = "INSERT INTO Verifiziert (Email, PrüfungsID, Bewertung) VALUES (?, ?, ?)";
    String query2 = "SELECT * FROM Verifiziert WHERE PrüfungsID = ?";
    String query3 = "DELETE FROM Verifiziert WHERE PrüfungsID = ?";
    String query4 = "UPDATE Prüfungen SET Verifiziert = ? WHERE PrüfungsID = ?";

    Boolean erg1 = false;
    Boolean erg2 = false;
    Boolean erg3 = false;

    String email;
    int TestID;
    Boolean ergebnis;

    public VerifizierenErgebnisSendenQuery(String e, int ID, boolean er) {
        email = e;
        TestID = ID;
        ergebnis = er;
    }

    protected Void call() throws SQLException {
        DbConnection datenbank = DbConnection.getInstance();

        Optional<PreparedStatement> opt = datenbank.getPreparedStatement(query1);
        PreparedStatement queryComplete = opt.get();

        queryComplete.setString(1, email);
        queryComplete.setInt(2, TestID);
        queryComplete.setBoolean(3, ergebnis);

        try {
            queryComplete.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Optional<PreparedStatement> opt2 = datenbank.getPreparedStatement(query2);
        PreparedStatement queryComplete2 = opt2.get();

        queryComplete2.setInt(1, TestID);

        try {
            queryComplete2.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet ergebnis2 = queryComplete2.getResultSet();

        if (ergebnis2.next()) {
            erg1 = ergebnis2.getBoolean(1);
            if (ergebnis2.next()) {
                erg2 = ergebnis2.getBoolean(1);
                if (ergebnis2.next()) {
                    erg3 = ergebnis2.getBoolean(1);


                    Optional<PreparedStatement> opt3 = datenbank.getPreparedStatement(query3);
                    PreparedStatement queryComplete3 = opt3.get();

                    queryComplete3.setInt(1, TestID);

                    try {
                        queryComplete3.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Optional<PreparedStatement> opt4 = datenbank.getPreparedStatement(query3);
                    PreparedStatement queryComplete4 = opt4.get();

                    if ((erg1 && erg2) || (erg1 && erg3) || (erg2 && erg3)) {
                        queryComplete4.setBoolean(1, true);
                    } else {
                        queryComplete4.setBoolean(1, false);
                    }
                    queryComplete4.setInt(2, TestID);

                    try {
                        queryComplete4.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }
            }
        }                                                                   // Wenn Prüfung dreimal verifiziert
        return null;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
