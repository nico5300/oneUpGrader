package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.dbConnection.AnmeldenQuery;
import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import de.hlg.oneUpGrader.client.ui.view.RegistrierenView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.HashMap;


/**
 * Created by Miran on 11.05.2017.
 */
public class AnmeldenController {
    @FXML
    private Button btnAnmelden;

    @FXML
    private Button btnRegistrieren;

    @FXML
    private Text textAnmeldung;

    @FXML
    private TextField txtfieldEmail;

    @FXML
    private PasswordField passfieldPasswort;

    @Inject
    private HashMap<String, Object> injectionMap;

    @FXML
    public void onAnmeldenClicked(ActionEvent e) {

        AnmeldenQuery query = new AnmeldenQuery(txtfieldEmail.getText(), passfieldPasswort.getText());

        //stateEvent muss hingeschriebenwerden ... ist egal
        // query ist abgeleitet von task und task ausführbar ....wird als thread behandelt/es ist ein thread
        //setOnScheduled((stateEvent) -> wird ausgeführt wenn der thread kurz davor ist ausgeführt zu werden
        //wenn der thread der query gestartet wird und die anmeldung läuft
        // darf niemand auf die buttons klicken!
        query.setOnScheduled((stateEvent) -> {

            textAnmeldung.setText("Anmeldevorgang läuft...");
            btnAnmelden.setDisable(true);
            btnRegistrieren.setDisable(true);
            txtfieldEmail.setDisable(true);
            passfieldPasswort.setDisable(true);
        });

        //wird ausgeführt wenn der thread beendet wurde
        query.setOnSucceeded((stateEvent) -> {
            if(  query.getValue()) { // ACHTUNG!!!!!! AUSRUFEZEICHEN NACHHER ENTFERNEN --BISHER DEBUG--

                injectionMap.put("currentUser", txtfieldEmail.getText());

                MainWindowView view = new MainWindowView();
                Stage currentStage = (Stage) txtfieldEmail.getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setTitle(  currentStage.getTitle() );
                Scene scene = new Scene(view.getView());
                newStage.setScene(scene);
                newStage.show();
                currentStage.close();       // Hier hat es funktioniert, User wurde eingeloggt

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Falscher Benutzername, falsches Passwort\noder keine " +
                        "Datenbankverbindung!", ButtonType.OK);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
                btnAnmelden.setDisable(false);
                btnRegistrieren.setDisable(false);
                txtfieldEmail.setDisable(false);
                passfieldPasswort.setDisable(false);
                textAnmeldung.setText("Anmeldung");     // Hier hat das Anmelden nicht geklappt, Fehlermeldung!
            }
        });

        query.execute();


    }

    @FXML
    public void onRegistrierenClicked(ActionEvent e) {
        //code here
        RegistrierenView view = new RegistrierenView();
        Stage currentStage = (Stage) txtfieldEmail.getScene().getWindow();
        Scene scene = new Scene(view.getView());
        currentStage.setScene(scene);
        currentStage.show();                    // Zur RegistrierenView wechseln
    }
}
