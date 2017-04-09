package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.AbrufenAuswahlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Nico on 08.04.17.
 */
public class MainWindowController {

    @FXML
    private Button btnVerifizieren;

    @FXML
    private Button btnAbrufen;

    @FXML
    private Button btnHochladen;

    @FXML
    private void onVerifizierenClick(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Verifizieren Stage öffnen");
    }

    @FXML
    private void onAbrufenClick(ActionEvent e) {
        AbrufenAuswahlView view = new AbrufenAuswahlView();
        Stage st = ((Stage) (btnAbrufen.getScene().getWindow()));
        Scene scene = new Scene(view.getView());
        st.setScene(scene);
        st.show();
    }

    @FXML
    private void onHochladenClick(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Hochladen Stage öffnen");

    }


}
