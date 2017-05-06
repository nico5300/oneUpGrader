package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created by nico on 04.05.17.
 */
public class VerifizierenAuswahlController {

    @FXML
    private Button btnBack;

    public void onBackClicked(ActionEvent e) {
        MainWindowView view = new MainWindowView();
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(view.getView());
        stage.setScene(scene);
        stage.show();
    }
}
