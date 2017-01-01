package com.erikmafo.chess.gui.controller;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.gui.components.*;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.move.Moves;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.utils.parser.FenParseException;
import com.erikmafo.chess.utils.parser.FenParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by erikmafo on 27.12.16.
 */
public class Controller implements Initializable {

    @FXML
    private Chessboard chessboard;

    private Board board;
    private BoardLocation clickedPieceLocation;


    public void handleSquarePressed(ChessboardEvent event) {
        System.out.println("mouse pressed at (" + event.getFile()+ ", " + event.getRank() + ")");
    }



    public void handleRestartPressed(ActionEvent actionEvent) {
        System.out.println("Restart pressed");
    }

    public void handleUndoPressed(ActionEvent actionEvent) {
        System.out.println("Undo pressed");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            board = new FenParser().parse(FenParser.START_POSITION);
        } catch (FenParseException e) {
            e.printStackTrace();
        }

        for (Square square : board.getOccupiedSquares()) {
            if (board.isOccupied(square)) {
                PieceImageView view = getPieceImageView(board.getNullablePiece(square));
                chessboard.put(toBoardLocation(square), view);
            }
        }

        chessboard.setColorToMoveProperty("white");

    }

    @NotNull
    private BoardLocation toBoardLocation(Square square) {
        return new BoardLocation(square.getFile(), square.getRank());
    }

    @NotNull
    private Square toSquare(BoardLocation boardLocation) {
        return Square.valueOf(boardLocation.getFile(), boardLocation.getRank());
    }


    PieceImageView getPieceImageView(Piece piece) {
        return new PieceImageView("" + piece.getColor(), "" + piece.getType());
    }


    public void handlePieceClicked(ChessboardEvent chessboardEvent) {

        clickedPieceLocation = chessboardEvent.getBoardLocation();

    }

    public void handleSquareClicked(ChessboardEvent event) {

        if (clickedPieceLocation != null) {

            Move move = Moves.valueOf(board, toSquare(clickedPieceLocation), toSquare(event.getBoardLocation()));

            if (move != null) {
                chessboard.relocate(clickedPieceLocation, event.getBoardLocation());
                move.play();
                chessboard.toogleColorToMove();
            }



        }

    }


    public void handlePieceDropped(PieceDroppedEvent event) {

        Move move = Moves.valueOf(board, toSquare(event.getInitialLocation()), toSquare(event.getBoardLocation()));

        if (move != null) {
            event.setPieceDropAccepted(true);
            move.play();
            chessboard.toogleColorToMove();
        }

    }
}
