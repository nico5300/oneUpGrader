package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.dbConnection.HochladenQuery;
import de.hlg.oneUpGrader.client.dbConnection.Prüfung;
import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Jakob on 19.05.2017.
 */
@SuppressWarnings("unchecked")
public class HochladenController implements Initializable {

    private File file;

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
    private Label labelPruefungsDatei;

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
     * Diese Methode zeigt den Dateipfad des file objekts in dem labelPruefungsdatei an
     *
     */
    @FXML
    public void onPruefungAuswaehlenClick(ActionEvent e){
        choosefile();

        String st = file.getAbsolutePath();
        labelPruefungsDatei.setText(st);

    }

    /**
     * @param ActionEvent
     * @Author Jakob
     *
     * Diese Methode erstellt anhand der gemachten Eingaben eine Prüfungsdatei.
     * Diese wird dann einer HochladenQuery übergeben.
     *
     */

    @FXML
    public void onHochladenClick(ActionEvent e) {

        String fach = (String) cboxFach.getValue();                 //rauslesen der Daten aus den Eingabeobjekten
        String lehrer = (String) cboxLehrer.getValue();
        String sjahrgang = (String) cboxJahrgangsstufe.getValue();
        LocalDate ldatum = datepckDatum.getValue();
        String sArt = (String) cboxArt.getValue();
        String beschreibung = "---";                                //Beschreibungsfunktion noch nicht implementiert...

        System.out.println(fach);                                   //debug
        System.out.println(lehrer);
        System.out.println(sjahrgang);
        System.out.println(ldatum);
        System.out.println(sArt);


        if ((fach != null) &&                                         //überprüfen ob alle Eingaben gemacht wurden
            (lehrer != null) &&
            (sjahrgang != null) &&
            (ldatum != null &&
            (sArt != null) &&
            (labelPruefungsDatei.getText().compareTo("---") != 0)))
        {

            int jahrgang = Integer.parseInt(sjahrgang);                     //umwandlung des sjahrgangstrings in ein int

            java.util.Date datum = java.sql.Date.valueOf(ldatum);           //umwandlung des Localdate Objekts in ein Date objekt

            BufferedImage img = null;
            try {
                img = ImageIO.read(file);                                   //umwandlung der file Datei in eine image Datei
            } catch (IOException r) {
            }


            boolean art = true;

            if (sArt.compareTo("großer Leistungsnachweis") == 0) {          //"umwandlung" des Strings der Combobox in ein boolean
                art = true;
            }
            else {
                art = false;
            }


            Prüfung pruefung = new Prüfung(fach, lehrer, jahrgang, datum, art, beschreibung, img);          //Arstellen einer Prüfung

            HochladenQuery hq = new HochladenQuery(pruefung);                                               //Ausführen der Query
            hq.execute();

        }
        else {
            Alert al = new Alert(Alert.AlertType.ERROR, "Es fehlen Eingaben!", ButtonType.OK);
            al.show();
        }

    }

    /**
     * @param url
     * @param resourceBundle
     *
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

    public void choosefile() {

        FileChooser filechooser = new FileChooser();
        filechooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),    //Filter um nur jpg und png Dateien zu erlauben
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        file = filechooser.showOpenDialog(datepckDatum.getScene().getWindow());

        //bei der Auswahl von mehreren Bildern zu verwenden --> Wunschkriterium?
        //return filechooser.showOpenMultipleDialog(datepckDatum.getScene().getWindow());
    }
}
