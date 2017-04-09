package de.hlg.oneUpGrader.client;

/**
 * Created by nico on 08.04.17.
 */

import de.hlg.oneUpGrader.client.ui.view.MainWindowView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    private String ver = "0.0.1 <TEST>";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainWindowView mView = new MainWindowView();

        primaryStage.setWidth(1000);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("OneUpGrader " + ver);
        Scene scene = new Scene(mView.getView());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
