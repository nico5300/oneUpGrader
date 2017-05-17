package de.hlg.oneUpGrader.client.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

/**
 * Created by Jakob on 11.05.2017.
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
    }
}
