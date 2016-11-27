package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.board.Offset;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.pieces.PieceColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikmafo on 19.11.16.
 */
public class CastlingMoveGenerator implements MoveGenerator {

    private final BoardCoordinate[] whiteKingSideCastlingSquares;
    private final BoardCoordinate[] whiteQueenSideCastlingSquares;

    private final BoardCoordinate[] blackKingSideCastlingSquares;
    private final BoardCoordinate[] blackQueenSideCastlingSquares;


    private final BoardSeeker boardSeeker;

    CastlingMoveGenerator(BoardSeeker boardSeeker) {
        this.boardSeeker = boardSeeker;

        whiteKingSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.F1, BoardCoordinate.G1};
        whiteQueenSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.D1, BoardCoordinate.C1, BoardCoordinate.B1};


        blackKingSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.F8, BoardCoordinate.G8};;
        blackQueenSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.D8, BoardCoordinate.C8, BoardCoordinate.B8};;

    }

    private BoardCoordinate[] getKingSideCastlingSquares(PieceColor color) {
        return color.isWhite() ? whiteKingSideCastlingSquares : blackKingSideCastlingSquares;
    }

    private BoardCoordinate[] getQueenSideCastlingSquares(PieceColor color) {
        return color.isWhite() ? whiteQueenSideCastlingSquares : blackQueenSideCastlingSquares;
    }


    private BoardCoordinate getQueenSideCastlingTarget(PieceColor color) {
        return color.isWhite() ? BoardCoordinate.C1 : BoardCoordinate.C8;
    }

    private BoardCoordinate getKingSideCastlingTarget(PieceColor color) {
        return color.isWhite() ? BoardCoordinate.G1 : BoardCoordinate.G8;
    }


    private BoardCoordinate getInitialKingSideRookCoordinate(PieceColor color) {
        return color.isWhite() ? BoardCoordinate.H1 : BoardCoordinate.H8;
    }

    private BoardCoordinate getInitialQueenSideRookCoordinate(PieceColor color) {
        return color.isWhite() ? BoardCoordinate.A1 : BoardCoordinate.A8;
    }


    private boolean isChecked(Board board, PieceColor color) {

        return boardSeeker.isAttackedBy(color.getOpposite(), board.getKingLocation(color), board);

    }


    private boolean isKingSideCastlingLegal(Board board, PieceColor color) {
        if (isChecked(board, color) || !board.hasKingSideCastlingRight(color)) {
            return false;
        }

        for (BoardCoordinate coordinate : getKingSideCastlingSquares(color)) {
            if (board.isOccupied(coordinate)) {
                return false;
            }
        }

        return !(boardSeeker.isAttackedBy(color, getKingSideCastlingSquares(color)[0], board) ||
                boardSeeker.isAttackedBy(color, getKingSideCastlingSquares(color)[1], board));
    }


    private boolean isQueenSideCastlingLegal(Board board, PieceColor color) {
        if (isChecked(board, color) || !board.hasQueenSideCastlingRight(color)) {
            return false;
        }

        for (BoardCoordinate coordinate : getQueenSideCastlingSquares(color)) {
            if (board.isOccupied(coordinate)) {
                return false;
            }
        }

        return !(boardSeeker.isAttackedBy(color, getQueenSideCastlingSquares(color)[0], board) ||
                boardSeeker.isAttackedBy(color, getQueenSideCastlingSquares(color)[1], board));

    }


    @Override
    public List<Move> generateMoves(Board board, BoardCoordinate from, MoveFactory moveFactory) {
        List<Move> moves = new ArrayList<>();
        appendCastlingMoves(moveFactory, board, from, moves);
        return moves;
    }

    private void appendCastlingMoves(MoveFactory moveFactory, Board board, BoardCoordinate from, List<Move> moves) {
        PieceColor color = board.getColorToMove();

        if (isKingSideCastlingLegal(board, color)) {
            moves.add(moveFactory.newCastlingMove(from, getKingSideCastlingTarget(color),
                    getInitialKingSideRookCoordinate(color), from.next(Offset.RIGHT)));
        } else if (isQueenSideCastlingLegal(board, color)) {
            moves.add(moveFactory.newCastlingMove(from , getQueenSideCastlingTarget(color),
                    getInitialQueenSideRookCoordinate(color), from.next(Offset.LEFT)));
        }
    }

}
