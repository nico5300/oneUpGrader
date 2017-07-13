package de.hlg.oneUpGrader.client.dbConnection;

import javafx.concurrent.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Michael on 12.05.2017.

    Übergabe von Username, PrüfungsID und Ergebnis
    Keine Rückgabe

    Methode fügt Eintrag zu Tabelle verifiziert
    Methode überprüft, ob nun bereits dreimal verifiziert
    Falls ja, Methode löscht alles aus verifiziert Tabelle
    UND trägt bei Prüfung ein ob Prüfung passt oder nicht
    Verifizierer bekommt immer 2 Punkte
    Falls Prüfung passt bekommt Autor der Prüfung Punkte (Klausur 5, Ex 3)
 */
public class VerifizierenErgebnisSendenQuery extends Task<Void> {

    String query1 = "INSERT INTO Verifiziert (Email, PrüfungsID, Bewertung) VALUES (?, ?, ?)";
    String query2 = "SELECT * FROM Verifiziert WHERE PrüfungsID = ?";
    String query3 = "DELETE FROM Verifiziert WHERE PrüfungsID = ?";
    String query4 = "UPDATE Prüfungen SET Verifiziert = ? WHERE PrüfungsID = ?";
    String query44 = "SELECT Punkte FROM Anwender WHERE Email = ?";
    String query45 = "UPDATE Anwender SET Punkte = ? WHERE Email = ?";
    String query5 = "SELECT Art, AutorID FROM Prüfungen WHERE PrüfungsID = ?";
    String query6 = "SELECT Punkte FROM Anwender WHERE Email = ?";
    String query7 = "UPDATE Anwender SET Punkte = ? WHERE Email = ?";
    

    Boolean erg1 = false;
    Boolean erg2 = false;
    Boolean erg3 = false;

    String email;
    int TestID;
    Boolean ergebnis;
    Boolean TestArt;
    Boolean PVer;
    int Pk;
    int Pkv;

    public VerifizierenErgebnisSendenQuery(String e, int ID, boolean er) {
        email = e;
        TestID = ID;
        ergebnis = er;
        TestArt = false;
        AutorID = false;
        Pver = false;
        Pk = 0;
        Pkv = 0;
    }

    protected Void call() throws SQLException {
        DbConnection datenbank = DbConnection.getInstance();

        Optional<PreparedStatement> opt = datenbank.getPreparedStatement(query1);
        PreparedStatement queryComplete = opt.get();

        queryComplete.setString(1, email);
        queryComplete.setInt(2, TestID);
        queryComplete.setBoolean(3, ergebnis);

        try {
            queryComplete.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Optional<PreparedStatement> opt2 = datenbank.getPreparedStatement(query2);
        PreparedStatement queryComplete2 = opt2.get();

        queryComplete2.setInt(1, TestID);

        try {
            queryComplete2.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet ergebnis2 = queryComplete2.getResultSet();

        if (ergebnis2.next()) {
            erg1 = ergebnis2.getBoolean(1);
            if (ergebnis2.next()) {
                erg2 = ergebnis2.getBoolean(1);
                if (ergebnis2.next()) {
                    erg3 = ergebnis2.getBoolean(1);


                    Optional<PreparedStatement> opt3 = datenbank.getPreparedStatement(query3);
                    PreparedStatement queryComplete3 = opt3.get();

                    queryComplete3.setInt(1, TestID);

                    try {
                        queryComplete3.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Optional<PreparedStatement> opt4 = datenbank.getPreparedStatement(query4);
                    PreparedStatement queryComplete4 = opt4.get();

                    if ((erg1 && erg2) || (erg1 && erg3) || (erg2 && erg3)) {
                        queryComplete4.setBoolean(1, true);
                        Pver = true;
                        
                    } else {
                        queryComplete4.setBoolean(1, false);
                    }
                    queryComplete4.setInt(2, TestID);

                    try {
                        queryComplete4.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    
                    
                    //Punkte des Verifizierers holen
                    Optional<PreparedStatement> opt44 = datenbank.getPreparedStatement(query44);
                    PreparedStatement queryComplete44 = opt44.get();

                    queryComplete44.setString(email);
                    
                    try {
                        queryComplete44.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    ResultSet ergebnis44 = queryComplete44.getResultSet();
                    
                    if(ergebnis44.next())
                    {
                       Pkv = ergebnis44.getInt("Punkte");     
                    }
                    Pkv = Pkv + 2;
                    
                    
                    //Punkte des Verifizierers Updaten
                    Optional<PreparedStatement> opt45 = datenbank.getPreparedStatement(query45);
                    PreparedStatement queryComplete45 = opt45.get();
                    
                    queryComplete45.setInt(Pkv);
                    queryComplete45.setString(email);
                    
                    try {
                        queryComplete45.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    
                    
                    
                    //Hierfür neues Attribut AutorID mit email des Autors der Prüfung in Datenbank Tabelle Prüfungen notwendig!
                    //Autor der Prüfung bekommt seine Punkte
                    if(Pver)
                    {
                        Optional<PreparedStatement> opt5 = datenbank.getPreparedStatement(query5);
                        PreparedStatement queryComplete5 = opt5.get();
                    
                        queryComplete5.setInt(TestID);
                    
                        try {
                            queryComplete5.execute();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        ResultSet ergebnis5 = queryComplete5.getResultSet();    
                        if(ergebnis5.next())
                        {
                            TestArt = ergebnis5.getBoolean("Art");
                            AutorID = ergebnis5.getString("AutorID");
                        }

                    
                        Optional<PreparedStatement> opt6 = datenbank.getPreparedStatement(query6);
                        PreparedStatement queryComplete6 = opt6.get();
                        queryComplete6.setString(AutorID);
                        
                        try {
                        queryComplete6.execute();
                        } catch (SQLException e) {
                        e.printStackTrace();
                        }
                        ResultSet ergebnis6 = queryComplete6.getResultSet(); 
                        
                        if(ergebnis6.next())
                        {
                            Pk = ergebnis6.getInt("Punkte");
                        }
                        
                        if(TestArt)
                        {
                            Pk = Pk + 5;
                        }
                        else 
                        {
                            PK = Pk + 3;
                        }  
                        
                        Optional<PreparedStatement> opt7 = datenbank.getPreparedStatement(query7);
                        PreparedStatement queryComplete7 = opt7.get();
                        queryComplete7.setInt(Pk);
                        queryComplete7.setString(AutorID);
                        
                        try {
                        queryComplete7.execute();
                        } catch (SQLException e) {
                        e.printStackTrace();
                        }
                        
                    }    
                }
            }
        }                                                                   // Wenn Prüfung dreimal verifiziert
        return null;
    }

    public void execute() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
