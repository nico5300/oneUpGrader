package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.AbrufenView;
import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;


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

    @FXML
    public void onBackClicked(ActionEvent e) {
        MainWindowView view = new MainWindowView();
        Stage st = (Stage) cboxFach.getScene().getWindow();
        Scene scene = new Scene(view.getView());
        st.setScene(scene);
        st.show();
    }

    @FXML
    public void onSuchenClicked(ActionEvent e) {
        AbrufenView view = new AbrufenView();
        Stage st = (Stage) cboxFach.getScene().getWindow();
        Scene scene  = new Scene(view.getView());
        st.setScene(scene);
        st.show();
    }
}
