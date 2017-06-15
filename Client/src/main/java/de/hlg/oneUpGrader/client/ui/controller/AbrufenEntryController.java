package de.hlg.oneUpGrader.client.ui.controller;

import de.hlg.oneUpGrader.client.dbConnection.DownloadQuery;
import de.hlg.oneUpGrader.client.dbConnection.KaufenDownloadQuery;
import de.hlg.oneUpGrader.client.dbConnection.Prüfung;
import de.hlg.oneUpGrader.client.dbConnection.PunkteAbrufenQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nico on 20.04.17.
 */
public class AbrufenEntryController {
    @FXML
    private Label lbBeschreibung;

    @FXML
    private Label lbPreis;

    @FXML
    private Button btnKaufen;

    @FXML
    private Label lbInfo;

    @Inject
    private String currentUser;

    private int prüfungsID;
    private int prüfungsTyp; // 1 = Klausur, Schux (großer Nachweis); 0 = Ex, Test... (kleiner Nachweis); keine enums weil man die Dinger nicht in MySQL reinkriegt
    private Date datum = Calendar.getInstance().getTime();
    private boolean gekauft = false;


    public void setPrüfungsID(int prüfungsID) {
        this.prüfungsID = prüfungsID;
    }

    public void setPrüfungsTyp(int prüfungsTyp) {
        this.prüfungsTyp = prüfungsTyp;
        onUpdate();
    }

    public void setDatum(Date datum) {
        this.datum = datum;
        onUpdate();
    }

    public void setGekauft(boolean b) {
        gekauft = b;
        onUpdate();
    }

    // ein Updatehandler für Arme... Sorgt dafür, dass die View bei Änderung der Infos angepasst wird
    private void onUpdate() {
        StringBuilder builder = new StringBuilder(30); // StringBuilder ist effizienter...
        int kosten;                                       // sonst gäbs ihn ja ned
        switch (prüfungsTyp) {
            case 0:
                builder.append("Kleine Prüfung ");
                kosten = 3;
                break;
            case 1:
                builder.append("Große Prüfung ");
                kosten = 5;
                break;
                default:
                    builder.append("Woah, Technology... failed!");    // Fehler... sollte nie passieren
                    kosten = 0;
        }
        builder.append("vom ");
        builder.append(DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.getDefault()).format(datum)); // Mach Datum schön...

        lbBeschreibung.setText(builder.toString());

        if (gekauft) {      // Wenn Prüfg. bereits gekauft, View anpassen
            lbPreis.setText("");
            lbInfo.setText("Bereits Gekauft");
            lbInfo.translateXProperty().set(-50.0);
            btnKaufen.setText("Download");
            btnKaufen.translateXProperty().set(-35.0);
            btnKaufen.setFont(new Font(17.0));

        } else {
            lbPreis.setText(Integer.toString(kosten));
        }
    }

    /**
     * Diese Methode unterscheidet zunächst ob die Prüfung schon gekauft ist oder ned. Ist sie noch nicht gekauft, so wird die
     * KaufenDownloadQuery verwendet. Ist sie schon gekauft, so wird die DownloadQuery verwendet. Dadurch wird die Prüfung in der
     * Datenbank für den jeweiligen Anwender dann als gekauft gemeldet wenn sie noch nicht gekauft wurde und der Punktestand ausreicht.
     * Ist die Prüfung dann gekauft, kommt es zu einem Download.
     *
     * @Author Jakob
     * @param ActionEvent e
     */
    @FXML
    public void onButtonClicked(ActionEvent e) {
        if (!gekauft) {             //wenn die Prüfung vom User noch nicht gekauft wurde
            PunkteAbrufenQuery pq = new PunkteAbrufenQuery(currentUser);


            pq.setOnSucceeded((event) -> {
                int punktestand = pq.getValue();                    //Abrufen des Punktestands

                if (prüfungsTyp == 1) {                                 //wenn es sich um einen großen Leistungsnachweis handelt

                    if (punktestand < 5) {                              //wenn der User zu wenig Punkte hat für einen grpßen Leistungsnachweis
                        Alert al = new Alert(Alert.AlertType.ERROR, "Punktestand zu niedrig!", ButtonType.OK);
                        al.show();
                        return; // Unnötig (ist letzte Anweisung einer void Methode) , aber leichter lesbar
                    } else {                                            //wenn er ausreichend Punkte hat
                        KaufenDownloadQuery kdq = new KaufenDownloadQuery(currentUser, prüfungsID);

                        kdq.setOnSucceeded((event1) -> {

                            FileChooser fc = new FileChooser();
                            fc.setInitialFileName("Prüfung.png");
                            fc.getExtensionFilters().addAll(
                                    new FileChooser.ExtensionFilter("PNG", "*.png"));

                            Prüfung p = kdq.getValue();                 //Abrufen der angeforderten Prüfung
                            BufferedImage img = p.getBild();            //auslesen der Bilddatei aus der Prüfung

                            File file = fc.showSaveDialog(btnKaufen.getScene().getWindow());         //anzeigen des Speicherdialogs; bekommen des Speicherorts

                            try {
                                ImageIO.write(img, "PNG", file);                          //speichern der Datei
                            } catch (IOException r) {
                                Alert al1 = new Alert(Alert.AlertType.ERROR, "Format Exception!", ButtonType.OK);
                                al1.show();
                                return;
                            }

                        });

                        kdq.execute();

                    }
                } else {                                                //wenn es sich um einen kleinen Leistungsnachweis handelt

                    if (punktestand < 3) {                              //wenn der User zu wenig Punkte hat für einen kleinen Leistungsnachweis
                        Alert al2 = new Alert(Alert.AlertType.ERROR, "Punktestand zu niedrig!", ButtonType.OK);
                        al2.show();
                        return;
                    } else {                                            //wenn er ausreichend Punkte hat
                        KaufenDownloadQuery kdq = new KaufenDownloadQuery(currentUser, prüfungsID);

                        kdq.setOnSucceeded((event1) -> {

                            FileChooser fc = new FileChooser();
                            fc.setInitialFileName("Prüfung.png");
                            fc.getExtensionFilters().addAll(
                                    new FileChooser.ExtensionFilter("PNG", "*.png"));

                            Prüfung p = kdq.getValue();                 //Abrufen der angeforderten Prüfung
                            BufferedImage img = p.getBild();            //auslesen der Bilddatei aus der Prüfung

                            File file = fc.showSaveDialog(btnKaufen.getScene().getWindow());         //anzeigen des Speicherdialogs; bekommen des Speicherorts

                            try {
                                ImageIO.write(img, "PNG", file);                          //speichern der Datei
                            } catch (IOException r) {
                                Alert al3 = new Alert(Alert.AlertType.ERROR, "Format Exception!", ButtonType.OK);
                                al3.show();
                                return;
                            }

                        });

                        kdq.execute();
                    }
                }
            });

            pq.execute();

        }
        else {              //wenn der User die Prüfung bereits gekauft hat
            DownloadQuery dq = new DownloadQuery(prüfungsID);

            dq.setOnSucceeded((event2) -> {
                FileChooser fc = new FileChooser();
                fc.setInitialFileName("Prüfung.png");
                fc.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("PNG", "*.png"));

                Prüfung p = dq.getValue();                 //Abrufen der angeforderten Prüfung
                BufferedImage img = p.getBild();           //auslesen der Bilddatei aus der Prüfung

                File file = fc.showSaveDialog(btnKaufen.getScene().getWindow());         //anzeigen des Speicherdialogs; bekommen des Speicherorts

                try {
                ImageIO.write(img, "PNG", file);                              //speichern der Datei
                } catch (IOException r) {
                    Alert al4 = new Alert(Alert.AlertType.ERROR, "Format Exception!", ButtonType.OK);
                    al4.show();
                    return;
                }
            });
        }
    }
}
