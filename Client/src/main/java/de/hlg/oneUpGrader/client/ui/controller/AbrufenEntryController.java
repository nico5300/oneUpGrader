package de.hlg.oneUpGrader.client.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

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
                    builder.append("Woah, Technology failed!!");    // Fehler... sollte nie passieren
                    kosten = 0;
        }
        builder.append("vom ");
        builder.append(DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.getDefault()).format(datum)); // Mach Datum schön...

        lbBeschreibung.setText(builder.toString());

        if (gekauft) {
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

    @FXML
    public void onButtonClicked(ActionEvent e) {

    }
}
