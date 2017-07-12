package de.hlg.oneUpGrader.client.dbConnection;

import com.airhacks.afterburner.views.FXMLView;
import de.hlg.oneUpGrader.client.UpdateHandler;
import de.hlg.oneUpGrader.client.ui.controller.AbrufenEntryController;
import de.hlg.oneUpGrader.client.ui.view.AbrufenEntryView;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by nico on 19.04.17.
 */
public class AbrufenQuery extends Task<Void>{



    private String lehrer;
    private String fach;
    private String currentUser;
    private int jahrgang;
    private List<UpdateHandler<FXMLView>> observerList; // Hier werden die angemeldeten Observer gespeichert


    // Der SELECT Teil der Abfrage
    private String queryPart1Long = "SELECT Prüfungen.PrüfungsID, Fach.Name, Lehrer.Name, " +
                                    "Prüfungen.Jahrgangsstufe, Prüfungen.Datum, Prüfungen.Art, " +
                                    "Prüfungen.Beschreibung, Prüfungen.Verifiziert ";  // Für Wunschkriterien...

    private String queryPart1Short = "SELECT Prüfungen.PrüfungsID, Prüfungen.Datum, Prüfungen.Art, Prüfungen.Verifiziert ";


    // Der Inner Join
    private String queryPart2 = "FROM Prüfungen, Fach, Lehrer " +
                                "WHERE Fach.FachID = Prüfungen.Fach && " +
                                "Lehrer.LehrerID = Prüfungen.Lehrer ";


    // Die vom Benutzer gemachten Angaben, getrennt wg. Vorarbeit für Wunschkriterium
    private String jahrgangStringQuery = "&& ? = Prüfungen.Jahrgangsstufe ";
    private String lehrerStringQuery = "&& Lehrer.Name LIKE CONCAT('%', ?, '%') ";
    private String fachStringQuery = "&& Fach.Name LIKE CONCAT('%', ?, '%') ";


    // Diese Abfrage holt die IDs aller Prüfungen, die der User bereits gekauft hat, damit man nachher überprüfen kann,
    // ob man "Kaufen" oder "Download" in den Knopf reinschreiben muss
    private String prüfungBereitsGekauftQuery = "SELECT PrüfungsID FROM Gekauft WHERE Email = ?;";


    // Selbsterklärend: Angabe der für die Abfrage benötigten Informationen
    public AbrufenQuery(String lehrer, String fach, int jahrgang, String currentUser) {
        this.lehrer = lehrer;
        this.fach = fach;
        this.jahrgang = jahrgang;
        this.currentUser = currentUser;
        observerList = new ArrayList<>();
    }


    /**
     * Zuerst werden die Abfrage, welche Prüfungen der User gekauft hat, und die Abfrage nach den Prüfungen
     * selber zusammengebaut und initialisiert, danach mit Parametern gefüllt und schließlich ausgeführt.
     * die IDs der bereits vom User gekauften Prüfungen werden in eine Liste gespeichert und
     *
     * @return Nichts... Vorgabe der Parentmethode, einen Rückgabeparameter zu haben, deshalb ein geboxtes Void
     */
    @Override
    protected Void call() {
        DbConnection conn = DbConnection.getInstance();

/////////// BAU DER ABFRAGE ////////////

        try {
            StringBuilder realQuery = new StringBuilder(200);
            realQuery.append(queryPart1Short);
            realQuery.append(queryPart2);

            // Das ist Vorarbeit für das Wunschkriterium: "Auslassungen möglich bei der Auswahl der Suchkriterien"
            if(jahrgang != 0)
                realQuery.append(jahrgangStringQuery);
            if(!fach.isEmpty())
                realQuery.append(fachStringQuery);
            if(!lehrer.isEmpty())
                realQuery.append(lehrerStringQuery);

            realQuery.append(';');

            Optional<PreparedStatement> optStatement = conn.getPreparedStatement(realQuery.toString());
            Optional<PreparedStatement> optBereitsGekauftStatement = conn.getPreparedStatement(prüfungBereitsGekauftQuery);
            if(!(optStatement.isPresent() && optBereitsGekauftStatement.isPresent())) {
                System.out.println("Kann die Abfrage ned durchführen, weil ich kein PreparedStatement bekommen hab!");
                return null;
            }


            PreparedStatement statement = optStatement.get();
            PreparedStatement bereitsGekauftStatement = optBereitsGekauftStatement.get();

            int anInt = 1;
            if(jahrgang != 0) { //Haste schlampig gedacht, musst es jetzt ausbaden... Zur Verteidigung, es
                statement.setInt(anInt, jahrgang);  //ist grad 00:36
                anInt++;
            }
            if(!fach.isEmpty()) {
                statement.setString(anInt, fach);
                anInt++;
            }
            if(!lehrer.isEmpty()) {
                statement.setString(anInt, lehrer);
            }

            bereitsGekauftStatement.setString(1, currentUser);

//////////// ABFRAGE (möglicherweise zeitintensiv) ////////////
            System.out.println(statement.execute() ? "Abfrage Erfolgreich" : "Fehler bei Abfrage");
            System.out.println(bereitsGekauftStatement.execute() ? "Abfrage 2 Erfolgreich" : "Fehler bei Abfrage 2");

            ResultSet result = statement.getResultSet();
            ResultSet bereitsGekauftResult = bereitsGekauftStatement.getResultSet();

////////// VERARBEITUNG //////////

            ArrayList<Integer> bereitsGekauftePrüfungen = new ArrayList<>();

            // Speichern der bereits gekauften Prüfungen in eine Liste, für einfache Wiederverwendung
            while (bereitsGekauftResult.next())
                bereitsGekauftePrüfungen.add(bereitsGekauftResult.getInt("PrüfungsID"));

            // Erstellen der korrespondierenden AbrufenEntryViews für jede Zeile im ResultSet
            while (result.next()) {

                AbrufenEntryView view = new AbrufenEntryView();
                try{

                    if(!result.getBoolean("Verifiziert")) {
                        continue;
                    }

                    final int prüfungsTypResult = result.getInt(3);
                    final Date datumResult = result.getDate(2);
                    final int prüfungsIDResult = result.getInt(1);


                    // Referenz auf Controller holen, um diese mit Infos zu befüllen
                    AbrufenEntryController controller = (AbrufenEntryController) view.getPresenter();


                    controller.setPrüfungsTyp(prüfungsTypResult);
                    controller.setDatum(datumResult);
                    controller.setPrüfungsID(prüfungsIDResult);
                    controller.setGekauft(bereitsGekauftePrüfungen.contains(prüfungsIDResult)); // Ist die Prüfung schon gekauft worden?

                    // Pretty dank isn't it?    Lambda-Magic ;P
                    // Im Prinzip wird damit die ForEach Schleife im Eventhaindling ;) Thread ausgeführt
                    // und in dieser Schleife für jeden angemeldeten Observer die .handle Methode aufgerunfen
                    Platform.runLater(() -> observerList.forEach((i) -> i.handle(view)));


                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Konnte beim Initialisieren des EntryViews die Werte nicht lesen!");
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void addObserver(UpdateHandler<FXMLView> handler) {
        observerList.add(handler);
    }

    public void execute() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
}
