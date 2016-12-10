package com.erikmafo.javachess.board;

import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.Random;

/**
 * Created by erikf on 12/10/2016.
 */
public class ZobristTable {

    private static final int NUMBER_OF_SQUARES = Square.values().length;
    private static final int NUMBER_OF_PIECE_TYPES = PieceType.values().length;
    private static final int NUMBER_OF_PIECE_COLORS = PieceColor.values().length;
    private static final int NUMBER_OF_CASTLING_RIGHTS = CastlingRight.values().length;


    private final long[][][] pieces = new long[NUMBER_OF_PIECE_COLORS][NUMBER_OF_PIECE_TYPES][NUMBER_OF_SQUARES];
    private final long[][] castlingRights = new long[NUMBER_OF_PIECE_COLORS][NUMBER_OF_CASTLING_RIGHTS];
    private final long[] enPassent = new long[NUMBER_OF_SQUARES];
    private final long colorToMove;

    private long value = 0;


    public ZobristTable(Random random) {

        colorToMove = random.nextLong();

        for (int i=0; i<NUMBER_OF_PIECE_COLORS; i++) {
            for (int j=0; j<NUMBER_OF_PIECE_TYPES; j++) {
                for (int k=0; k<NUMBER_OF_SQUARES; k++) {
                    pieces[i][j][k] = random.nextLong();
                }
            }
        }

        for (int i=0; i<NUMBER_OF_CASTLING_RIGHTS; i++) {
            castlingRights[PieceColor.BLACK.ordinal()][i] = random.nextLong();
            castlingRights[PieceColor.WHITE.ordinal()][i] = random.nextLong();
        }

        for (int i=0; i<NUMBER_OF_SQUARES; i++) {
            enPassent[i] = random.nextLong();
        }
    }

    public ZobristTable(long seed) {
        this(new Random(seed));
    }




    public void shiftPiece(Square square, Piece piece) {
        value ^= pieces[piece.getColor().ordinal()][piece.getType().ordinal()][square.ordinal()];
    }


    public void shiftEnPassentTarget(Square square) {
        value ^= enPassent[square.ordinal()];
    }


    public void shiftColorToMove() {
        value ^= colorToMove;
    }

    public void shiftCastlingRight(PieceColor color, CastlingRight castlingRight) {
        value ^= castlingRights[color.ordinal()][castlingRight.ordinal()];
    }

    public long getValue() {
        return value;
    }



}
