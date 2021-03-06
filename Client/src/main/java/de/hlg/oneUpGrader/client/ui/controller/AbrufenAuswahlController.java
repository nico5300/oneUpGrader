package de.hlg.oneUpGrader.client.ui.controller;

import com.airhacks.afterburner.views.FXMLView;
import de.hlg.oneUpGrader.client.UpdateHandler;
import de.hlg.oneUpGrader.client.dbConnection.AbrufenQuery;
import de.hlg.oneUpGrader.client.dbConnection.BereitsGekauftAbrufenQuery;
import de.hlg.oneUpGrader.client.ui.view.AbrufenView;
import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.swing.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;


/**
 * Created by Nico on 09.04.17.
 */
@SuppressWarnings("unchecked") // Das ist hier in Ordnung............
public class AbrufenAuswahlController implements Initializable {
    @FXML
    private Button btnGekauftePruefungen;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnSuchen;

    @FXML
    private ComboBox<String> cboxJahrgang;

    @FXML
    private ComboBox<String> cboxFach;

    @FXML
    private ComboBox<String> cboxLehrer;

    @Inject
    private HashMap<String, Object> injectionMap;

    @Inject
    private ObservableList<String> jahrgangList;

    @Inject
    private ObservableList<String> fachList;

    @Inject
    private ObservableList<String> lehrerList;

    @Inject
    private String fachString;

    @Inject
    private String lehrerString;

    @Inject
    private int jahrgangInt;

    @Inject
    private String currentUser;

//Anmelden Query verwendet die methoden um zeug in die dropdownboxen einzufügen (in der view die auswahlmöglichkeiten)
    void setCboxLehrerList(ObservableList<String> list) {
        cboxLehrer.setItems(list);
    }

    void setCboxFachList(ObservableList<String> list) {
        cboxFach.setItems(list);
    }

    void setCboxJahrgangList(ObservableList<String> list) {
        cboxJahrgang.setItems(list);
    }

    @FXML
    public void onGekauftePruefungenClick() {

        injectionMap.put("lehrerString", lehrerString);
        injectionMap.put("fachString", fachString);
        injectionMap.put("jahrgangInt", jahrgangInt);

        BereitsGekauftAbrufenQuery query = new BereitsGekauftAbrufenQuery(currentUser);

        AbrufenView view = new AbrufenView();

        //meldet diesen controller bei der query an die dann so bericht erstatten kann wenn ein ergebnis fertig ist um in die view eingefügt zu werden
        query.addObserver( (UpdateHandler<FXMLView>) view.getPresenter());
        //.........weil hier oben nur AbrufenController rauskommen, die UpdateHandler implementieren!
        query.execute();

        Stage st = (Stage) cboxFach.getScene().getWindow();
        Scene scene  = new Scene(view.getView());
        st.setScene(scene);
        st.show();
    }

    @FXML
    public void onBackClicked(ActionEvent e) {

        //die Ergebnisse (Referenzen) sollen neu geladen werden wenn man raus und wieder rein geht
        injectionMap.remove("jahrgangList");
        injectionMap.remove("fachList");
        injectionMap.remove("lehrerList");
        injectionMap.remove("fachString");
        injectionMap.remove("lehrerString");
        injectionMap.remove("jahrgangInt");

        MainWindowView view = new MainWindowView();
        Stage st = (Stage) cboxFach.getScene().getWindow();
        Scene scene = new Scene(view.getView());
        st.setScene(scene);
        st.show();
    }

    @FXML
    public void onSuchenClicked(ActionEvent e) {


        String lehrerString = cboxLehrer.getEditor().getText();
        String fachString = cboxFach.getEditor().getText();
        int jahrgangInt = 0;

        try {
            if(!cboxJahrgang.getEditor().getText().isEmpty())
                //der eingegebene string wird zu einem integer gemacht
                jahrgangInt = Integer.parseInt(cboxJahrgang.getEditor().getText());


            if(jahrgangInt != 0) {
                if(jahrgangInt>12 || jahrgangInt<5)
                    throw new NumberFormatException();
            }

        } catch (NumberFormatException e1) {
            //e1.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Der Jahrgang muss eine Zahl zwischen\n" +
                    "5 und 12 (eingeschlossen) sein!", "Fehler!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }



        ////////// ZUR VEREINFACHUNG !!!!! Später wenn Wunschkriterien eingeführt, entfernen ///////

        if(lehrerString.isEmpty() || fachString.isEmpty() || jahrgangInt == 0) {
            JOptionPane.showConfirmDialog(null, "Alle Felder müssen ausgefüllt werden\n" +
                    "(zumindest noch... Kann sich alles ändern)", "Fehler!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        // injectionmap ist eine art datenbank von fx und da werden die 3 attribute zwischengespeichert ... der AbrufenController hohlt sie sich dann
        injectionMap.put("lehrerString", lehrerString);
        injectionMap.put("fachString", fachString);
        injectionMap.put("jahrgangInt", jahrgangInt);

        //die klasse kümmert sich um die abzurufenden ergebnisse
        AbrufenQuery query = new AbrufenQuery(lehrerString, fachString, jahrgangInt, currentUser);


        AbrufenView view = new AbrufenView();

        //meldet diesen controller bei der query an die dann so bericht erstatten kann wenn ein ergebnis fertig ist um in die view eingefügt zu werden
        query.addObserver( (UpdateHandler<FXMLView>) view.getPresenter());
        //.........weil hier oben nur AbrufenController rauskommen, die UpdateHandler implementieren!
        query.execute();

        Stage st = (Stage) cboxFach.getScene().getWindow();
        Scene scene  = new Scene(view.getView());
        st.setScene(scene);
        st.show();
    }


    @Override


    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(cboxJahrgang.getItems().isEmpty())       // Diese Abfragen verhindern das Entstehen einer
            cboxJahrgang.setItems(jahrgangList);    // Race-Condition, die durch die vorzeitige
                                                    // Abarbeitung des AbrufenAuswahlQuerys entstehen könnte
        if(cboxLehrer.getItems().isEmpty())
            cboxLehrer.setItems(lehrerList);

        if(cboxFach.getItems().isEmpty())
            cboxFach.setItems(fachList);

        cboxLehrer.getEditor().setText(lehrerString);
        cboxFach.getEditor().setText(fachString);
        if(jahrgangInt != 0)
            cboxJahrgang.getEditor().setText(Integer.toString(jahrgangInt));
    }


}
