package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.dbConnection.Prüfung;
import de.hlg.oneUpGrader.client.dbConnection.VerifizierenErgebnisSendenQuery;
import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Jakob/Miran on 08.05.2017.
 */
public class VerifizierenController {

    @Inject
    private HashMap<String, Object> injectionMap;

    @Inject
    private Prüfung VerifizierenPruefung;

    @Inject
    private String currentUser;

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

    /**
     * @Author Jakob
     *
     * Konstruktor von VerifizerienController; füllt Labels mit Informationen der zu verifizierenden Prüfung
     *
     *
     */

    public VerifizierenController() {
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
     * Diese Methode erstellt eine VerifizierenErgebnisSendenQuery, die die zu Verifizierende PrÜfung als Mangelhaft bewertet.
     * Anschließend wird der Bilschirm zur MainWindow gewechselt.
     *
     * @Author Jakob
     * @param ActionEvent e
     */

    @FXML
    public void onMangelhaftClick(ActionEvent e) {

        VerifizierenErgebnisSendenQuery vesq = new VerifizierenErgebnisSendenQuery(currentUser,VerifizierenPruefung.getPrüfungsID(),false);

        vesq.setOnSucceeded((event) -> {
            injectionMap.remove("VerifizierenPruefung");

            MainWindowView mView = new MainWindowView();
            Stage stage = (Stage) (labelArt.getScene().getWindow());
            Scene scene = new Scene(mView.getView());
            stage.setScene(scene);
            stage.show();
        });

    }

    /**
     * Diese Methode erstellt eine VerifizierenErgebnisSendenQuery, die die zu Verifizierende PrÜfung als Mangelhaft bewertet.
     * Anschließend wird der Bilschirm zur MainWindow gewechselt.
     *
     * @Author Jakob
     * @param ActionEvent e
     */

    @FXML
    public void onInOrdnungClick(ActionEvent e) {

        VerifizierenErgebnisSendenQuery vesq = new VerifizierenErgebnisSendenQuery(currentUser,VerifizierenPruefung.getPrüfungsID(),true);

        vesq.setOnSucceeded((event) -> {
            injectionMap.remove("VerifizierenPruefung");

            MainWindowView mView = new MainWindowView();
            Stage stage = (Stage) (labelArt.getScene().getWindow());
            Scene scene = new Scene(mView.getView());
            stage.setScene(scene);
            stage.show();
        });
    }
}
