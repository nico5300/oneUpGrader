package de.hlg.oneUpGrader.client.dbConnection;

import com.airhacks.afterburner.views.FXMLView;
import de.hlg.oneUpGrader.client.UpdateHandler;
import javafx.concurrent.Task;

import java.util.List;

/**
 * Created by nico on 01.05.17.
 */
public class AlreadyBoughtQuery extends Task<Void> {

    private List<UpdateHandler<FXMLView>> observerList;

    private String query = "SELECT Prüfungen.PrüfungsID, Prüfungen.Datum, Prüfungen.Art" +
            ""; // TODO: Query fertig schreiben!!!!!

    @Override
    protected Void call() throws Exception {
        DbConnection connection = DbConnection.getInstance();


        return null;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
