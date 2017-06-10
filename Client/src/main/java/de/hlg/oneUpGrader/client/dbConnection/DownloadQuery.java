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
 * Created by Michael on 10.06.2017.
 *
 * Übergabe der PrüfungsID als int
 * Rückgabe des Prüfungsobjekts
 */

public class DownloadQuery extends Task<Prüfung> {

    int PrüfungsID;
    int fachID;
    String fach = "Nicht auflösbar";
    int lehrerID;
    String lehrer = "Nicht auflösbar";
    int jahrgangsstufe;
    Date datum;
    String beschreibung;
    BufferedImage bild;
    Boolean art;

    String query5 = "SELECT * FROM Prüfungen WHERE PrüfungsID = ?";
    String query6 = "SELECT * FROM Fach WHERE FachID = ?";
    String query7 = "SELECT * FROM Lehrer WHERE LehrerID = ?";

    public DownloadQuery(int p)
    {
        PrüfungsID = p;
    }


    protected Prüfung call() throws Exception {
        DbConnection datenbank = DbConnection.getInstance();

        //Prüfung herunterladen
        Optional<PreparedStatement> optStatement5 = datenbank.getPreparedStatement(query5);
        PreparedStatement statement5 = optStatement5.get();

        statement5.setInt(1, PrüfungsID);

        try {
            statement5.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet ergebnis5 = statement5.getResultSet();

        if (ergebnis5.next()) {
            fachID = ergebnis5.getInt("Fach");
            lehrerID = ergebnis5.getInt("Lehrer");
            jahrgangsstufe = ergebnis5.getInt("Jahrgangsstufe");
            datum = ergebnis5.getDate("Datum");
            art = ergebnis5.getBoolean("Art");
            beschreibung = ergebnis5.getString("Beschreibung");
            Blob blob = ergebnis5.getBlob("Bild");
            try {
                bild = ImageIO.read(blob.getBinaryStream());
            } catch (IOException e) {
                e.printStackTrace();
            }


            // FachID durch Namen ersetzen
            Optional<PreparedStatement> optStatement6 = datenbank.getPreparedStatement(query6);
            PreparedStatement statement6 = optStatement6.get();

            statement6.setInt(1, fachID);

            try {
                statement6.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ResultSet ergebnis6 = statement6.getResultSet();
            if (ergebnis6.next()) {
                fach = ergebnis6.getString("Name");
            }

            //LehrerID durch Namen ersetzen
            Optional<PreparedStatement> optStatement7 = datenbank.getPreparedStatement(query7);
            PreparedStatement statement7 = optStatement7.get();

            statement7.setInt(1, lehrerID);

            try {
                statement7.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ResultSet ergebnis7 = statement7.getResultSet();
            if (ergebnis7.next()) {
                lehrer = ergebnis7.getString("Name");
            }

            //Prüfungsobjekt erstellen und zurückgeben

            Prüfung test = new Prüfung(PrüfungsID, fach, lehrer, jahrgangsstufe, datum, art, beschreibung, bild);
            return test;


        }
        return null;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
