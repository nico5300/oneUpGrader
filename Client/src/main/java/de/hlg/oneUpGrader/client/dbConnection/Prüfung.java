package de.hlg.oneUpGrader.client.dbConnection;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * Created by Michael on 20.05.2017.
 */
public class Prüfung {
    private String fach;
    private String lehrer;
    private int jahrgangsstufe;
    private Date datum;
    private boolean art;
    private String beschreibung;
    private BufferedImage bild;

    public Prüfung(String fach, String lehrer, int jahrgangsstufe,
                   Date datum, boolean art, String beschreibung, BufferedImage img) {
        this.fach = fach;
        this.lehrer = lehrer;
        this.jahrgangsstufe = jahrgangsstufe;
        this.datum = datum;
        this.art = art;
        this.beschreibung = beschreibung;
        this.bild = img;
    }

    public void setFach(String x)
    {
        fach = x;
    }

    public void setLehrer(String x)
    {
        lehrer = x;
    }

    public void setBeschreibung(String x)
    {
        beschreibung = x;
    }

    public void setJahrgangsstufe(int x)
    {
        jahrgangsstufe = x;
    }

    public void setDatum(Date x)
    {
        datum = x;
    }

    public void setArt(boolean x)
    {
        art = x;
    }

    public void setBild(BufferedImage x)
    {
        bild = x;
    }

    public String getFach()
    {
        return fach;
    }

    public String getLehrer()
    {
        return lehrer;
    }

    public int getJahrgangsstufe()
    {
        return jahrgangsstufe;
    }

    public Date getDatum()
    {
        return datum;
    }

    public boolean getArt()
    {
        return art;
    }

    public String getBeschreibung()
    {
        return beschreibung;
    }

    public BufferedImage getBild()
    {
        return bild;
    }

}
