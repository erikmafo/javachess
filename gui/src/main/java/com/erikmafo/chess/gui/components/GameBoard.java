package com.erikmafo.chess.gui.components;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by erikmafo on 01.01.17.
 */
public class GameBoard extends Pane {

    private final DoubleProperty squareSizeProperty = new SimpleDoubleProperty();

    private EventHandler<GameBoardEvent> squareClickedHandler;

    @FXML
    private Pane content;


    public GameBoard() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game_board.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        squareSizeProperty.bind(
                Bindings.min(content.widthProperty(), content.heightProperty()).divide(8)
        );

        content.setOnMouseClicked(event -> {
            squareClickedHandler.handle(new GameBoardEvent(
                    GameBoardEvent.SQUARE_CLICKED,
                    getFileIndex(event.getX()),
                    getRankIndex(event.getY())
                    ));
        });
    }

    public void put(BoardLocation boardLocation, ImageView node) {
        content.getChildren().add(node);
        node.layoutXProperty().bind(fileCoordinate(boardLocation.getFile()));
        node.layoutYProperty().bind(rankCoordinate(boardLocation.getRank()));
        node.fitHeightProperty().bind(squareSizeProperty);
        node.fitWidthProperty().bind(squareSizeProperty);
    }

    public void removeFromBoard(Node node) {
        content.getChildren().remove(node);
    }

    public Pane getContent() {
        return content;
    }

    public void setContent(Pane content) {
        this.content = content;
    }

    private DoubleBinding rankCoordinate(int rank) {
        return layoutYProperty().add(squareSizeProperty.multiply(7 - rank));
    }

    private DoubleBinding fileCoordinate(int file) {
        return layoutXProperty().add(squareSizeProperty.multiply(file));
    }

    private double getX(int file) {
        return getSquareSize() * file;
    }

    private double getY(int rank) {
        return getSquareSize() * (7 - rank);
    }


    private int getFileIndex(double x) { return (int) (x / getSquareSize()); }

    private int getRankIndex(double y) {
        return 7 - (int) (y / getSquareSize());
    }


    public double getSquareSize() {
        return squareSizeProperty.get();
    }

    public ReadOnlyDoubleProperty squareSizePropertyProperty() {
        return squareSizeProperty;
    }


    public double getSquareSizeProperty() {
        return squareSizeProperty.get();
    }

    public void setSquareSizeProperty(double squareSizeProperty) {
        this.squareSizeProperty.set(squareSizeProperty);
    }

    public EventHandler<GameBoardEvent> getOnSquareClicked() {
        return squareClickedHandler;
    }

    public void setOnSquareClicked(EventHandler<GameBoardEvent> squareClickedHandler) {
        this.squareClickedHandler = squareClickedHandler;
    }
}
