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
  Created by Michael on 12.05.2017.

  Übergabe  von Useremail, PrüfungsID
  Kauf der Prüfung im System und Rückgabe der Prüfung
 */
public class KaufenDownloadQuery extends Task<Prüfung> {

    String query1 = "SELECT Punkte FROM Anwender WHERE Email = ?";
    String query2 = "SELECT Art FROM Prüfungen WHERE PrüfungsID = ?";
    String query3 = "UPDATE Anwender SET Punkte = ? WHERE Email = ?";
    String query4 = "INSERT INTO Gekauft (Email, PrüfungsID) VALUES (?, ?)";
    String query5 = "SELECT * FROM Prüfungen WHERE PrüfungsID = ?";
    String query6 = "SELECT * FROM Fach WHERE FachID = ?";
    String query7 = "SELECT * FROM Lehrer WHERE LehrerID = ?";

    String email;
    int Punkte;
    Boolean Art;
    int PrüfungsID;

    int fachID;
    String fach = "Nicht auflösbar";
    int lehrerID;
    String lehrer = "Nicht auflösbar";
    int jahrgangsstufe;
    Date datum;
    String beschreibung;
    BufferedImage bild;


    public KaufenDownloadQuery(String e, int p) {
        email = e;
        PrüfungsID = p;
    }

    @Override
    protected Prüfung call() throws Exception {
        DbConnection datenbank = DbConnection.getInstance();

        //Anzahl der Punkte des Users herausfinden
        Optional<PreparedStatement> optStatement1 = datenbank.getPreparedStatement(query1);
        PreparedStatement statement1 = optStatement1.get();

        statement1.setString(1, email);
        try {
            statement1.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet ergebnis = statement1.getResultSet();

        if(ergebnis.next()) {
            Punkte = ergebnis.getInt("Punkte");
        }

        //Art der Prüfung herausfinden
        Optional<PreparedStatement> optStatement2 = datenbank.getPreparedStatement(query2);
        PreparedStatement statement2 = optStatement2.get();

        statement2.setInt(1,PrüfungsID);
        try {
            statement2.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet ergebnis2 = statement2.getResultSet();
        if(ergebnis2.next())
        {
            Art = ergebnis2.getBoolean("Art");
        }

        //Anwender die Punkte für Kauf abziehen
        Optional<PreparedStatement> optStatement3 = datenbank.getPreparedStatement(query3);
        PreparedStatement statement3 = optStatement3.get();
        int w;
        if(Art)
        {
            w = Punkte - 5;
        }
        else
        {
            w = Punkte - 3;
        }
        statement3.setInt(1,w);
        statement3.setString(2, email);

        try {
            statement3.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Prüfung dem User als gekauft zuordnen
        Optional<PreparedStatement> optStatement4 = datenbank.getPreparedStatement(query4);
        PreparedStatement statement4 = optStatement4.get();

        statement4.setString(1, email);
        statement4.setInt(2, PrüfungsID);

        try {
            statement4.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        if(ergebnis5.next())
        {
            fachID = ergebnis5.getInt("Fach");
            lehrerID = ergebnis5.getInt("Lehrer");
            jahrgangsstufe = ergebnis5.getInt("Jahrgangsstufe");
            datum = ergebnis5.getDate("Datum");
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
            if(ergebnis6.next())
            {
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
            if(ergebnis7.next())
            {
                lehrer = ergebnis7.getString("Name");
            }

            //Prüfungsobjekt erstellen und zurückgeben

            Prüfung test = new Prüfung(PrüfungsID, fach, lehrer, jahrgangsstufe, datum, Art, beschreibung, bild);
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
