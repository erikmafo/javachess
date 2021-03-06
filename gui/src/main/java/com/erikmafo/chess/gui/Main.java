package com.erikmafo.chess.gui;

import com.erikmafo.chess.gui.model.ChessEngine;
import com.erikmafo.chess.gui.model.ChessEngineImpl;
import com.erikmafo.chess.gui.model.DefaultChessEngine;
import com.erikmafo.chess.gui.utils.FXFactory;
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

    private FXFactory fxFactory = new FXFactory(getConfig());

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlLoader.setControllerFactory(fxFactory);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private Map<String, Object> getConfig() {

        Map<String, Object> context = new HashMap<>();

        //FenParser fenParser = new FenParser();
        //ChessEngine chessEngine = new DefaultChessEngine(fenParser, Executors.newSingleThreadExecutor());

        ChessEngine chessEngine = new ChessEngineImpl();

        context.put("engine", chessEngine);
        context.put("startPositionFen", FenParser.START_POSITION);

        return context;
    }

    @Override
    public void stop() {
        fxFactory.destroy();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

