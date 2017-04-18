package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.dbConnection.AbrufenAuswahlTeacherQuery;
import de.hlg.oneUpGrader.client.ui.view.AbrufenAuswahlView;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

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

        AbrufenAuswahlTeacherQuery tq = new AbrufenAuswahlTeacherQuery();
            tq.setOnSucceeded( (WorkerStateEvent t) -> {
                try {
                    ((AbrufenAuswahlController)view.getPresenter()).setCboxLehrerList(tq.get());
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            });
        new Thread(tq).start();


    }

    @FXML
    private void onHochladenClick(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Hochladen Stage öffnen");

    }


}
