package de.hlg.oneUpGrader.client.dbConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.util.Optional;

/**
 * Created by Nico on 19.04.2017.
 */
public class AbrufenAuswahlQuery extends Task<Void> {

    ObservableList<String> jahrgangList;
    ObservableList<String> fachList;
    ObservableList<String> lehrerList;

    public AbrufenAuswahlQuery() {
        jahrgangList = FXCollections.observableArrayList();
        fachList = FXCollections.observableArrayList();
        lehrerList = FXCollections.observableArrayList();
    }

    @Override
    protected Void call() throws Exception {
        DbConnection conn = DbConnection.getInstance();

        for(int i=5; i<=12; i++)
            jahrgangList.add(Integer.toString(i));

        Optional<ResultSet> optFachResult = conn.executeQuery("SELECT Name FROM Fach;");

        if(optFachResult.isPresent()) {

            ResultSet fachResult = optFachResult.get();
            while (fachResult.next())
                fachList.add(fachResult.getString(1));

        } else {
            System.out.println("Konnte Die Jahrgangsliste nicht fühlen, weil ich" +
                    " das ResultSet nicht bekommen hab");
        }

        Optional<ResultSet> optLehrerResult = conn.executeQuery("SELECT Vorname, Nachname FROM Lehrer;");

        if(optLehrerResult.isPresent()) {

            ResultSet lehrerResult = optLehrerResult.get();
            while (lehrerResult.next()) {
                lehrerList.add(lehrerResult.getString(1) + " " + lehrerResult.getString(2));
            }
        }

        return null;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    public ObservableList<String> getJahrgangList() {
        return jahrgangList;
    }

    public ObservableList<String> getFachList() {
        return fachList;
    }

    public ObservableList<String> getLehrerList() {
        return lehrerList;
    }
}
