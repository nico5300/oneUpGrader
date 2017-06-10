package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import de.hlg.oneUpGrader.client.ui.view.RegistrierenView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
    private Text textAnmeldung;

    @FXML
    private TextField txtfieldEmail;

    @FXML
    private PasswordField passfieldPasswort;

    @FXML
    public void onAnmeldenClicked(ActionEvent e) {

        MainWindowView view = new MainWindowView();
        Stage currentStage = (Stage) txtfieldEmail.getScene().getWindow();
        Stage newStage = new Stage();
        newStage.setTitle(  currentStage.getTitle() );
        Scene scene = new Scene(view.getView());
        newStage.setScene(scene);
        newStage.show();
        currentStage.close();       // Hier hat es funktioniert, User wurde eingeloggt
        /*


        //code here
        // Debug: damit man die anderen Fenster auch erreichen kann......


        //
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

        //wird ausgeführt wenn der thread ausgeführt ist
        query.setOnSucceeded((stateEvent) -> {
            if( ! query.getValue()) { // ACHTUNG!!!!!! AUSRUFEZEICHEN NACHHER ENTFERNEN --BISHER DEBUG--
                MainWindowView view = new MainWindowView();
                Stage currentStage = (Stage) txtfieldEmail.getScene().getWindow();
                Stage newStage = new Stage();
                newStage.setTitle(  currentStage.getTitle() );
                Scene scene = new Scene(view.getView());
                newStage.setScene(scene);
                newStage.show();
                currentStage.close();       // Hier hat es funktioniert, User wurde eingeloggt
                // TODO: 17.05.17 username iwo speichern...

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
        */

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
