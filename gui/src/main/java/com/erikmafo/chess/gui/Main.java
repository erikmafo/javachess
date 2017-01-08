package com.erikmafo.chess.gui;

import com.erikmafo.chess.gui.controller.Controller;
import com.erikmafo.chess.gui.model.ChessEngine;
import com.erikmafo.chess.gui.utils.FXControllerFactory;
import com.erikmafo.chess.utils.parser.FenParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.Executors;

/**
 * Created by erikmafo on 27.12.16.
 */
public class Main extends Application {

    private FXControllerFactory fxControllerFactory = new FXControllerFactory(getContext());

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlLoader.setControllerFactory(fxControllerFactory);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    private Map<String, Object> getContext() {

        Map<String, Object> context = new HashMap<>();

        FenParser fenParser = new FenParser();
        ChessEngine chessEngine = new ChessEngine(fenParser, Executors.newSingleThreadExecutor());

        context.put("engine", chessEngine);
        context.put("startPositionFen", FenParser.START_POSITION);

        return context;
    }


    @Override
    public void stop() {
        fxControllerFactory.destroy();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

