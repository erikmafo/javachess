package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.Offset;
import com.erikmafo.javachess.boardrep.ReadableBoard;
import com.erikmafo.javachess.pieces.AttackTable;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.List;

/**
 * Created by erikmafo on 15.11.16.
 */
class KingMoveGenerator extends NonSlidingMoveGenerator {

    private BoardCoordinate[] kingSideCastlingSquares;
    private BoardCoordinate[] queenSideCastlingSquares;

    private BoardCoordinate kingSideCastlingTarget;
    private BoardCoordinate queenSideCastlingTarget;

    private BoardCoordinate initialKingSideRookCoordinate;
    private BoardCoordinate initialQueenSideRookCoordinate;

    KingMoveGenerator(PieceColor pieceColor) {
        super(pieceColor, PieceType.KING);
        if (pieceColor.isWhite()) {
            kingSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.F1, BoardCoordinate.G1};
            queenSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.D1, BoardCoordinate.C1,
                    BoardCoordinate.B1};
            kingSideCastlingTarget = BoardCoordinate.G1;
            queenSideCastlingTarget = BoardCoordinate.C1;

            initialKingSideRookCoordinate = BoardCoordinate.H1;
            initialQueenSideRookCoordinate = BoardCoordinate.A1;

        } else {
            kingSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.F8, BoardCoordinate.G8};
            queenSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.D8, BoardCoordinate.C8,
                    BoardCoordinate.B8};
            kingSideCastlingTarget = BoardCoordinate.G8;
            queenSideCastlingTarget = BoardCoordinate.C8;

            initialKingSideRookCoordinate = BoardCoordinate.H8;
            initialQueenSideRookCoordinate = BoardCoordinate.A8;
        }
    }

    @Override
    public void findPossibleMoves(ReadableBoard board, BoardCoordinate pieceLocation,
                                  AttackTable attackTable, List<Move> moves) {
        super.findPossibleMoves(board, pieceLocation, attackTable, moves);

        // check for castling moves

        if (isKingSideCastlingLegal(board)) {
            moves.add(Moves.createCastlingMove(pieceLocation, kingSideCastlingTarget,
                    initialKingSideRookCoordinate, kingSideCastlingTarget.next(Offset.LEFT)));
        }

        if (isQueenSideCastlingLegal(board)) {
            moves.add(Moves.createCastlingMove(pieceLocation, queenSideCastlingTarget,
                    initialQueenSideRookCoordinate, queenSideCastlingTarget.next(Offset.RIGHT)));
        }

    }


    private boolean isKingSideCastlingLegal(ReadableBoard board) {
        if (board.isChecked(pieceColor) || !board.isKingSideCastlingPossible(pieceColor)) {
            return false;
        }

        for (BoardCoordinate coordinate : kingSideCastlingSquares) {
            if (board.isOccupiedAt(coordinate) || board.isAttacked(pieceColor, coordinate)) {
                return false;
            }
        }

        return true;
    }


    private boolean isQueenSideCastlingLegal(ReadableBoard board) {
        if (board.isChecked(pieceColor) || !board.isQueenSideCastlingPossible(pieceColor)) {
            return false;
        }

        for (BoardCoordinate coordinate : queenSideCastlingSquares) {
            if (board.isOccupiedAt(coordinate)) {
                return false;
            }
        }

        return !(board.isAttacked(pieceColor, queenSideCastlingSquares[0]) ||
                board.isAttacked(pieceColor, queenSideCastlingSquares[1]));

    }



}
