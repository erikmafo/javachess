package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.board.*;
import com.erikmafo.chess.pieces.Piece;
import com.erikmafo.chess.pieces.PieceColor;
import com.erikmafo.chess.pieces.PieceType;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by erikmafo on 19.11.16.
 */
public class BoardSeeker {


    private final Offset[] knightOffsets = KnightOffset.values();
    private final Offset[] rookOffsets = BasicOffset.rookValues();
    private final Offset[] bishopOffsets = BasicOffset.bishopValues();
    private final Offset[] kingOffsets = BasicOffset.values();
    private final Offset[] blackPawnOffsets = {BasicOffset.DOWN_LEFT, BasicOffset.DOWN_RIGHT};
    private final Offset[] whitePawnOffsets = {BasicOffset.UP_LEFT, BasicOffset.UP_RIGHT};


    public boolean isAttackedBy(PieceColor opponent, Square square, Board board) {

        boolean result;

        if (search(piece -> piece.is(opponent, PieceType.BISHOP), board, square, true, bishopOffsets).isPresent() ||
                search(piece -> piece.is(opponent, PieceType.ROOK), board, square, true, rookOffsets).isPresent() ||
                search(piece -> piece.is(opponent, PieceType.KNIGHT), board, square, false, knightOffsets).isPresent() ||
                search(piece -> piece.is(opponent, PieceType.KING), board, square, false, kingOffsets).isPresent()) {
            result = true;
        } else {
            Offset[] pawnOffsets = opponent.isWhite() ? blackPawnOffsets : whitePawnOffsets;
            result = search(piece -> piece.is(opponent, PieceType.PAWN), board, square, false, pawnOffsets).isPresent();
        }

        return result;
    }



    public MobilityCount getMobilityCount(Board board, Square from, boolean slide, Offset... offsets) {

        int empty = 0;
        int occupiedByWhite = 0;
        int occupiedByBlack = 0;

        for (Offset offset : offsets) {
            Square current = from.next(offset);

            while (current.isOnBoard()) {

                if (board.isOccupied(current)) {
                    Piece piece = board.getNullablePiece(current);
                    if (piece.getColor().isWhite()) {
                        occupiedByWhite++;
                    } else {
                        occupiedByBlack++;
                    }
                    break;
                } else {
                    empty++;
                }

                if (slide) {
                    current = current.next(offset);
                } else {
                    break;
                }

            }
        }

        return new MobilityCount(empty, occupiedByWhite, occupiedByBlack);
    }




    public Optional<Piece> search(Predicate<Piece> piecePredicate, Board board, Square start, boolean slide, Offset... offsets) {

        Optional<Piece> result = Optional.empty();

        for (Offset offset : offsets) {

            Square current = start.next(offset);

            while (current.isOnBoard()){

                Optional<Piece> pieceOptional = board.pieceAt(current);
                if (pieceOptional.filter(piecePredicate).isPresent()) {
                    result = pieceOptional;
                    break;
                }

                if (slide) {
                    if (pieceOptional.isPresent()) {
                        break;
                    }
                    current = current.next(offset);
                } else {
                    break;
                }
            }


        }

        return result;
    }





}
