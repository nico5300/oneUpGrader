package de.hlg.oneUpGrader.client.dbConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.ResultSet;
import java.util.Optional;

/**
 * Created by nico on 19.04.17.
 */
public class AbrufenAuswahlTeacherQuery extends Task<ObservableList<String>> {


    @Override
    protected ObservableList<String> call() throws Exception {



        DbConnection conn = DbConnection.getInstance();
        Optional<ResultSet> optional = conn.executeQuery("SELECT Vorname, Nachname FROM Lehrer;");
        ResultSet result;

        if(!optional.isPresent()) {
            System.out.println("So Kann mein Task nicht arbeiten.... I'm outta here......");
            return null;
        }

        result = optional.get();
        ObservableList<String> list = FXCollections.observableArrayList();

        while (result.next()) {
            String nextEntry = result.getString(1) + " " + result.getString(2);
            list.add(nextEntry);

        }
        return list;
    }
}
