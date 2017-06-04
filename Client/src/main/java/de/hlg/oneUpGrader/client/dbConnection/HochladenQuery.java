package de.hlg.oneUpGrader.client.dbConnection;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.imageio.ImageIO;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;


/**
 * Created by nico on 01.06.17.
 */

/**
 * @author nico
 *
 * Diese Klasse kümmert sich um das Hochladen eines Prüfungs-Objekts in die Datenbank, dazu übergibt man im
 * Konstruktor eine Instanz der Klasse Prüfung, die die nötigen Informationen kapselt.
 *
 *
 */
public class HochladenQuery extends Task<Boolean> {

    private Prüfung prüfung;

    private String insertStatement = "INSERT INTO Prüfungen (Fach, Lehrer, Jahrgangsstufe, Datum, Art, Beschreibung, Bild)" +
                                        "VALUES(?, ?, ?, ?, ?, ?, ?);";


    /**
     *
     * @param prüfung Die hochzuladende Prüfung
     */
    public HochladenQuery(Prüfung prüfung) {
        this.prüfung = prüfung;
    }


    /**
     *
     * @return Gibt an, ob das Hochladen der Prüfung erfolgreich war
     *
     * In der call()-Methode steht der Code, welcher durch den Thread ausgeführt werden soll:
     *
     * Zuerst werden die Integer-Werte der Primärschlüssel des Lehrers und des Faches der Prüfung von der
     * Datenbank abgefragt.
     *
     */
    @Override
    protected Boolean call() {
        DbConnection conn = DbConnection.getInstance();
        int fachIndex;
        int lehrerIndex;

        //////////////////////////////
        //  ERMITTELN DER INDIZES   //
        //////////////////////////////

        Optional<PreparedStatement> optFächerListQuery = conn.getPreparedStatement("SELECT * FROM Fach WHERE Name = ?;"); // Abfrage, um Fachindex zu ermitteln
        Optional<PreparedStatement> optLehrerListQuery = conn.getPreparedStatement("SELECT * FROM Lehrer WHERE Name = ?;"); // Abfrage, um Lehrerindex zu ermitteln

        if(!optFächerListQuery.isPresent() || !optLehrerListQuery.isPresent()) {
            System.out.println("Konnte Insertion nicht durchführen, weil die Erstellung der Abfragen nicht funktioniert hat.");
            return Boolean.FALSE; // Es hat nicht geklappt!
        }

        PreparedStatement fächerListQuery = optFächerListQuery.get();
        PreparedStatement lehrerListQuery = optLehrerListQuery.get();
        ResultSet fächerList;
        ResultSet lehrerList;


        try {
            // Ausführen der Abfragen
            fächerListQuery.setString(1, prüfung.getFach());
            lehrerListQuery.setString(1, prüfung.getLehrer());
            fächerList = fächerListQuery.executeQuery();
            lehrerList = lehrerListQuery.executeQuery();

            ////// ÜBERPRÜFEN DER ERGEBNISSE //////

            // Auf Doppelte Einträge....
            if(fächerList.getRow() > 1) {
                System.out.println("Offensichtlich gibt es mehrere Fächer mit dem selben Namen... Das darf " +
                        "eigentlich nicht sein. Wir haben also einen Fehler in der Datenbank!");
                return Boolean.FALSE;
            }

            if(lehrerList.getRow() > 1) {
                System.out.println("Offensichtlich gibt es mehrere Lehrer mit dem selben Namen... Das darf " +
                        "eigentlich nicht sein. Wir haben also einen Fehler in der Datenbank!");
                return Boolean.FALSE;
            }


            // Auf leere Resultate
            fächerList.last();
            if(fächerList.getRow() == 0) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Das angegebene Fach existiert nicht!\n" +
                            "Bitte überprüfe deine Eingabe.", ButtonType.OK);
                    alert.show();
                });
                return Boolean.FALSE;
            }

            lehrerList.last();
            if(lehrerList.getRow() == 0) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Der angegebene Lehrer existiert nicht!\n" +
                            "Bitte überprüfe deine Eingabe.", ButtonType.OK);
                    alert.show();
                });
                return Boolean.FALSE;
            }

            // Cursor wieder zurücksetzen (wurde zum Testen der Länge auf den letzten Eintrag gesetzt)
            fächerList.beforeFirst();
            lehrerList.beforeFirst();

            // kann man getrost unchecked lassen, weil wir davor sichergestellt haben, dass nur 1 Ergebnis vorhanden ist
            fächerList.next();
            lehrerList.next();


            fachIndex = fächerList.getInt("FachID");
            lehrerIndex = lehrerList.getInt("LehrerID");


        } catch (SQLException e) {
            System.out.println("Konnte Abfragen nach den Indizes von Lehrer und Fach nicht ausführen... Siehe Stacktrace");
            e.printStackTrace();
            return Boolean.FALSE;
        }


        //////////////////////////////////
        // VORBEREITUNGEN ZUM HOCHLADEN //
        //////////////////////////////////

        // Jahrgangsstufe zwischen 5 und 12?
        if(!(prüfung.getJahrgangsstufe() > 4 && prüfung.getJahrgangsstufe()< 13)) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Die eingegebene Jahrgangsstufe muss zwischen 5 und 12 (eingeschlossen) liegen!\n" +
                        "Bitte überprüfe deine Eingabe.", ButtonType.OK);
                alert.show();
            });
            return Boolean.FALSE;
        }

        /////////////////////////////
        // ERSTELLEN DER INSERTION //
        /////////////////////////////

        Optional<PreparedStatement> optInsertStatement = conn.getPreparedStatement(insertStatement);
        if(!optInsertStatement.isPresent()) {
            System.out.println("Konnte das Insertion Statement nicht erstellen!");
            return Boolean.FALSE;
        }

        PreparedStatement insertStatement = optInsertStatement.get();

        try {

            insertStatement.setInt(1, fachIndex);
            insertStatement.setInt(2, lehrerIndex);
            insertStatement.setInt(3, prüfung.getJahrgangsstufe());
            // Das sind die Momente, in denen ich Java hasse.... Datum und Zeit ist DIE HÖLLE
            insertStatement.setDate(4, java.sql.Date.valueOf(prüfung.getDatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            insertStatement.setBoolean(5, prüfung.getArt());
            insertStatement.setString(6, prüfung.getBeschreibung());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {

                ImageIO.write(prüfung.getBild(), "png", baos);
                InputStream is = new ByteArrayInputStream(baos.toByteArray());
                insertStatement.setBlob(7, is);

            } catch (IOException e) {
                System.out.println("Fehler beim Konvertieren der Bilddatei vor dem Hochladen");
                e.printStackTrace();
            }

            //////////////////////////////////
            // DIE AUSFÜHRUNG DER INSERTION //
            //////////////////////////////////

            int updateCount = insertStatement.executeUpdate();

            if(updateCount != 1) {
                System.out.println("Die Insertion selbst hat nicht geklappt... ");
                return Boolean.FALSE;
            }

        } catch (SQLException e) {
            System.out.println("Konnte Insertion nicht ausführen... Siehe Stacktrace");
            e.printStackTrace();
            return Boolean.FALSE;
        }


        return Boolean.TRUE; // Alles hat geklappt... Rückmeldung!
    }

    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }


    public static void main(String[] args) throws Exception{

        DbConnection conn = DbConnection.getInstance();
        System.out.println(new HochladenQuery(new Prüfung("Informatik", "Hans Vader", 11, Date.from(Instant.now()),
                false, "bla", ImageIO.read(new File("/home/nico/Bilder/Screenshot.png")))).call());

    }
}
