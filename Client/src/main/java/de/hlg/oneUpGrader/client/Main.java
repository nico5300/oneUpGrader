package de.hlg.oneUpGrader.client;

/**
 * Created by nico on 08.04.17.
 */

import com.airhacks.afterburner.injection.Injector;
import de.hlg.oneUpGrader.client.dbConnection.DbConnection;
import de.hlg.oneUpGrader.client.ui.view.AnmeldenView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;


public class Main extends Application {
    private String ver = "0.0.3 <TEST>";

    public static void main(String[] args) {
        launch(args);
    }
    private DbConnection d;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Starte Anwendung");
        d = DbConnection.getInstance();

        HashMap<String, Object> injectionMap = new HashMap<>();
        injectionMap.put("injectionMap", injectionMap);
        Injector.setConfigurationSource(injectionMap::get); // It's a kind of magic...

//        MainWindowView mView = new MainWindowView();
//        primaryStage.setResizable(false);
//        primaryStage.setTitle("OneUpGrader " + ver);
//        Scene scene = new Scene(mView.getView());
//        primaryStage.setScene(scene);
//        primaryStage.show();

        AnmeldenView aView = new AnmeldenView();
        primaryStage.setResizable(false);
        primaryStage.setTitle("OneUpGrader " + ver);
        Scene scene = new Scene(aView.getView());
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    @Override
    public void stop() {
        d.closeConnection();
    }
}
