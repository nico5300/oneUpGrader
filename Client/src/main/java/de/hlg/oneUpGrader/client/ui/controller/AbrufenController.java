package de.hlg.oneUpGrader.client.ui.controller;

import com.airhacks.afterburner.views.FXMLView;
import de.hlg.oneUpGrader.client.UpdateHandler;
import de.hlg.oneUpGrader.client.ui.view.AbrufenAuswahlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by nico on 19.04.17.
 */
public class AbrufenController implements Initializable, UpdateHandler<FXMLView>{

    @FXML
    private Label lbJahrgang;

    @FXML
    private Label lbFach;

    @FXML
    private Label lbLehrer;

    @FXML
    private Button btnBack;

    @FXML
    private VBox vbox;

    @Inject
    private String fachString;

    @Inject
    private String lehrerString;

    @Inject
    private int jahrgangInt;

    @FXML
    public void onBackClicked(ActionEvent e) {
        AbrufenAuswahlView view = new AbrufenAuswahlView();
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(view.getView());
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!fachString.isEmpty())
            lbFach.setText(fachString);
        else
            lbFach.setText("<beliebig>");
        if(!lehrerString.isEmpty())
            lbLehrer.setText(lehrerString);
        else
            lbLehrer.setText("<beliebig>");
        if(jahrgangInt != 0)
            lbJahrgang.setText(Integer.toString(jahrgangInt));
        else
            lbJahrgang.setText("<beliebig>");
    }


    @Override
    public void handle(FXMLView obj) {
        vbox.getChildren().add(obj.getView());
    }
}
