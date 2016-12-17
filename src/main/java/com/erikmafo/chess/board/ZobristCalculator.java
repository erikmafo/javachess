package com.erikmafo.chess.board;

import com.erikmafo.chess.pieces.Piece;
import com.erikmafo.chess.pieces.PieceColor;
import com.erikmafo.chess.pieces.PieceType;

import java.util.Random;

/**
 * Created by erikf on 12/10/2016.
 */
public class ZobristCalculator {

    private static final int NUMBER_OF_SQUARES = Square.values().length;
    private static final int NUMBER_OF_PIECE_TYPES = PieceType.values().length;
    private static final int NUMBER_OF_PIECE_COLORS = PieceColor.values().length;
    private static final int NUMBER_OF_CASTLING_RIGHTS = CastlingRight.values().length;


    private final long[][][] pieces = new long[NUMBER_OF_PIECE_COLORS][NUMBER_OF_PIECE_TYPES][NUMBER_OF_SQUARES];
    private final long[][] castlingRights = new long[NUMBER_OF_PIECE_COLORS][NUMBER_OF_CASTLING_RIGHTS];
    private final long[] enPassent = new long[NUMBER_OF_SQUARES];
    private final long colorToMove;

    private long value = 0;


    public ZobristCalculator(Random random) {

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

    public ZobristCalculator(long seed) {
        this(new Random(seed));
    }



    public static ZobristCalculator from(Board board, Random random) {

        ZobristCalculator zobristCalculator = new ZobristCalculator(random);

        for (Square square : board.getOccupiedSquares()) {
            zobristCalculator.shiftPiece(square, board.getNullablePiece(square));
        }

        for (PieceColor color : PieceColor.values()) {
            if (board.hasKingSideCastlingRight(color) && board.hasQueenSideCastlingRight(color)) {
                zobristCalculator.shiftCastlingRight(color, CastlingRight.BOTH);
            } else if (board.hasKingSideCastlingRight(color)) {
                zobristCalculator.shiftCastlingRight(color, CastlingRight.SHORT);
            } else if (board.hasQueenSideCastlingRight(color)) {
                zobristCalculator.shiftCastlingRight(color, CastlingRight.LONG);
            } else {
                zobristCalculator.shiftCastlingRight(color, CastlingRight.NONE);
            }
        }

        if (board.enPassentTarget().isPresent()) {
            zobristCalculator.shiftEnPassentTarget(board.enPassentTarget().get());
        }

        if (PieceColor.BLACK.equals(board.getColorToMove())) {
            zobristCalculator.shiftColorToMove();
        }

        return zobristCalculator;
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
