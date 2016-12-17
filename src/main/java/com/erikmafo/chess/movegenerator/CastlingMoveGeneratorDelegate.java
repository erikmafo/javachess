package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.BasicOffset;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.move.MoveFactory;
import com.erikmafo.chess.pieces.PieceColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikmafo on 19.11.16.
 */
public class CastlingMoveGeneratorDelegate implements MoveGeneratorDelegate {


    private final MoveFactory moveFactory;

    private final Square[] whiteKingSideCastlingSquares;
    private final Square[] whiteQueenSideCastlingSquares;

    private final Square[] blackKingSideCastlingSquares;
    private final Square[] blackQueenSideCastlingSquares;


    private final BoardSeeker boardSeeker;

    CastlingMoveGeneratorDelegate(MoveFactory moveFactory, BoardSeeker boardSeeker) {
        this.moveFactory = moveFactory;
        this.boardSeeker = boardSeeker;

        whiteKingSideCastlingSquares = new Square[]{Square.F1, Square.G1};
        whiteQueenSideCastlingSquares = new Square[]{Square.D1, Square.C1, Square.B1};


        blackKingSideCastlingSquares = new Square[]{Square.F8, Square.G8};;
        blackQueenSideCastlingSquares = new Square[]{Square.D8, Square.C8, Square.B8};;

    }

    private Square[] getKingSideCastlingSquares(PieceColor color) {
        return color.isWhite() ? whiteKingSideCastlingSquares : blackKingSideCastlingSquares;
    }

    private Square[] getQueenSideCastlingSquares(PieceColor color) {
        return color.isWhite() ? whiteQueenSideCastlingSquares : blackQueenSideCastlingSquares;
    }


    private Square getQueenSideCastlingTarget(PieceColor color) {
        return color.isWhite() ? Square.C1 : Square.C8;
    }

    private Square getKingSideCastlingTarget(PieceColor color) {
        return color.isWhite() ? Square.G1 : Square.G8;
    }


    private Square getInitialKingSideRookCoordinate(PieceColor color) {
        return color.isWhite() ? Square.H1 : Square.H8;
    }

    private Square getInitialQueenSideRookCoordinate(PieceColor color) {
        return color.isWhite() ? Square.A1 : Square.A8;
    }


    private boolean isChecked(Board board, PieceColor color) {

        return boardSeeker.isAttackedBy(color.getOpposite(), board.getKingLocation(color), board);

    }


    private boolean isKingSideCastlingLegal(Board board, PieceColor color) {
        if (isChecked(board, color) || !board.hasKingSideCastlingRight(color)) {
            return false;
        }

        for (Square coordinate : getKingSideCastlingSquares(color)) {
            if (board.isOccupied(coordinate)) {
                return false;
            }
        }

        return !(boardSeeker.isAttackedBy(color.getOpposite(), getKingSideCastlingSquares(color)[0], board) ||
                boardSeeker.isAttackedBy(color.getOpposite(), getKingSideCastlingSquares(color)[1], board));
    }


    private boolean isQueenSideCastlingLegal(Board board, PieceColor color) {
        if (isChecked(board, color) || !board.hasQueenSideCastlingRight(color)) {
            return false;
        }

        for (Square coordinate : getQueenSideCastlingSquares(color)) {
            if (board.isOccupied(coordinate)) {
                return false;
            }
        }

        return !(boardSeeker.isAttackedBy(color.getOpposite(), getQueenSideCastlingSquares(color)[0], board) ||
                boardSeeker.isAttackedBy(color.getOpposite(), getQueenSideCastlingSquares(color)[1], board));

    }


    @Override
    public List<Move> generateMoves(Board board, Square from) {
        List<Move> moves = new ArrayList<>();
        appendCastlingMoves(board, from, moves);
        return moves;
    }

    private void appendCastlingMoves(Board board, Square from, List<Move> moves) {
        PieceColor color = board.getColorToMove();

        if (isKingSideCastlingLegal(board, color)) {
            moves.add(moveFactory.newCastlingMove(board, from, getKingSideCastlingTarget(color),
                    getInitialKingSideRookCoordinate(color), from.next(BasicOffset.RIGHT)));
        } else if (isQueenSideCastlingLegal(board, color)) {
            moves.add(moveFactory.newCastlingMove(board, from , getQueenSideCastlingTarget(color),
                    getInitialQueenSideRookCoordinate(color), from.next(BasicOffset.LEFT)));
        }
    }

}
