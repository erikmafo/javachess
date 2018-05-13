package com.erikmafo.chess.gui.controller;

import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.gui.components.*;
import com.erikmafo.chess.gui.model.BoardLocation;
import com.erikmafo.chess.gui.model.ChessEngine;
import com.erikmafo.chess.gui.model.ChessMove;
import com.erikmafo.chess.gui.model.EngineSearchResult;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.utils.parser.FenParseException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.jetbrains.annotations.NotNull;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by erikmafo on 27.12.16.
 */
public class Controller {

    @FXML
    private ListView<String> turnHistory;

    @FXML
    private ObservableList<String> turnHistoryItems = FXCollections.observableArrayList();

    @FXML
    private Chessboard chessboard;

    private Task<EngineSearchResult> searchResultTask;

    private BoardLocation clickedPieceLocation;

    @Inject
    private ChessEngine engine;

    @Inject
    private String startPositionFen;

    private final List<ChessMove> moveList = new ArrayList<>();

    private String playerColor = "white";

    public void handleRestartPressed(ActionEvent actionEvent) {

        stopCalculating();
        moveList.clear();
        turnHistoryItems.clear();
        update();

    }

    public void handleUndoPressed(ActionEvent actionEvent) {

        stopCalculating();

        if (!turnHistoryItems.isEmpty()) {
            turnHistoryItems.remove(turnHistoryItems.size() - 1);
        }

        if (playerColor.equals(chessboard.getColorToMove())) {
            if (moveList.size() > 1) {
                moveList.remove(moveList.size() - 1);
                moveList.remove(moveList.size() - 1);
            }

        } else {
            if (moveList.size() > 0) {
                moveList.remove(moveList.size() - 1);
            }
        }

        update();
    }

    /**
     * Called by the fxml loader on startup
     */
    public void initialize() {

        turnHistory.setItems(turnHistoryItems);

        update();
    }

    @PreDestroy
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

        chessboard.setColorToMove(engine.getColorToMove().toString().toLowerCase());

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

        String moveAsLongAlgebraic = move.toLongAlgebraic();

        if (PieceColor.WHITE.equals(engine.getColorToMove())) {
            turnHistoryItems.add(moveAsLongAlgebraic);
        } else {
            int index = turnHistoryItems.size() - 1;
            String newEntry = turnHistoryItems.get(index) + " " + moveAsLongAlgebraic;
            turnHistoryItems.set(index, newEntry);
        }

        update();
    }

    private void update() {
        try {
            engine.setPosition(startPositionFen, moveList);
        } catch (FenParseException e) {
            e.printStackTrace(); // should not happen since startPositionFen is always valid
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

    private boolean isCalculationInProgress() {
        return searchResultTask != null && searchResultTask.isRunning();
    }

    private void stopCalculating() {
        if (isCalculationInProgress()) {
            searchResultTask.cancel();
            searchResultTask = null;
        }
    }

    private void handlePlayerMove(ChessMove playerMove) {

        if (isCalculationInProgress()) {
            return;
        }

        play(playerMove);
        searchResultTask = engine.go();
        searchResultTask.setOnSucceeded(evt -> {
            EngineSearchResult result = searchResultTask.getValue();
            play(result.getBestMove());
        });
    }
}
