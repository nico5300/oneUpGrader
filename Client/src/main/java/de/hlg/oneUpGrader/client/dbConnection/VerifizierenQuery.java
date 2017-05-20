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
 */

public class VerifizierenQuery extends Task<Prüfung> {

    String query1 = "SELECT PrüfungsID FROM Verifiziert ORDER BY VerifiziertID;"; //Herausfinden der zu verifizierenden Prüfung
    String query2 = "SELECT * FROM Prüfungen WHERE PrüfungsID = ?;";
    String query3 = "SELECT Name FROM Fach WHERE FachID = ?";
    String query4 = "SELECT Nachname, Vorname FROM Lehrer WHERE LehrerID = ?";

    Prüfung test;

    @Override
    protected Prüfung call() throws SQLException, IOException {
        DbConnection datenbank = DbConnection.getInstance();

        Optional<PreparedStatement> opt = datenbank.getPreparedStatement(query1);
        PreparedStatement prep1 = opt.get();

        try {
            prep1.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet ergebnis = prep1.getResultSet();

        if(ergebnis.next())
        {
            String PrüfungsID = ergebnis.getString(0);

            Optional<PreparedStatement> opt2 = datenbank.getPreparedStatement(query2);
            PreparedStatement prep2 = opt2.get();

            prep2.setString(1, PrüfungsID);

            try {
                prep2.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ResultSet ergebnis2 = prep2.getResultSet();
            BufferedImage bild = null;
            if(ergebnis2.next())
            {
                Blob blob = ergebnis2.getBlob(8);
                try {
                    bild = ImageIO.read(blob.getBinaryStream());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                int fachID = ergebnis2.getInt(2);
                int lehrerID = ergebnis2.getInt(3);
                int jahrgangsstufe = ergebnis2.getInt(4);
                Date datum = ergebnis2.getDate(5);
                boolean art = ergebnis2.getBoolean(6);
                String beschreibung = ergebnis2.getString(7);


                Optional<PreparedStatement> opt3 = datenbank.getPreparedStatement(query3);
                PreparedStatement prep3 = opt3.get();

                prep3.setString(1, String.valueOf(fachID));

                try {
                    prep3.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                ResultSet ergebnis3 = prep3.getResultSet();

                String fach = "Nicht auflösbar!";
                if(ergebnis3.next())
                {
                    fach = ergebnis3.getString(1);
                }

                Optional<PreparedStatement> opt4 = datenbank.getPreparedStatement(query4);
                PreparedStatement prep4 = opt4.get();

                prep3.setString(1, String.valueOf(lehrerID));

                ResultSet ergebnis4 = prep4.getResultSet();

                String lehrer;
                String lehrerVorname = "Nicht ";
                String lehrerNachname = "auflösbar!";
                if(ergebnis4.next())
                {
                    lehrerNachname = ergebnis4.getString(1);
                    lehrerVorname = ergebnis4.getString(2);
                }
                lehrer = lehrerVorname + lehrerNachname;
                test = new Prüfung(fach, lehrer, jahrgangsstufe, datum, art, beschreibung, bild);
                return test;
            }
        }


        return null;
    }
}