package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import de.hlg.oneUpGrader.client.ui.view.RegistrierenView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Created by Jakob on 11.05.2017.
 */
public class AnmeldenController {
    @FXML
    private Button btnAnmelden;

    @FXML
    private Button btnRegistrieren;

    @FXML
    private TextField txtfieldEmail;

    @FXML
    private PasswordField passfieldPasswort;

    @FXML
    public void onAnmeldenClicked(ActionEvent e) {
        //code here
        // Debug: damit man die anderen Fenster auch erreichen kann......
        MainWindowView view = new MainWindowView();
        Stage currentStage = (Stage) txtfieldEmail.getScene().getWindow();
        Stage newStage = new Stage();
        newStage.setTitle(  currentStage.getTitle() );
        Scene scene = new Scene(view.getView());
        newStage.setScene(scene);
        newStage.show();
        currentStage.close();
    }

    @FXML
    public void onRegistrierenClicked(ActionEvent e) {
        //code here
        RegistrierenView view = new RegistrierenView();
        Stage currentStage = (Stage) txtfieldEmail.getScene().getWindow();
        Scene scene = new Scene(view.getView());
        currentStage.setScene(scene);
        currentStage.show();
    }
}
