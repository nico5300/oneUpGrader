package de.hlg.oneUpGrader.client.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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

    private int prüfungsID;
    private int prüfungsTyp; // 1 = Klausur, Schux (großer Nachweis); 0 = Ex, Test... (kleiner Nachweis); keine enums weil man die Dinger nicht in MySQL reinkriegt
    private Date datum = Calendar.getInstance().getTime();


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
        lbPreis.setText(Integer.toString(kosten));
    }

    @FXML
    public void onButtonClicked(ActionEvent e) {
        btnKaufen.getStyleClass().remove("kaufen-button");
        btnKaufen.getStyleClass().add("herunterladen-button");
    }
}
