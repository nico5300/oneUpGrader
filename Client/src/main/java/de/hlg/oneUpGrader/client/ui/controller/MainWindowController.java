package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.dbConnection.InformationenQuery;
import de.hlg.oneUpGrader.client.ui.view.AbrufenAuswahlView;
import de.hlg.oneUpGrader.client.ui.view.AnmeldenView;
import de.hlg.oneUpGrader.client.ui.view.HochladenView;
import de.hlg.oneUpGrader.client.ui.view.VerifizierenAuswahlView;
import javafx.collections.FXCollections;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.HashMap;

/**
 * Created by Miran on 08.04.17.
 */
public class MainWindowController {

    @Inject
    private HashMap<String, Object> injectionMap;

    @FXML
    private Button btnAbmelden;

    @FXML
    private Button btnVerifizieren;

    @FXML
    private Button btnAbrufen;

    @FXML
    private Button btnHochladen;

    @FXML
    public void onAbmeldenClicked(ActionEvent e) {
        // currentUser aus memory löschen
        injectionMap.remove("currentUser");

        AnmeldenView view = new AnmeldenView();
        Stage currentStage = ((Stage) (btnVerifizieren.getScene().getWindow()));
        Stage newStage = new Stage();
        Scene scene = new Scene(view.getView());
        newStage.setScene(scene);
        newStage.setTitle(currentStage.getTitle());
        newStage.show();
        currentStage.close();
    }

    @FXML
    private void onVerifizierenClick(ActionEvent e) {
        VerifizierenAuswahlView view = new VerifizierenAuswahlView();
        Stage st = ((Stage) (btnVerifizieren.getScene().getWindow()));
        Scene scene = new Scene(view.getView());
        st.setScene(scene);
        st.show();
    }

    @FXML
    private void onAbrufenClick(ActionEvent e) {

        injectionMap.put("lehrerList", FXCollections.observableArrayList());
        injectionMap.put("fachList", FXCollections.observableArrayList());
        injectionMap.put("jahrgangList", FXCollections.observableArrayList());

        AbrufenAuswahlView view = new AbrufenAuswahlView();
        Stage st = ((Stage) (btnAbrufen.getScene().getWindow()));
        Scene scene = new Scene(view.getView());
        st.setScene(scene);
        st.show();



        InformationenQuery iq = new InformationenQuery();

        iq.setOnSucceeded( (WorkerStateEvent t) -> {
            AbrufenAuswahlController controller = (AbrufenAuswahlController) view.getPresenter();
            controller.setCboxLehrerList(iq.getLehrerList());
            controller.setCboxFachList(iq.getFachList());
            controller.setCboxJahrgangList(iq.getJahrgangList());

            injectionMap.remove("lehrerList");
            injectionMap.remove("fachList");
            injectionMap.remove("jahrgangList");
            injectionMap.put("lehrerList", iq.getLehrerList());
            injectionMap.put("fachList", iq.getFachList());
            injectionMap.put("jahrgangList", iq.getJahrgangList());
        });

        Thread t = new Thread(iq);
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void onHochladenClick(ActionEvent e) {

        injectionMap.put("lehrerList", FXCollections.observableArrayList());
        injectionMap.put("fachList", FXCollections.observableArrayList());
        injectionMap.put("jahrgangList", FXCollections.observableArrayList());
        injectionMap.put("artList", FXCollections.observableArrayList());
        //combo.getItems.add(0);

        HochladenView hView = new HochladenView();
        Stage stage1 = (Stage) (btnHochladen.getScene().getWindow());
        Scene scene = new Scene(hView.getView());
        stage1.setScene(scene);
        stage1.show();

        InformationenQuery iq = new InformationenQuery();

        iq.setOnSucceeded( (WorkerStateEvent t) -> {
            HochladenController controller = (HochladenController) hView.getPresenter();
            controller.setCboxLehrerList(iq.getLehrerList());
            controller.setCboxFachList(iq.getFachList());
            controller.setCboxJahrgangList(iq.getJahrgangList());

            injectionMap.remove("lehrerlist");
            injectionMap.remove("fachList");
            injectionMap.remove("jahrgangList");
            injectionMap.put("lehrerlist", iq.getLehrerList());
            injectionMap.put("fachList", iq.getFachList());
            injectionMap.put("jahrgangList", iq.getJahrgangList());
        });

        Thread t = new Thread(iq);
        t.setDaemon(true);
        t.start();

    }
}
