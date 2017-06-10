package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jakob on 19.05.2017.
 */
@SuppressWarnings("unchecked")
public class HochladenController implements Initializable{

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

        Stage stage1 = (Stage)(datepckDatum.getScene().getWindow());
        MainWindowView mView = new MainWindowView();
        Scene scene = new Scene(mView.getView());
        stage1.setScene(scene);
        stage1.show();
    }


    /**
     * @param ActionEvent
     * @Author Jakob
     *
     *
     *
     *
     */
    @FXML
    public void onPruefungAuswaehlenClick(ActionEvent e){
        //todo
        File file = choosefile();
        String st = file.getAbsolutePath();
        System.out.println(st);
        labelPruefungDatei.setText("hi");


    }

    @FXML
    public void onHochladenClick(ActionEvent e) {
        //todo @Jakob: Wenn du die HochladenQuery benutzen willst, ruf mich an und frag einfach
    }

    /**
     * @param url
     * @param resourceBundle
     * Diese Methode sorgt dafür, dass der cboxArt auswahlmöglichkeiten zur Verfügung gestellt werden.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        artList = FXCollections.observableArrayList();

        artList.add("kleiner Leistungsnachweis");
        artList.add("großer Leistungsnachweis");

        setCboxArtList(artList);
    }

    /**
     *
     * @Author Jakob
     * @return File
     *
     * Diese Methode erlaubt es dem User, den Dateipfad der Hochzuladenden Datei mithilfe des Filechooer Dialogs auszuwählen.
     */

    public File choosefile() {
        FileChooser filechooser = new FileChooser();
        return filechooser.showOpenDialog(datepckDatum.getScene().getWindow());

        //bei der Auswahl von mehreren Bildern zu verwenden --> Wunschkriterium?
        //return filechooser.showOpenMultipleDialog(datepckDatum.getScene().getWindow());
    }
}
