package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.inject.Inject;

/**
 * Created by Jakob/Miran on 19.05.2017.
 */
@SuppressWarnings("unchecked")
public class HochladenController {
    @FXML
    private Button btnBack;

    @FXML
    private Button btnPruefungAuswaehlen;

    @FXML
    private Button btnHochladen;

    @FXML
    private ComboBox cboxLehrer;

    @FXML
    private ComboBox cboxFach;

    @FXML
    private ComboBox cboxJahrgangsstufe;

    @FXML
    private ComboBox cboxArt;

    @FXML
    private DatePicker datepckDatum;

    @FXML
    private Label labelPruefungDatei;

    @Inject
    private ObservableList<String> jahrgangList;

    @Inject
    private ObservableList<String> fachList;

    @Inject
    private ObservableList<String> lehrerList;

    @Inject
    private ObservableList<String> artList;

    void setCboxLehrerList(ObservableList<String> list) {
        cboxLehrer.setItems(list);
    }

    void setCboxFachList(ObservableList<String> list) {
        cboxFach.setItems(list);
    }

    void setCboxJahrgangList(ObservableList<String> list) {
        cboxJahrgangsstufe.setItems(list);
    }

    void setCboxArtList(ObservableList<String> list) { cboxArt.setItems(list);}

    @FXML
    public void onBackClick(ActionEvent e){

        //Injection bzw. ObservableList Work left

        Stage stage1 = (Stage)(datepckDatum.getScene().getWindow());
        MainWindowView mView = new MainWindowView();
        Scene scene = new Scene(mView.getView());
        stage1.setScene(scene);
        stage1.show();
    }

    @FXML
    public void onPruefungAuswaehlenClick(ActionEvent e){
        //todo
    }

    @FXML
    public void onHochladenClick(ActionEvent e) {
        //todo @Jakob: Wenn du die HochladenQuery benutzen willst, ruf mich an und frag einfach
    }

}
