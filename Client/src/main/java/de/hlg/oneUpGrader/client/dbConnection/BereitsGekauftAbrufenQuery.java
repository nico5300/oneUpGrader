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
 * Created by nico on 12.07.17.
 */
public class BereitsGekauftAbrufenQuery extends Task<Void> {

    private String currentUser;
    private List<UpdateHandler<FXMLView>> observerList; // Hier werden die angemeldeten Observer gespeichert


    // Der SELECT Teil der Abfrage
    private String queryPart1Long = "SELECT Prüfungen.PrüfungsID, Fach.Name, Lehrer.Name, " +
            "Prüfungen.Jahrgangsstufe, Prüfungen.Datum, Prüfungen.Art, " +
            "Prüfungen.Beschreibung, Prüfungen.Verifiziert, Gekauft.Email";  // Für Wunschkriterien...

    private String queryPart1Short = "SELECT Prüfungen.PrüfungsID, Prüfungen.Datum, Prüfungen.Art," +
            " Prüfungen.Verifiziert, Gekauft.Email ";


    private String queryPart2 = "FROM Prüfungen, Fach, Lehrer, Gekauft ";

    private String queryPart3 = "WHERE Gekauft.PrüfungsID = Prüfungen.PrüfungsID && " +
                                "Fach.FachID = Prüfungen.Fach && " +
                                "Lehrer.LehrerID = Prüfungen.Lehrer && " +
                                "Gekauft.Email = ? " ;


    public BereitsGekauftAbrufenQuery(String currentUser) {
        this.currentUser = currentUser;
        observerList = new ArrayList<>();
    }


    @Override
    protected Void call() {

        DbConnection conn = DbConnection.getInstance();

        StringBuilder builder = new StringBuilder(500);

        builder.append(queryPart1Short).append(queryPart2).append(queryPart3).append(';');

        Optional<PreparedStatement> optStatement = conn.getPreparedStatement(builder.toString());

        if(!optStatement.isPresent()) {
            System.out.println("Konnte Prepared Statement nicht erzeugen!");
            return null;
        }

        PreparedStatement statement = optStatement.get();

        try {
            statement.setString(1, currentUser);
            statement.execute();

            ResultSet result = statement.getResultSet();

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
                    controller.setGekauft(true); // Ist die Prüfung schon gekauft worden?

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
            System.out.println("Fehler beim Ausführen der Abfrage");
            e.printStackTrace();
        }


        return null;
    }

    public void addObserver(UpdateHandler<FXMLView> handler) {
        observerList.add(handler);
    }


    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
