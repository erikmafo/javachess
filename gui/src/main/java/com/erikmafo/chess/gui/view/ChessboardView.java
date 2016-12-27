package com.erikmafo.chess.gui.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

/**
 * Created by erikmafo on 26.12.16.
 */
public class ChessboardView extends Region {

    private DoubleProperty boardSizeProperty = new SimpleDoubleProperty();
    private DoubleProperty squareSizeProperty = new SimpleDoubleProperty();


    private Pane content = new Pane();

    private EventHandler<ChessboardEvent> squarePressedHandler;
    private EventHandler<ChessboardEvent> squareClickedHandler;

    public ChessboardView() {
        super();

        boardSizeProperty.bind(Bindings.min(widthProperty(),heightProperty()));
        squareSizeProperty.bind(boardSizeProperty.divide(8));

        content.prefHeightProperty().bind(prefHeightProperty());
        content.prefHeightProperty().bind(prefWidthProperty());

        getChildren().add(content);

        generateSquares();

        content.setOnMousePressed(event -> {
            fireEvent(newChessboardEvent(ChessboardEvent.SQUARE_PRESSED, event));
        });

        content.setOnMouseClicked(event -> {
            fireEvent(newChessboardEvent(ChessboardEvent.SQUARE_CLICKED, event));
        });


    }


    private ChessboardEvent newChessboardEvent(EventType<ChessboardEvent> eventEventType, MouseEvent mouseEvent) {

        double x = mouseEvent.getX();
        double y = mouseEvent.getY();


        return new ChessboardEvent(mouseEvent, this, eventEventType, getFileIndex(x), getRankIndex(y));

    }




    private void generateSquares() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle square = createSquare(i, j);
                content.getChildren().add(square);
            }
        }
    }

    private Rectangle createSquare(int file, int rank) {
        Rectangle square = new Rectangle();
        square.setId("(" + file + "," + rank + ")");
        square.layoutXProperty().bind(fileCoordinate(file));
        square.layoutYProperty().bind(rankCoordinate(rank));
        square.widthProperty().bind(squareSizeProperty);
        square.heightProperty().bind(squareSizeProperty);
        String styleClass = (file + rank) % 2 == 0 ? "black-square" : "white-square";
        square.getStyleClass().addAll(styleClass, "square");
        return square;
    }


    private DoubleBinding rankCoordinate(int rank) {
        return layoutYProperty().add(squareSizeProperty.multiply(7 - rank));
    }

    private DoubleBinding fileCoordinate(int file) {
        return layoutXProperty().add(squareSizeProperty.multiply(file));
    }

    private Rectangle getSquare(int file, int rank) {
        String id = "(" + file + "," + rank + ")";
        Rectangle square = null;
        for (Node node : getChildren()) {
            if (id.equals(node.getId())) {
                square = (Rectangle) node;
                break;
            }
        }
        return square;
    }


    public double getSquareSize() {
        return squareSizeProperty.get();
    }

    public EventHandler<ChessboardEvent> getOnSquareClicked() {
        return squareClickedHandler;
    }

    public void setOnSquareClicked(EventHandler<ChessboardEvent> onSquareClicked) {
        this.squareClickedHandler = onSquareClicked;
        setEventHandler(ChessboardEvent.SQUARE_CLICKED, onSquareClicked);
    }


    public EventHandler<ChessboardEvent> getOnSquarePressed() {
        return squarePressedHandler;
    }

    public void setOnSquarePressed(EventHandler<ChessboardEvent> onSquarePressed) {
        this.squarePressedHandler = onSquarePressed;
        setEventHandler(ChessboardEvent.SQUARE_PRESSED, onSquarePressed);
    }


    public void put(int file, int rank, PieceImageView pieceImageView) {
        content.getChildren().add(pieceImageView);
        pieceImageView.relocate(getX(file), getY(rank));
        pieceImageView.fitHeightProperty().bind(squareSizeProperty);
        pieceImageView.fitWidthProperty().bind(squareSizeProperty);
    }


    public boolean remove(int file, int rank) {

        double delta = squareSizeProperty.get() / 2;

        double x = getX(file);
        double y = getY(rank);

        Optional<Node> optional = content.getChildren().stream().filter(node -> {

            if (node instanceof PieceImageView) {
                return approxEquals(x, node.getLayoutX(), delta) && approxEquals(y, node.getLayoutY(), delta);
            } else {
                return false;
            }

        }).findFirst();

        boolean res = false;

        if (optional.isPresent()) {
            res = content.getChildren().remove(optional.get());
        }

        return res;
    }

    private boolean isWithinSquare(int file, int rank, double x, double y) {
        double fileX = getX(file);
        double rankY = getY(rank);
        return x > fileX && x < fileX + getSquareSize() && y < rankY && y > rankY - getSquareSize();
    }


    private boolean approxEquals(double d1, double d2, double delta) {
        return Math.abs(d1 - d2) < delta;
    }


    private double getX(int file) {
        return getSquareSize() * file;
    }

    private double getY(int rank) {
        return getSquareSize() * (7 - rank);
    }


    private int getFileIndex(double x) {
        return (int)(x / getSquareSize());
    }

    private int getRankIndex(double y) {
        return 7 - (int)(y / getSquareSize());
    }
}
