package com.erikmafo.chess.gui.controller;

import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.gui.components.*;
import com.erikmafo.chess.gui.model.ChessEngine;
import com.erikmafo.chess.gui.model.ChessMove;
import com.erikmafo.chess.gui.model.EngineSearchResult;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.utils.parser.FenParseException;
import com.erikmafo.chess.utils.parser.FenParser;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

/**
 * Created by erikmafo on 27.12.16.
 */
public class Controller implements Initializable {


    public static final String START_FEN = "START_FEN";


    @FXML
    private Chessboard chessboard;
    private BoardLocation clickedPieceLocation;

    private ChessEngine engine;
    private String startFen;
    private final List<ChessMove> moveList = new ArrayList<>();


    public void handleSquarePressed(ChessboardEvent event) {
        System.out.println("mouse pressed at (" + event.getFile()+ ", " + event.getRank() + ")");
    }

    public void handleRestartPressed(ActionEvent actionEvent) {

        moveList.clear();
        update();

    }

    public void handleUndoPressed(ActionEvent actionEvent) {

        if (moveList.size() > 0) {
            moveList.remove(moveList.size() - 1);
            update();
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        startFen = resources.getString(START_FEN);

        engine = new ChessEngine(new FenParser(), Executors.newSingleThreadExecutor());

        update();
    }


    public void shutdown() {
        engine.shutdown();
    }


    private void updateChessboard() {

        Map<Square, Piece> squarePieceMap = engine.getSquarePieceMap();

        for (Square square : Square.values()) {
            BoardLocation boardLocation = toBoardLocation(square);

            if (squarePieceMap.containsKey(square)) {
                Piece piece = squarePieceMap.get(square);
                PieceImageView pieceImageView = new PieceImageView(
                        piece.getColor().toString().toLowerCase(),
                        piece.getType().toString().toLowerCase());
                chessboard.put(boardLocation, pieceImageView);
            } else {
                chessboard.remove(boardLocation);
            }
        }

        chessboard.setColorToMoveProperty(engine.getColorToMove().toString().toLowerCase());

    }




    @NotNull
    private BoardLocation toBoardLocation(Square square) {
        return new BoardLocation(square.getFile(), square.getRank());
    }

    @NotNull
    private Square toSquare(BoardLocation boardLocation) {
        return Square.valueOf(boardLocation.getFile(), boardLocation.getRank());
    }


    public void handlePieceClicked(ChessboardEvent chessboardEvent) {

        clickedPieceLocation = chessboardEvent.getLocation();

    }

    public void handleSquareClicked(ChessboardEvent event) {

        if (clickedPieceLocation != null) {

            ChessMove chessMove = new ChessMove(
                    toSquare(clickedPieceLocation), toSquare(event.getLocation()));
            if (engine.isLegal(chessMove)) {
                handlePlayerMove(chessMove);
            }

        }

    }

    private void play(ChessMove move) {
        moveList.add(move);
        update();
    }

    private void update() {
        try {
            engine.setPosition(startFen, moveList);
        } catch (FenParseException e) {
            e.printStackTrace();
        }
        updateChessboard();
    }


    public void handlePieceDropped(PieceDroppedEvent event) {

        ChessMove chessMove = new ChessMove(
                toSquare(event.getInitialLocation()), toSquare(event.getDropLocation()));

        if (engine.isLegal(chessMove)) {
            event.setPieceDropAccepted(true);
            handlePlayerMove(chessMove);
        }

    }


    private void handlePlayerMove(ChessMove playerMove) {
        play(playerMove);
        Task<EngineSearchResult> searchResultTask = engine.go();
        searchResultTask.setOnSucceeded(evt -> {
            EngineSearchResult result = searchResultTask.getValue();
            play(result.getBestMove());
        });
    }






}
