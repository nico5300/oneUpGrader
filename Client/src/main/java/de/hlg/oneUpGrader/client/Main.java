package de.hlg.oneUpGrader.client;

/**
 * Created by nico on 08.04.17.
 *
 * Hier ist die Main Methode zuhause... Die Wurzel allen Übels
 */

import com.airhacks.afterburner.injection.Injector;
import de.hlg.oneUpGrader.client.dbConnection.DbConnection;
import de.hlg.oneUpGrader.client.ui.view.AnmeldenView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;


public class Main extends Application {
    private String ver = "0.0.5 <TEST>";


    public static void main(String[] args) {
        launch(args);
    }
    private DbConnection d;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Starte Anwendung");
        d = DbConnection.getInstance();

        // Initialisierung des Injectors, indem die .get(key):value Methode einer Hashmap als zu benutzende
        // Funktion festgelegt wird.

        // .get bei der Hashmap funktioniert ja wie eine mathematische Funktion f(x)=irgendwas....
        // Man gibt ihr einen x-Wert mit und hinten kommt ein y-Wert raus. Eine Funktion dieser Art erwartet
        // Injector.setConfigurationSource(), damit sie vom Injector gefragt werden kann, ob sie ein Objekt
        // zu dem Namen der Variable hat, die im Moment vom Injector befüllt werden soll, wenn er auf ein
        // @Inject trifft. Ist in der Hashmap ein passendes Objekt gespeichert, wird es zurückgegeben und
        // injeziert, wenn nicht, wird versucht, der Standardkonstruktor aufzurufen...
        // Ist der Standardkonstruktor private,(bsp: ObservableList) geht das natürlich nicht und es kommt
        // zu einer RuntimeException, die nur schwer zu finden ist. Deshalb ist Testen wichtig
        HashMap<String, Object> injectionMap = new HashMap<>();
        injectionMap.put("injectionMap", injectionMap);
        Injector.setConfigurationSource(injectionMap::get); // It's a kind of magic...

        // Initialisieren und anzeigen der AnmeldenView
        AnmeldenView aView = new AnmeldenView();
        primaryStage.setResizable(false);
        primaryStage.setTitle("OneUpGrader " + ver);
        Scene scene = new Scene(aView.getView());
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    // Die Datenbankverbindung ordnungsgemäß beenden
    @Override
    public void stop() {
        d.closeConnection();
    }
}
