package de.hlg.oneUpGrader.client.dbConnection;

import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

/**
 * Created by Michael on 08.05.17.

 Aufrufen dieser Klasse bei Klick auf Prüfung Verifizieren Button
 Rückgabe eines Objekts vom Typ Prüfung 
 */

public class VerifizierenQuery extends Task<Optional<Prüfung>> {

    String query1 = "SELECT PrüfungsID FROM Verifiziert ORDER BY VerifiziertID WHERE NOT Email = ?;"; //Herausfinden der zu verifizierenden Prüfung
    String query2 = "SELECT * FROM Prüfungen WHERE PrüfungsID = ?;";
    String query3 = "SELECT Name FROM Fach WHERE FachID = ?";
    String query4 = "SELECT * FROM Lehrer WHERE LehrerID = ?";
    String query5 = "SELECT * FROM Prüfungen WHERE Verifiziert = false AND NOT AutorID = ? ORDER BY PrüfungsID";

    Prüfung test;
    int PrüfungsID;
    String email;
 
    public void VerifizierenQuery(String e)
    {
     email = e;
    }
     
    @Override
    protected Optional<Prüfung> call() {
        DbConnection datenbank = DbConnection.getInstance();


        //----------------------------------------------
        // Richtige Prüfung herausfinden
        Optional<PreparedStatement> opt = datenbank.getPreparedStatement(query1);

        PreparedStatement prep1;
        if (opt.isPresent()) {
            prep1 = opt.get();
        } else {
            System.out.println("Konnte erstes PreparedStatement nicht erzeugen");
            return Optional.empty();
        }
     
        prep1.setString(email);

        try {
            prep1.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }


        try {
            ResultSet ergebnis = prep1.getResultSet();                  //Überprüfen ob bereits teilweise
            //verifizierte Prüfung existiert
            if(ergebnis.next()) {
                PrüfungsID = ergebnis.getInt("PrüfungsID");
            }
            else {
                Optional<PreparedStatement> opt5 = datenbank.getPreparedStatement(query5);
                PreparedStatement prep5 = opt5.get();
                
                prep5.setString(email);
                try {
                    prep5.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
             
                ResultSet ergebnis5 = prep5.getResultSet();
                if(ergebnis5.next()) {
                    PrüfungsID = ergebnis5.getInt("PrüfungsID");
                }                                                       //Ansonsten überprüfen ob noch gar nicht
                                                                        //verifizierte Prüfungen existieren
                else {
                    return Optional.empty();
                }                                                       //keine zu verifizierende Prüfung

            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Verarbeiten von Abfrage 1");
            e.printStackTrace();
        }

        //-----------------------------------------
        // Prüfung herunterladen und abspeichern
            Optional<PreparedStatement> opt2 = datenbank.getPreparedStatement(query2);

        PreparedStatement prep2;
        if (opt2.isPresent()) {
            prep2 = opt2.get();
        } else {
            System.out.println("Konnte zweites PreparedStatement nicht erzeugen");
            return Optional.empty();
        }
        try {
            prep2.setInt(1, PrüfungsID);


            prep2.execute();
        } catch (SQLException e) {
            System.out.println("Fehler beim Ausführen von Abfrage 2");
            e.printStackTrace();
        }


        try {
            ResultSet ergebnis2 = prep2.getResultSet();                     //Prüfung herunterladen
            BufferedImage bild = null;
            if(ergebnis2.next())
            {
                Blob blob = ergebnis2.getBlob(8);
                try {
                    bild = ImageIO.read(blob.getBinaryStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return Optional.empty();
                }

                int fachID = ergebnis2.getInt(2);
                int lehrerID = ergebnis2.getInt(3);
                int jahrgangsstufe = ergebnis2.getInt(4);
                Date datum = ergebnis2.getDate(5);
                boolean art = ergebnis2.getBoolean(6);
                String beschreibung = ergebnis2.getString(7);               //Prüfung in Variablen speichern



                //---------------------------------------------
                //FachID durch Fachnamen ersetzen

                Optional<PreparedStatement> opt3 = datenbank.getPreparedStatement(query3);

                PreparedStatement prep3;
                if (opt3.isPresent()) {
                    prep3 = opt3.get();
                } else {
                    System.out.println("Konnte drittes PreparedStatement nicht erzeugen");
                    return Optional.empty();
                }

                prep3.setString(1, String.valueOf(fachID));


                    prep3.execute();


                ResultSet ergebnis3 = prep3.getResultSet();

                String fach = "Nicht auflösbar!";
                if(ergebnis3.next())
                {
                    fach = ergebnis3.getString(1);
                }

                //-------------------------------------------
                // LehrerID durch Lehrernamen ersetzen
                Optional<PreparedStatement> opt4 = datenbank.getPreparedStatement(query4);

                PreparedStatement prep4;
                if (opt4.isPresent()) {
                    prep4 = opt4.get();
                } else {
                    System.out.println("Konnte viertes PreparedStatement nicht erzeugen");
                    return Optional.empty();
                }

                prep4.setString(1, String.valueOf(lehrerID));

                prep4.execute();

                ResultSet ergebnis4 = prep4.getResultSet();

                String lehrer = "Nicht auflösbar";

                if(ergebnis4.next())
                {
                    lehrer = ergebnis4.getString("Name");

                }

                //Prüfungsobjekt erzeugen
                test = new Prüfung(PrüfungsID, fach, lehrer, jahrgangsstufe, datum, art, beschreibung, bild);
                //Prüfung zurückgeben
                return Optional.of(test);
            }
        } catch (SQLException e) {
            System.out.println("Allgemeiner Fehler bei Abfragen 2 bis 4... Siehe Stacktrace und Zeilenangabe!");
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.empty();
    }



    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
