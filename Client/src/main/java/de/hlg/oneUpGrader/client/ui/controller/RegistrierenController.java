package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.dbConnection.RegistrierenQuery;
import de.hlg.oneUpGrader.client.ui.view.AnmeldenView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


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
    /**
     * @param ActionEvent
     * @Authot Jakob
     *
     * Es wird zunächst überprüft, ob die Passwörter in beiden Passwortfeldern identisch sind.
     * Wenn ja, dann wird mit der RegistrierenQuery in der Datenbank ein neuer Benutzer erstellt.
     * Ist der Benutzername jedoch nicht mehr verfügber, so gibt es eine Fehlermeldung.
     */
    public void onRegistrierenClicked(ActionEvent e) {

        if ((passfieldPasswort.getText().compareTo(passfieldPasswortBestaetigen.getText())) != 0) {
            Alert al = new Alert(Alert.AlertType.ERROR, "Passwörter sind nicht identisch!", ButtonType.OK);
            al.show();
            return;
        }

        RegistrierenQuery rq = new RegistrierenQuery(txtfieldEmail.getText(), passfieldPasswort.getText());
        rq.setOnSucceeded((event) -> {

            if (!rq.getValue()) {
                Alert al2 = new Alert(Alert.AlertType.ERROR, "Benutzername nicht verfügbar!", ButtonType.OK);
                al2.show();
                return;
            }
            else
            {
                Alert al3 = new Alert(Alert.AlertType.INFORMATION, "Registrierung erfolgreich.", ButtonType.OK);
                al3.show();
                return;
            }

        });




        rq.execute();

    }

    @FXML
    public void onZurAnmeldungClicked(ActionEvent e) {
        AnmeldenView view = new AnmeldenView();
        Stage st = (Stage) btnRegistrieren.getScene().getWindow();
        Scene sc = new Scene(view.getView());
        st.setScene(sc);
        st.show();
    }
}
