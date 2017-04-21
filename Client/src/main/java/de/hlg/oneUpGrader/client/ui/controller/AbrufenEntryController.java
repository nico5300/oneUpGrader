package de.hlg.oneUpGrader.client.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nico on 20.04.17.
 */
public class AbrufenEntryController {
    @FXML
    private Label lbBeschreibung;

    @FXML
    private Label lbPreis;

    @FXML
    private Button btnKaufen;

    private int prüfungsID;
    private int prüfungsTyp; // 1 = Klausur, 2 = Schulaufgabe, 3 = Test, 4 = Ex
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
        StringBuilder builder = new StringBuilder(30);
        int kosten;
        switch (prüfungsTyp) {
            case 1:
                builder.append("Klausur ");
                kosten = 5;
                break;
            case 2:
                builder.append("Schulaufgabe ");
                kosten = 5;
                break;
            case 3:
                builder.append("Test ");
                kosten = 4;
                break;
            case 4:
                builder.append("Ex ");
                kosten = 3;
                break;
            default:
                kosten = 0;
        }
        builder.append("vom ");
        builder.append(DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.getDefault()).format(datum));

        lbBeschreibung.setText(builder.toString());
        lbPreis.setText(Integer.toString(kosten));
    }
}
