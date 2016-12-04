package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.board.Offset;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by erikmafo on 19.11.16.
 */
public class BoardSeeker {


    private final Offset[] knightOffsets = new Offset[8];
    private final Offset[] rookOffsets = new Offset[4];
    private final Offset[] bishopOffsets = new Offset[4];
    private final Offset[] kingOffsets = new Offset[8];
    private final Offset[] blackPawnOffsets = new Offset[2];
    private final Offset[] whitePawnOffsets = new Offset[2];

    public BoardSeeker() {
        Offset.getKnightAttackOffsets().toArray(knightOffsets);
        Offset.getRookAttackOffsets().toArray(rookOffsets);
        Offset.getBishopAttackOffsets().toArray(bishopOffsets);
        Offset.getKingAttackOffsets().toArray(kingOffsets);
        Offset.getBlackPawnAttackOffsets().toArray(blackPawnOffsets);
        Offset.getWhitePawnAttackOffsets().toArray(whitePawnOffsets);
    }

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
