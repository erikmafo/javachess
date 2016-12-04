package com.erikmafo.javachess.ui;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.Moves;
import com.erikmafo.javachess.movegenerator.BoardSeeker;
import com.erikmafo.javachess.movegenerator.MoveGenerationStrategy;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;
import com.erikmafo.javachess.search.*;
import com.erikmafo.javachess.uci.FenParseException;
import com.erikmafo.javachess.uci.FenParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by erikmafo on 13.11.16.
 */
public class ChessApplication extends Application {

    public static final int SQUARE_SIZE = 60;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;


    private final SearchExecutor searchExecutor = new SearchExecutorImpl(
            new AlphaBetaSearch(new BoardSeeker()),
            Executors.newSingleThreadExecutor());




    private Board board;

    public ChessApplication() {
        try {
            this.board = new FenParser().parse(FenParser.START_POSITION);
        } catch (FenParseException e) {
            e.printStackTrace();
        }
    }

    private final Group squares = new Group();
    private final Group pieceGroup = new Group();

    private final Map<BoardCoordinate, PieceEntry> entries = new HashMap<>();
    private final Map<BoardCoordinate, ImageView> pieceImageViews = new HashMap<>();
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
                Square square = new Square(light, i, j);
                squares.getChildren().add(square);
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

        for (BoardCoordinate sq : BoardCoordinate.values()) {
            updateSquareView(sq);
        }

    }


    private void removePieceImage(BoardCoordinate boardCoordinate) {
        PieceEntry entry = entries.remove(boardCoordinate);
        if (entry != null) {
            pieceGroup.getChildren().remove(entry.getPieceImageView());
        }

    }

    private void setPieceImageView(BoardCoordinate sq, PieceColor color, PieceType pieceType) {


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

    private void updateSquareView(BoardCoordinate sq) {

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

        for (BoardCoordinate sq : BoardCoordinate.values()) {

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

    private ImageView createPieceImageView(BoardCoordinate sq, PieceColor pieceColor, PieceType pieceType) {

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

        BoardCoordinate boardCoordinate = BoardCoordinate.valueOf(file, rank);

        dragPiece.setBoardCoordinate(boardCoordinate);

        dragPiece.setIsSelected(true);
    }


    private void handleMouseReleased(ImageView pieceView, MouseEvent event) {

        int x = (int) event.getSceneX();
        int y = (int) event.getSceneY();

        int file = findFile(x);
        int rank = findRank(y);

        BoardCoordinate from = dragPiece.getCoordinate();
        BoardCoordinate to = BoardCoordinate.valueOf(file, rank);


        boolean acceptMove = false;

        if (playerColor.equals(getCurrentColor())) {

            Move move = Moves.valueOf(board, from, to);

            if (move != null) {

                acceptMove = true;

                playMove(move);

                calculateComputerMoveTask = new Task() {
                    @Override
                    protected Move call() throws Exception {

                        SearchResult result = searchExecutor.submitSearch(board, new MaterialBoardEvaluation(), 5).get();

                        return result.getBestMove();
                    }
                };


                calculateComputerMoveTask.setOnSucceeded(successEvent -> {
                    Move computerMove = (Move) successEvent.getSource().getValue();
                    playMove(computerMove);
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


    private List<Move> getMoves() {

        List<Move> moves = board.getMoves(MoveGenerationStrategy.ALL_PSEUDO_LEGAL_MOVES);

        return moves;

    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Chess Application");

        Scene scene = new Scene(createContent());

        primaryStage.setScene(scene);

        primaryStage.show();

        primaryStage.setOnCloseRequest(t -> {

            if (calculateComputerMoveTask != null) {
                calculateComputerMoveTask.cancel();
            }

            Platform.exit();
        });

    }


    public void setEmtpy(BoardCoordinate sq) {

        if (entries.containsKey(sq)) {

            removePieceImage(sq);

        }

    }
}
