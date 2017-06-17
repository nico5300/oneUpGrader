package de.hlg.oneUpGrader.client.dbConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.util.Optional;

/**
 * Created by Nico on 19.04.2017.
 */
public class InformationenQuery extends Task<Void> {

    private ObservableList<String> jahrgangList;
    private ObservableList<String> fachList;
    private ObservableList<String> lehrerList;

    public InformationenQuery() {
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

        Optional<ResultSet> optLehrerResult = conn.executeQuery("SELECT Name FROM Lehrer;");

        if(optLehrerResult.isPresent()) {

            ResultSet lehrerResult = optLehrerResult.get();
            while (lehrerResult.next()) {
                lehrerList.add(lehrerResult.getString(1));
            }
        }

        return null;
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
