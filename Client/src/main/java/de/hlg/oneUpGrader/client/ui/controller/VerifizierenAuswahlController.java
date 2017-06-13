package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.dbConnection.Prüfung;
import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import de.hlg.oneUpGrader.client.ui.view.VerifizierenView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Jakob/Miran on 04.05.17.
 */
public class VerifizierenAuswahlController {

    @Inject
    private HashMap<String, Object> injectionMap;

    @Inject
    private Prüfung VerifizierenPruefung;

    private boolean pruefungHeruntergeladen = false;

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

    /**
     * @Author Jakob
     *
     * Konstruktor von VerifizerienAuswahlController; füllt Labels mit Informationen der zu verifizierenden Prüfung
     *
     *
     */

    public VerifizierenAuswahlController() {
        Prüfung p = VerifizierenPruefung;


        Date pdatum = new Date();
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");          //umwandlung des Date Objekts in ein String
        String datum = formatter.format(pdatum);

        String art;
        if (p.getArt()) {                                                       //"umwandlung" des Booleans art in ein String
            art = "großer Leistungsnachweis (z.B.: Schulaufgabe, Klausur)";
        }
        else {
            art = "kleiner Leistungsnachweis (z.B.: Stegreifaufgabe, Test)";
        }


        labelJahrgangsstufe.setText("" + p.getJahrgangsstufe());                //  "" ist notwendig damit der Übergabeparameter ein String ist
        labelFach.setText(p.getFach());
        labelLehrer.setText(p.getLehrer());
        labelDatum.setText(datum);
        labelArt.setText(art);
    }


    /**
     * Diese Methode ermöglich das zurückwechseln zum MainWindow, solange die Prüfung noch nicht heruntergeladen wurde.
     *
     *
     * @Author Jakob
     * @param ActionEvent e
     */

    public void onBackClicked(ActionEvent e) {

        if (pruefungHeruntergeladen == false) {
            MainWindowView view = new MainWindowView();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            Scene scene = new Scene(view.getView());
            stage.setScene(scene);
            stage.show();

            injectionMap.remove("VerifizierenPruefung");
        }
        else {
            Alert al = new Alert(Alert.AlertType.ERROR, "Prüfung muss nach dem\nHerunterladen Bewertet werden!", ButtonType.OK);
            al.show();
        }
    }

    /**
     * Diese Methode ermöglicht es dem User, die zu verifizierende Prüfung im Systemspeicher abzuspeichern.
     *
     * @Author Jakob
     * @param ActionEvent e
     */


    @FXML
    public void onPruefungHerunterladenClicked(ActionEvent e) {
        pruefungHeruntergeladen = true;

        FileChooser fc = new FileChooser();
        fc.setInitialFileName("Zu_Verifizierende_Pruefung.png");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"));

        Prüfung p = VerifizierenPruefung;

        BufferedImage img = p.getBild();           //auslesen der Bilddatei aus der Prüfung

        File file = fc.showSaveDialog(btnBack.getScene().getWindow());         //anzeigen des Speicherdialogs; bekommen des Speicherorts

        try {
            ImageIO.write(img, "PNG", file);                              //speichern der Datei
        } catch (IOException r) {
            Alert al = new Alert(Alert.AlertType.ERROR, "Format Exception!", ButtonType.OK);
            al.show();
            pruefungHeruntergeladen = false;
            return;
        }
    }

    /**
     * Diese Methode ermöglicht dem User dann das Wechseln zum Verifizieren Fenster, wenn das Prüfungsbild heruntergeladen wurde.
     *
     * @Author Jakob
     * @param ActionEvent e
     */


    @FXML
    public void onBewertenClicked(ActionEvent e) {
        if (pruefungHeruntergeladen == true) {

            VerifizierenView vView = new VerifizierenView();
            Stage st = (Stage) (btnBack.getScene().getWindow());
            Scene scene = new Scene(vView.getView());
            st.setScene(scene);
            st.show();

        }
        else {
            Alert al1 = new Alert(Alert.AlertType.ERROR, "Prüfung muss vor dem Bewerten\nheruntergeladen werden", ButtonType.OK);
            al1.show();
        }
    }
}
