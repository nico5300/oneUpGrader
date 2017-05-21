package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import de.hlg.oneUpGrader.client.ui.view.VerifizierenView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


/**
 * Created by Miran on 04.05.17.
 */
public class VerifizierenAuswahlController {

    @FXML
    private Label labelJahrgangsstufe;

    @FXML
    private Label labelFach;

    @FXML
    private Label labelLehrer;

    @FXML
    private Label labelDatum;

    @FXML
    private Label labelArt;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnPruefungHerunterladen;

    @FXML
    private Button btnBewerten;

    public void onBackClicked(ActionEvent e) {
        MainWindowView view = new MainWindowView();
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(view.getView());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onPruefungHerunterladenClicked(ActionEvent e) {

    }

    @FXML
    public void onBewertenClicked(ActionEvent e) {
        VerifizierenView vView = new VerifizierenView();
        Stage st = (Stage) (btnBack.getScene().getWindow());
        Scene scene = new Scene(vView.getView());
        st.setScene(scene);
        st.show();
    }


}
