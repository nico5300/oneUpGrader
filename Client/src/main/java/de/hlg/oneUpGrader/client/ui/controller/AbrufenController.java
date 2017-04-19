package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.AbrufenAuswahlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by nico on 19.04.17.
 */
public class AbrufenController {

    @FXML
    private Label lbJahrgang;

    @FXML
    private Label lbFach;

    @FXML
    private Label lbLehrer;

    @FXML
    private Button btnBack;

    @FXML
    public void onBackClicked(ActionEvent e) {
        AbrufenAuswahlView view = new AbrufenAuswahlView();
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(view.getView());
        stage.setScene(scene);
        stage.show();
    }

}
