package com.erikmafo.chess.gui.components;

import com.erikmafo.chess.gui.model.BoardLocation;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by erikmafo on 26.12.16.
 */
public class Chessboard extends Pane {

    private final DoubleProperty squareSizeProperty = new SimpleDoubleProperty();
    private final StringProperty colorToMoveProperty = new SimpleStringProperty();

    private final Map<BoardLocation, PieceImageView> pieceImageViewMap = new HashMap<>();

    @FXML
    private Pane content;

    private EventHandler<ChessboardEvent> squareClickedHandler;
    private EventHandler<ChessboardEvent> pieceClickedHandler;
    private EventHandler<PieceDroppedEvent> pieceDroppedHandler;

    public Chessboard() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/chessboard.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
           throw new RuntimeException(e);
        }

        squareSizeProperty.bind(Bindings.min(widthProperty(), heightProperty()).divide(8));

        content.prefHeightProperty().bind(prefHeightProperty());
        content.prefHeightProperty().bind(prefWidthProperty());

        generateSquares();

        content.setOnMouseClicked(event -> {
            squareClickedHandler.handle(newChessboardEvent(ChessboardEvent.SQUARE_CLICKED, event));
        });
    }

    private ChessboardEvent newChessboardEvent(EventType<ChessboardEvent> eventEventType, MouseEvent mouseEvent) {

        double x = mouseEvent.getX();
        double y = mouseEvent.getY();


        return new ChessboardEvent(mouseEvent, this, eventEventType, new BoardLocation(getFileIndex(x), getRankIndex(y)));
    }

    private final char[] files = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

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
        square.setId("" + files[file] + (rank + 1));
        square.layoutXProperty().bind(fileCoordinate(file));
        square.layoutYProperty().bind(rankCoordinate(rank));
        square.widthProperty().bind(squareSizeProperty);
        square.heightProperty().bind(squareSizeProperty);
        String styleClass = (file + rank) % 2 == 0 ? "black-square" : "white-square";
        square.getStyleClass().addAll(styleClass, "square");
        return square;
    }


    private DoubleBinding rankCoordinate(int rank) {
        return content.layoutYProperty().add(squareSizeProperty.multiply(7 - rank));
    }

    private DoubleBinding fileCoordinate(int file) {
        return content.layoutXProperty().add(squareSizeProperty.multiply(file));
    }


    public double getSquareSize() {
        return squareSizeProperty.get();
    }

    public EventHandler<ChessboardEvent> getOnSquareClicked() {
        return squareClickedHandler;
    }

    public void setOnSquareClicked(EventHandler<ChessboardEvent> onSquareClicked) {
        this.squareClickedHandler = onSquareClicked;
    }


    public void setOnPieceClicked(EventHandler<ChessboardEvent> onPieceClicked) {
        this.pieceClickedHandler = onPieceClicked;
    }

    public EventHandler<ChessboardEvent> getOnPieceClicked() {
        return pieceClickedHandler;
    }


    public void setOnPieceDropped(EventHandler<PieceDroppedEvent> onPieceDropped) {
        this.pieceDroppedHandler = onPieceDropped;
    }

    public EventHandler<PieceDroppedEvent> getOnPieceDropped() {
        return pieceDroppedHandler;
    }


    public String getColorToMove() {
        return colorToMoveProperty.get();
    }

    public StringProperty colorToMoveProperty() {
        return colorToMoveProperty;
    }

    public void setColorToMove(String colorToMove) {
        this.colorToMoveProperty.set(colorToMove);
    }

    public void relocate(BoardLocation from, BoardLocation to) {

        if (pieceImageViewMap.containsKey(from)) {
            PieceImageView piece = pieceImageViewMap.get(from);
            piece.layoutXProperty().bind(fileCoordinate(to.getFile()));
            piece.layoutYProperty().bind(rankCoordinate(to.getRank()));
            pieceImageViewMap.put(to, piece);
        }
    }

    /**
     * Puts the piece on the square with the specified name, e.g. "e2", "a4".
     *
     * @param square the name of the square
     * @param pieceImageView the piece to put on the specified square
     */
    public void put(String square, PieceImageView pieceImageView)  {

        Node node = findSquare(square.toLowerCase());

        if (node != null) {
            put(new BoardLocation(square), pieceImageView);
        }
    }

    public void put(BoardLocation boardLocation, PieceImageView pieceImageView) {

        PieceImageView current = pieceImageViewMap.get(boardLocation);

        assert pieceImageView != null;

        if (current != null) {
            if (current.isSamePiece(pieceImageView)) {
                return;
            }
            remove(boardLocation);
        }

        pieceImageViewMap.put(boardLocation, pieceImageView);
        content.getChildren().add(makeDraggable(pieceImageView));
        pieceImageView.layoutXProperty().bind(fileCoordinate(boardLocation.getFile()));
        pieceImageView.layoutYProperty().bind(rankCoordinate(boardLocation.getRank()));
        pieceImageView.fitHeightProperty().bind(squareSizeProperty);
        pieceImageView.fitWidthProperty().bind(squareSizeProperty);
        pieceImageView.isDraggableProperty().bind(Bindings.equal(colorToMoveProperty, pieceImageView.colorProperty()));

    }

    @Nullable
    private Node findSquare(String square) {
        Node node = null;

        for (Node child : content.getChildren()) {
            if (square.equals(child.getId())) {
                node = child;
                break;
            }
        }
        return node;
    }

    public boolean remove(BoardLocation boardLocation) {

        Node piece = pieceImageViewMap.remove(boardLocation);

        boolean removed = false;

        if (piece != null) {
            content.getChildren().remove(piece.getParent());
            removed = true;
        }

        return removed;
    }

    private int getFileIndex(double x) {
        return (int) (x / getSquareSize());
    }

    private int getRankIndex(double y) {
        return 7 - (int) (y / getSquareSize());
    }

    private Node makeDraggable(final PieceImageView piece) {
        final DragContext dragContext = new DragContext();
        final Group wrapGroup = new Group(piece);

        wrapGroup.addEventFilter(
                MouseEvent.ANY,
                mouseEvent -> mouseEvent.consume());


        wrapGroup.addEventFilter(
                MouseEvent.MOUSE_CLICKED,
                event -> {
                    pieceClickedHandler.handle(new ChessboardEvent(
                            ChessboardEvent.PIECE_CLICKED,
                            new BoardLocation(getFileIndex(piece.getLayoutX()), getRankIndex(piece.getLayoutY()))));

                });



        wrapGroup.addEventFilter(
                MouseEvent.MOUSE_PRESSED,
                mouseEvent -> {
                    // remember initial mouse cursor coordinates
                    // and node position
                    if (piece.isDraggable()) {
                        dragContext.mouseAnchorX = mouseEvent.getX();
                        dragContext.mouseAnchorY = mouseEvent.getY();
                        dragContext.initialTranslateX = piece.getTranslateX();
                        dragContext.initialTranslateY = piece.getTranslateY();
                    }

                });

        wrapGroup.addEventFilter(
                MouseEvent.MOUSE_DRAGGED,
                mouseEvent -> {
                    if (piece.isDraggable()) {
                        // shift node from its initial position by delta
                        // calculated from mouse cursor movement
                        piece.setTranslateX(
                                dragContext.initialTranslateX
                                        + mouseEvent.getX()
                                        - dragContext.mouseAnchorX);
                        piece.setTranslateY(
                                dragContext.initialTranslateY
                                        + mouseEvent.getY()
                                        - dragContext.mouseAnchorY);
                    }
                });

        wrapGroup.addEventFilter(
                MouseEvent.MOUSE_RELEASED,
                event -> {

                    if (piece.isDraggable()) {
                        int file = getFileIndex(event.getX());
                        int rank = getRankIndex(event.getY());

                        if (isValidSquare(file, rank)) {
                            PieceDroppedEvent chessboardEvent = new PieceDroppedEvent(
                                    new BoardLocation(getFileIndex(piece.getLayoutX()), getRankIndex(piece.getLayoutY())),
                                    new BoardLocation(file, rank));

                            pieceDroppedHandler.handle(chessboardEvent);

                            if (chessboardEvent.isPieceDropAccepted()) {
                                relocate(chessboardEvent.getInitialLocation(), chessboardEvent.getDropLocation());
                            }
                        }

                        piece.setTranslateX(0);
                        piece.setTranslateY(0);
                    }

                }
        );

        return wrapGroup;

    }

    private boolean isValidSquare(int file, int rank) {
        return file >=0 && file <=7 && rank >= 0 && rank <= 7;
    }

    private static final class DragContext {
        public double mouseAnchorX;
        public double mouseAnchorY;
        public double initialTranslateX;
        public double initialTranslateY;


    }
}
