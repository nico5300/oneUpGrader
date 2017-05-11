package main.java.de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by Jakob on 08.05.2017.
 */
public class VerifizierenController {

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
    private Button btnMangelhaft;

    @FXML
    private Button btnInOrdnung;

    @FXML
    public void onMangelhaftClicked(ActionEvent e) {
        //code here
    }

    @FXML
    public void onInOrdnungClicked(ActionEvent e) {
        //code here
    }

}
