package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import javax.swing.*;

/**
 * Created by nico on 09.04.17.
 */
public class AbrufenAuswahlController {
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

    public void setCboxLehrerList(ObservableList<String> list) {
        cboxLehrer.setItems(list);
    }


    @FXML
    public void onBackClicked(ActionEvent e) {
        MainWindowView view = new MainWindowView();
        Stage st = ((Stage) (cboxFach.getScene().getWindow()));
        Scene scene = new Scene(view.getView());
        st.setScene(scene);
        st.show();

    }

    @FXML
    public void onSuchenClicked(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "AuswahlGesucht Stage öffnen");
        String[] st = {"Hi", "Ho", "Hu"};
        cboxLehrer.getItems().addAll(st);
        cboxLehrer.getItems().add("HEYDFJKSÖLFJSKLD");

    }
}
