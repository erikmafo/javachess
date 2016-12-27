package com.erikmafo.chess.gui.controller;

import com.erikmafo.chess.gui.view.ChessboardEvent;
import com.erikmafo.chess.gui.view.ChessboardView;
import com.erikmafo.chess.gui.view.PieceImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Created by erikmafo on 27.12.16.
 */
public class Controller {

    @FXML
    private ChessboardView chessboard;


    public void handleSquarePressed(ChessboardEvent event) {
        System.out.println("mouse pressed at (" + event.getFile()+ ", " + event.getRank() + ")");
        chessboard.put(event.getFile(), event.getRank(), new PieceImageView("white", "pawn"));
    }

    public void handleSquareClicked(ChessboardEvent event) {
        System.out.println("mouse clicked at (" + event.getFile()+ ", " + event.getRank() + ")");
        chessboard.remove(event.getFile(), event.getRank());
    }

    public void handleRestartPressed(ActionEvent actionEvent) {
        System.out.println("Restart pressed");
    }

    public void handleUndoPressed(ActionEvent actionEvent) {
        System.out.println("Undo pressed");
    }
}
