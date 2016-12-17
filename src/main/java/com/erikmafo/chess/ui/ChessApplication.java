package com.erikmafo.chess.ui;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.move.Moves;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import com.erikmafo.chess.search.*;
import com.erikmafo.chess.utils.parser.FenParseException;
import com.erikmafo.chess.utils.parser.FenParser;
import com.erikmafo.chess.search.evaluation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by erikmafo on 13.11.16.
 */
public class ChessApplication extends Application {

    public static final int SQUARE_SIZE = 60;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;


    private Board board;

    public ChessApplication() {
        try {
            board = new FenParser().parse(FenParser.START_POSITION);
        } catch (FenParseException e) {
            e.printStackTrace();
        }
    }

    private final Group squares = new Group();
    private final Group pieceGroup = new Group();

    private final Map<Square, PieceEntry> entries = new HashMap<>();
    private final Map<Square, ImageView> pieceImageViews = new HashMap<>();
    private final DragPiece dragPiece = new DragPiece();

    private final Stack<Move> playedMoves = new Stack<>();

    private PieceColor playerColor = PieceColor.WHITE;

    private Map<PieceColor, Map<PieceType, Image>> pieceImages = new HashMap<>();
    private Task<Move> calculateComputerMoveTask;

    private final Object mutex = new Object();

    private PieceColor getCurrentColor() {
        synchronized (mutex) {
            return board.getColorToMove();
        }
    }


    private void loadPieceImages() {

        pieceImages.put(PieceColor.BLACK, new HashMap<>());
        pieceImages.put(PieceColor.WHITE, new HashMap<>());

        String[] pieceColorNames = {"black", "white"};
        String[] pieceTypeNames = {"Pawn", "Bishop", "Knight", "Rook", "Queen", "King"};

        for (String colorName : pieceColorNames) {
            for (String pieceTypeName : pieceTypeNames) {

                String imageFile = "/" + colorName + pieceTypeName + ".png";
                Image image = new Image(getClass().getResourceAsStream(imageFile));
                PieceColor color = PieceColor.valueOf(colorName.toUpperCase());
                PieceType pieceType = PieceType.valueOf(pieceTypeName.toUpperCase());
                pieceImages.get(color).put(pieceType, image);

            }
        }
    }


    public static void main(String[] args) {
        Application.launch(ChessApplication.class);
    }


    private Parent createContent() {

        Pane root = new Pane();

        root.setPrefSize(SQUARE_SIZE * WIDTH, SQUARE_SIZE * (HEIGHT + 0.5));

        Button undoButton = createUndoButton();

        undoButton.setPrefSize(SQUARE_SIZE * WIDTH, SQUARE_SIZE * 0.5);

        undoButton.relocate(0, SQUARE_SIZE * HEIGHT);

        Pane boardViewPane = new Pane();

        boardViewPane.setPrefSize(SQUARE_SIZE * WIDTH, SQUARE_SIZE * HEIGHT);

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                boolean light = (i + j) % 2 == 0;
                SquareView squareView = new SquareView(light, i, j);
                squares.getChildren().add(squareView);
            }
        }

        createPieces();

        boardViewPane.getChildren().addAll(squares, pieceGroup);

        root.getChildren().addAll(boardViewPane, undoButton);

        return root;

    }

    private Button createUndoButton() {
        Button undoButton = new Button();

        undoButton.setText("Undo");

        undoButton.setOnAction(event -> {

            if (calculateComputerMoveTask != null) {
                calculateComputerMoveTask.cancel(true);
            }

            undo();

        });
        return undoButton;
    }


    private void updateBoardView() {

        for (Square sq : Square.values()) {
            updateSquareView(sq);
        }

    }


    private void removePieceImage(Square square) {
        PieceEntry entry = entries.remove(square);
        if (entry != null) {
            pieceGroup.getChildren().remove(entry.getPieceImageView());
        }

    }

    private void setPieceImageView(Square sq, PieceColor color, PieceType pieceType) {


        boolean update = true;

        if (entries.containsKey(sq)) {

            PieceEntry pieceEntry = entries.get(sq);

            if (board.pieceAt(sq)
                    .filter(piece -> piece.is(pieceEntry.getPieceColor(), pieceEntry.getPieceType()))
                    .isPresent()) {
                update = false;
            } else {
                removePieceImage(sq);
            }
        }


        if (update) {
            ImageView pieceImageView = createPieceImageView(sq, color, pieceType);

            pieceGroup.getChildren().add(pieceImageView);

            PieceEntry entry = new PieceEntry(color, pieceType, pieceImageView);

            entries.put(sq, entry);
        }


    }

    private void updateSquareView(Square sq) {

        if (board.isOccupied(sq)) {

            Piece piece = board.getNullablePiece(sq);

            setPieceImageView(
                    sq,
                    piece.getColor(),
                    piece.getType());
        } else {
            setEmtpy(sq);
        }

        board.pieceAt(sq).ifPresent(
                piece -> setPieceImageView(sq, piece.getColor(), piece.getType()));
    }


    private void createPieces() {

        loadPieceImages();

        for (Square sq : Square.values()) {

            if (board.isOccupied(sq)) {

                Piece piece = board.getNullablePiece(sq);
                PieceType type = piece.getType();
                PieceColor color = piece.getColor();

                ImageView pieceView = createPieceImageView(sq, color, type);

                PieceEntry pieceEntry = new PieceEntry(color, type, pieceView);

                entries.put(sq, pieceEntry);

                pieceImageViews.put(sq, pieceView);

                pieceGroup.getChildren().add(pieceView);
            }
        }
    }


    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private ImageView createPieceImageView(Square sq, PieceColor pieceColor, PieceType pieceType) {

        ImageView pieceView = new ImageView(getPieceImage(pieceColor, pieceType));
        pieceView.setFitHeight(SQUARE_SIZE);
        pieceView.setFitWidth(SQUARE_SIZE);
        pieceView.relocate(SQUARE_SIZE * sq.getFile(), SQUARE_SIZE * (7 - sq.getRank()));

        pieceView.setOnMousePressed(this::handleMousePressed);
        pieceView.setOnMouseReleased(event -> handleMouseReleased(pieceView, event));
        pieceView.setOnMouseDragged(event -> handleMouseDragged(pieceView, event));

        return pieceView;
    }

    private void handleMouseDragged(ImageView pieceView, MouseEvent event) {
        pieceView.relocate(event.getSceneX() - SQUARE_SIZE / 2, event.getSceneY() - SQUARE_SIZE / 2);
    }

    private void handleMousePressed(MouseEvent event) {
        int x = (int) event.getSceneX();
        int y = (int) event.getSceneY();

        int file = findFile(x);
        int rank = findRank(y);

        Square square = Square.valueOf(file, rank);

        dragPiece.setSquare(square);

        dragPiece.setIsSelected(true);
    }


    private void handleMouseReleased(ImageView pieceView, MouseEvent event) {

        int x = (int) event.getSceneX();
        int y = (int) event.getSceneY();

        int file = findFile(x);
        int rank = findRank(y);

        Square from = dragPiece.getCoordinate();

        if (file < 0 || file > 7 || rank < 0 || rank > 7) {
            pieceView.relocate(findX(from.getFile()), findY(from.getRank()));
            return;
        }

        Square to = Square.valueOf(file, rank);


        boolean acceptMove = false;

        if (playerColor.equals(getCurrentColor())) {

            Move move = Moves.valueOf(board, from, to);

            if (move != null) {

                acceptMove = true;

                playMove(move);

                calculateComputerMoveTask = new Task<Move>() {
                    @Override
                    protected Move call() throws Exception {

                        BoardToIntFunction evaluation = new BoardToIntFunctionChain.Builder()
                                .addFunction(new MaterialBoardEvaluation())
                                .addFunction(new CastlingEvaluation())
                                //.addFunction(new MobilityEvaluation())
                                .addFunction(new PositionalEvaluation())
                                .build();

                        IterativeDeepening iterativeDeepening =
                                new IterativeDeepening(board, new AlphaBetaSearch(), evaluation, Clock.systemUTC());

                        SearchResult result = null;
                        try {
                            result = iterativeDeepening.execute(7, 10, TimeUnit.SECONDS);
                        } catch (RuntimeException ex) {
                            Logger.getLogger(ChessApplication.class.getName()).log(Level.SEVERE, null, ex);
                            throw ex;
                        }
                        return result.getBestMove();
                    }
                };


                calculateComputerMoveTask.setOnSucceeded(successEvent -> {
                    Move computerMove = (Move) successEvent.getSource().getValue();
                    playMove(computerMove);
                });


                calculateComputerMoveTask.setOnFailed(ev -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Unfortunately the application has crashed");
                    alert.setOnCloseRequest(e -> terminate());
                    alert.showAndWait();
                });

                executorService.submit(calculateComputerMoveTask);
            }
        }

        if (!acceptMove) {
            pieceView.relocate(findX(from.getFile()), findY(from.getRank()));
        }

    }


    private void undo() {
        if (!playedMoves.empty()) {
            Move lastMove = playedMoves.pop();
            lastMove.undo();
            updateBoardView();
        }
    }

    private void playMove(Move move) {
        playedMoves.push(move);
        move.play();
        updateBoardView();
    }

    private int findX(int file) {
        return file * SQUARE_SIZE;
    }

    private int findY(int rank) {
        return 7 * SQUARE_SIZE - rank * SQUARE_SIZE;
    }

    private int findFile(int x) {
        return x / SQUARE_SIZE;
    }

    private int findRank(int y) {
        return (8 * SQUARE_SIZE - y) / SQUARE_SIZE;
    }

    private Image getPieceImage(PieceColor pieceColor, PieceType pieceType) {
        return pieceImages.get(pieceColor).get(pieceType);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Chess Application");

        Scene scene = new Scene(createContent());

        primaryStage.setScene(scene);

        primaryStage.show();

        primaryStage.setOnCloseRequest(t -> {

            terminate();
        });

    }

    private void terminate() {
        Logger.getLogger(ChessApplication.class.getName()).log(Level.INFO, "Terminating the application...");

        executorService.shutdown();

        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdownNow();

        Platform.exit();
    }


    public void setEmtpy(Square sq) {

        if (entries.containsKey(sq)) {

            removePieceImage(sq);

        }

    }
}
