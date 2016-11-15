package com.erikmafo.javachess.moves;


import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

public abstract class MoveGenerators {

    private MoveGenerators() {
    }

    private static MoveGenerator WHITE_PAWN = new PawnMoveGenerator(PieceColor.WHITE);
    private static MoveGenerator WHITE_KING = new KingMoveGenerator(PieceColor.WHITE);
    private static MoveGenerator WHITE_KNIGHT = new NonSlidingMoveGenerator(PieceColor.WHITE, PieceType.KNIGHT);
    private static MoveGenerator WHITE_BISHOP = new SlidingMoveGenerator(PieceColor.WHITE, PieceType.BISHOP);
    private static MoveGenerator WHITE_ROOK = new SlidingMoveGenerator(PieceColor.WHITE, PieceType.ROOK);
    private static MoveGenerator WHITE_QUEEN = new SlidingMoveGenerator(PieceColor.WHITE, PieceType.QUEEN);

    private static MoveGenerator BLACK_PAWN = new PawnMoveGenerator(PieceColor.BLACK);
    private static MoveGenerator BLACK_KING = new KingMoveGenerator(PieceColor.BLACK);
    private static MoveGenerator BLACK_KNIGHT = new NonSlidingMoveGenerator(PieceColor.BLACK, PieceType.KNIGHT);
    private static MoveGenerator BLACK_BISHOP = new SlidingMoveGenerator(PieceColor.BLACK, PieceType.BISHOP);
    private static MoveGenerator BLACK_ROOK = new SlidingMoveGenerator(PieceColor.BLACK, PieceType.ROOK);
    private static MoveGenerator BLACK_QUEEN = new SlidingMoveGenerator(PieceColor.BLACK, PieceType.QUEEN);

    private static MoveGenerator emptyMoveGenerator = new MoveGenerator();

    public static MoveGenerator getEmptyMoveGenerator() {
        return emptyMoveGenerator;
    }

    public static MoveGenerator getWhitePawn() {
        return WHITE_PAWN;
    }

    public static MoveGenerator getWhiteKnight() {
        return WHITE_KNIGHT;
    }

    public static MoveGenerator getWhiteBishop() {
        return WHITE_BISHOP;
    }

    public static MoveGenerator getWhiteRook() {
        return WHITE_ROOK;
    }

    public static MoveGenerator getWhiteQueen() {
        return WHITE_QUEEN;
    }

    public static MoveGenerator getWhiteKing() {
        return WHITE_KING;
    }

    public static MoveGenerator getBlackPawn() {
        return BLACK_PAWN;
    }

    public static MoveGenerator getBlackBishop() {
        return BLACK_BISHOP;
    }

    public static MoveGenerator getBlackRook() {
        return BLACK_ROOK;
    }

    public static MoveGenerator getBlackKnight() {
        return BLACK_KNIGHT;
    }

    public static MoveGenerator getBlackQueen() {
        return BLACK_QUEEN;
    }

    public static MoveGenerator getBlackKing() {
        return BLACK_KING;
    }

    public static MoveGenerator valueOf(PieceColor pieceColor, PieceType pieceType) {
        MoveGenerator moveGenerator = null;
        switch (pieceType) {

            case PAWN:
                moveGenerator = pieceColor.isWhite() ? WHITE_PAWN : BLACK_PAWN;
                break;
            case BISHOP:
                moveGenerator = pieceColor.isWhite() ? WHITE_BISHOP : BLACK_BISHOP;
                break;
            case KNIGHT:
                moveGenerator = pieceColor.isWhite() ? WHITE_KNIGHT : BLACK_KNIGHT;
                break;
            case ROOK:
                moveGenerator = pieceColor.isWhite() ? WHITE_ROOK : BLACK_ROOK;
                break;
            case QUEEN:
                moveGenerator = pieceColor.isWhite() ? WHITE_QUEEN : BLACK_QUEEN;
                break;
            case KING:
                moveGenerator = pieceColor.isWhite() ? WHITE_KING : BLACK_KING;
                break;
        }
        return moveGenerator;
    }


}
