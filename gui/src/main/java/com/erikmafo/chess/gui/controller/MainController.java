package com.erikmafo.chess.gui.controller;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.gui.components.BoardLocation;
import com.erikmafo.chess.gui.components.GameBoard;
import com.erikmafo.chess.gui.components.PieceImageView;
import com.erikmafo.chess.utils.parser.FenParseException;
import com.erikmafo.chess.utils.parser.FenParser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by erikmafo on 01.01.17.
 */
public class MainController implements Initializable {


    private final PieceImageViewResolver pieceImageViewResolver = new PieceImageViewResolver();
    private Board board;


    @FXML private GameBoard gameBoard;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            board = new FenParser().parse(FenParser.START_POSITION);
        } catch (FenParseException e) {
            e.printStackTrace();
        }

        for (Square square : board.getOccupiedSquares()) {
            if (board.isOccupied(square)) {
                PieceImageView pieceImageView = pieceImageViewResolver
                        .getPieceImageView(board.getNullablePiece(square));

                gameBoard.put(toBoardLocation(square), pieceImageView);


            }
        }


    }

    @NotNull
    private BoardLocation toBoardLocation(Square square) {
        return new BoardLocation(square.getFile(), square.getRank());
    }

    @NotNull
    private Square toSquare(BoardLocation boardLocation) {
        return Square.valueOf(boardLocation.getFile(), boardLocation.getRank());
    }



}
