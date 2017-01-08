package com.erikmafo.chess.gui;

import com.erikmafo.chess.gui.controller.Controller;
import com.erikmafo.chess.utils.parser.FenParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.*;

/**
 * Created by erikmafo on 27.12.16.
 */
public class Main extends Application {


    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage primaryStage) throws Exception{

        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlLoader.setResources(getResources());
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    private ResourceBundle getResources() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(Controller.START_FEN, FenParser.START_POSITION);
        return new SimpleResourceBundle(properties);
    }


    @Override
    public void stop() {

        ((Controller) fxmlLoader.getController()).shutdown();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

