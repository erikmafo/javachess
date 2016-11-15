package com.erikmafo.javachess.ui;

import com.erikmafo.javachess.boardrep.Board;
import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;
import com.erikmafo.javachess.moves.Move;
import com.erikmafo.javachess.moves.Moves;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by erikmafo on 13.11.16.
 */
public class ChessApplication extends Application {

    public static final int SQUARE_SIZE = 60;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;


    private Board board = new Board();
    private Group squares = new Group();
    private Group pieceGroup = new Group();


    private Map<BoardCoordinate, ImageView> pieceImageViews = new HashMap<>();

    private DragPiece dragPiece = new DragPiece();


    private Map<PieceColor, Map<PieceType, Image>> pieceImages = new HashMap<>();
    private Task<Move> calculateComputerMoveTask;

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


        root.setPrefSize(SQUARE_SIZE * WIDTH, SQUARE_SIZE * HEIGHT);

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                boolean light = (i + j) % 2 == 0;
                Square square = new Square(light, i, j);
                squares.getChildren().add(square);
            }
        }

        loadPieceImages();

        createPieces();

        root.getChildren().addAll(squares, pieceGroup);

        return root;

    }




    private void updateBoardView() {

        for (BoardCoordinate sq : BoardCoordinate.values()) {
            updateSquareView(sq);
        }

    }

    private void updateSquareView(BoardCoordinate sq) {
        if (board.isOccupiedAt(sq) && !pieceImageViews.containsKey(sq)) {
            PieceColor pieceColor = board.getPieceColorAt(sq);
            PieceType pieceType = board.getPieceTypeAt(sq);
            ImageView imageView = createPieceImageView(sq, pieceColor, pieceType);
            pieceImageViews.put(sq, imageView);
            pieceGroup.getChildren().add(imageView);
        } else if (pieceImageViews.containsKey(sq) && !board.isOccupiedAt(sq)) {
            ImageView imageView = pieceImageViews.remove(sq);
            pieceGroup.getChildren().remove(imageView);
        }
    }


    private void createPieces() {

        for (BoardCoordinate sq : BoardCoordinate.values()) {

            if (board.isOccupiedAt(sq)) {

                PieceColor pieceColor = board.getPieceColorAt(sq);
                PieceType pieceType = board.getPieceTypeAt(sq);

                ImageView pieceView = createPieceImageView(sq, pieceColor, pieceType);

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

        Move move = Moves.valueOf(board, from, to);

        if (getMoves().contains(move)) {

            playMove(move);

            calculateComputerMoveTask = new Task() {
                @Override
                protected Move call() throws Exception {
                    return Moves.findBestMove(board);
                }
            };


            calculateComputerMoveTask.setOnSucceeded(successEvent -> {
                Move computerMove = (Move) successEvent.getSource().getValue();
                playMove(computerMove);
            });

            executorService.submit(calculateComputerMoveTask);


        } else {
            pieceView.relocate(findX(from.getFile()), findY(from.getRank()));
        }
    }

    private void playMove(Move move) {

        if (pieceImageViews.containsKey(move.getTo())) {
            ImageView imageView = pieceImageViews.remove(move.getTo());
            pieceGroup.getChildren().remove(imageView);
        }

        board.play(move);
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

        List<Move> moves = board.getPossibleMoves();

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


}
