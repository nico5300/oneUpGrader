package de.hlg.oneUpGrader.client.ui.controller;

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
 * Created by nico on 09.04.17.
 */
public class AbrufenAuswahlController implements Initializable{
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
    HashMap<String, Object> injectionMap;

    @Inject
    ObservableList<String> jahrgangList;

    @Inject
    ObservableList<String> fachList;

    @Inject
    ObservableList<String> lehrerList;

    @Inject
    private String fachString;

    @Inject
    private String lehrerString;

    @Inject
    private int jahrgangInt;

    public void setCboxLehrerList(ObservableList<String> list) {
        cboxLehrer.setItems(list);
    }

    public void setCboxFachList(ObservableList<String> list) {
        cboxFach.setItems(list);
    }

    public void setCboxJahrgangList(ObservableList<String> list) {
        cboxJahrgang.setItems(list);
    }

    @FXML
    public void onBackClicked(ActionEvent e) {
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
                jahrgangInt = Integer.parseInt(cboxJahrgang.getEditor().getText());


            if(jahrgangInt != 0) {
                if(jahrgangInt>12 || jahrgangInt<5)
                    throw new NumberFormatException();
            }

        } catch (NumberFormatException e1) {
            //e1.printStackTrace();
            JOptionPane.showConfirmDialog(null, "Der Jahrgang muss eine Zahl zwischen\n" +
                    "5 und 12 (eingeschlossen) sein!", "Fehler!", JOptionPane.DEFAULT_OPTION, JOptionPane.OK_OPTION);
            return;
        }

        injectionMap.put("lehrerString", lehrerString);
        injectionMap.put("fachString", fachString);
        injectionMap.put("jahrgangInt", jahrgangInt);

        AbrufenView view = new AbrufenView();
        Stage st = (Stage) cboxFach.getScene().getWindow();
        Scene scene  = new Scene(view.getView());
        st.setScene(scene);
        st.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(cboxJahrgang.getItems().isEmpty())
            cboxJahrgang.setItems(jahrgangList);

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
