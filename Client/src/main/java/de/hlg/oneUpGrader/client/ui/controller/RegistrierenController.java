package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.AnmeldenView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Created by Miran on 11.05.2017.
 */
public class RegistrierenController {

    @FXML
    private TextField txtfieldEmail;

    @FXML
    private PasswordField passfieldPasswort;

    @FXML
    private PasswordField passfieldPasswortBestaetigen;

    @FXML
    private Button btnRegistrieren;

    @FXML
    private Button btnZurAnmeldung;

    @FXML
    public void onRegistrierenClicked(ActionEvent e) {
        //code here
    }

    @FXML
    public void onZurAnmeldungClicked(ActionEvent e) {
        //code here
        AnmeldenView view = new AnmeldenView();
        Stage st = (Stage) btnRegistrieren.getScene().getWindow();
        Scene sc = new Scene(view.getView());
        st.setScene(sc);
        st.show();
    }
}
