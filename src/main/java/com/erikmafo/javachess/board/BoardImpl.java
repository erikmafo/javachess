package com.erikmafo.javachess.board;

import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.movegenerator.MoveGenerationStrategy;
import com.erikmafo.javachess.movegenerator.MoveGenerator;
import com.erikmafo.javachess.movegenerator.MoveGeneratorFactory;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.*;

/**
 * Created by erikmafo on 19.11.16.
 */
public class BoardImpl implements Board, MoveReceiver {


    private final Map<Square, Piece> pieceEntryEnumMap = new EnumMap<>(Square.class);

    private final Map<Integer, Square> enPassentTargets = new HashMap<>();

    private final Map<PieceColor, Boolean> hasCastled = new HashMap<>();

    private final int[] castlingSquaresMoveCount = new int[6];

    private final MoveGeneratorFactory moveGeneratorFactory;

    private static final int E1_INDEX = 0;
    private static final int H1_INDEX = 1;
    private static final int A1_INDEX = 2;

    private static final int E8_INDEX = 3;
    private static final int H8_INDEX = 4;
    private static final int A8_INDEX = 5;

    private Piece lastMovedPiece;
    private Square lastMoveTo;
    private Square lastMoveFrom;

    private int moveCount = 0;
    private PieceColor colorToMove = PieceColor.WHITE;

    private Map<PieceColor, CastlingRight> initialCastlingRight = new HashMap<>();

    BoardImpl() {
        this.moveGeneratorFactory = new MoveGeneratorFactory();
    }

    BoardImpl(MoveGeneratorFactory moveGeneratorFactory, PieceColor colorToMove, Map<PieceColor, CastlingRight> initialCastlingRight) {
        this.moveGeneratorFactory = moveGeneratorFactory;
        this.colorToMove = colorToMove;
        this.initialCastlingRight = initialCastlingRight;
    }

    @Override
    public void movePiece(Square from, Square to) {

        lastMovedPiece = pieceEntryEnumMap.remove(from);
        pieceEntryEnumMap.put(to, lastMovedPiece);
        lastMoveFrom = from;
        lastMoveTo = to;

    }

    @Override
    public void remove(Square square) {
        pieceEntryEnumMap.remove(square);
    }

    @Override
    public void put(Square square, Piece piece) {
        pieceEntryEnumMap.put(square, piece);
    }

    @Override
    public void setEnPassentTarget(Square square) {
        enPassentTargets.put(moveCount + 1, square);
    }

    @Override
    public void removeEnPassentTarget() {
        enPassentTargets.remove(moveCount + 1);
    }

    @Override
    public void setHasCastled(PieceColor color, boolean hasCastled) {

        this.hasCastled.put(color, hasCastled);

    }

    @Override
    public void resetHalfMoveClock() {

    }

    @Override
    public void completePlay() {

        moveCount++;
        toggleColorToMove();

        int index = getCastlingSquareIndex(lastMoveTo);

        if (index > 0) {
            castlingSquaresMoveCount[index] += 1;
        }


    }


    private int getCastlingSquareIndex(Square castlingSquare) {

        if (castlingSquare == null) {
            return -1;
        }

        int index;

        switch (castlingSquare) {
            case E1:
                index = E1_INDEX;
                break;
            case H1:
                index = H1_INDEX;
                break;
            case A1:
                index = A1_INDEX;
                break;
            case E8:
                index = E8_INDEX;
                break;
            case H8:
                index = H8_INDEX;
                break;
            case A8:
                index = A8_INDEX;
                break;
            default:
                index = -1;
                break;
        }

        return index;
    }



    private void toggleColorToMove() {
        colorToMove = colorToMove.isWhite() ? PieceColor.BLACK : PieceColor.WHITE;
    }

    @Override
    public void completeUndo() {

        moveCount--;
        toggleColorToMove();

        int index = getCastlingSquareIndex(lastMoveFrom);

        if (index > 0) {
            castlingSquaresMoveCount[index] -= 1;
        }

    }

    @Override
    public PieceColor getColorToMove() {
        return colorToMove;
    }

    @Override
    public Square getKingLocation(PieceColor kingColor) {
        Square kingLocation = Square.OFF_BOARD;
        for (Square square : pieceEntryEnumMap.keySet()) {
            if (pieceAt(square).filter(piece -> piece.is(kingColor, PieceType.KING)).isPresent()) {
                kingLocation = square;
                break;
            }
        }
        return kingLocation;
    }

    @Override
    public Optional<Piece> pieceAt(Square square) {
        return Optional.ofNullable(getNullablePiece(square));
    }

    @Override
    public Piece getNullablePiece(Square square) {
        return pieceEntryEnumMap.get(square);
    }

    @Override
    public boolean hasCastled(PieceColor color) {
        return this.hasCastled.getOrDefault(color, false);
    }

    @Override
    public Optional<Square> enPassentTarget() {
        return Optional.ofNullable(enPassentTargets.getOrDefault(moveCount, null));
    }

    @Override
    public boolean isOccupied(Square square) {
        return pieceEntryEnumMap.containsKey(square);
    }

    @Override
    public int getMoveCount() {
        return moveCount;
    }

    @Override
    public List<Move> getMoves(MoveGenerationStrategy strategy) {

        MoveFactory moveFactory = new MoveFactory(this);

        MoveGenerator moveGenerator = moveGeneratorFactory.newInstance(strategy, moveFactory);

        List<Move> moves = new ArrayList<>();

        for (Square square : pieceEntryEnumMap.keySet()) {
            if (getNullablePiece(square).getColor().equals(colorToMove)) {
                moves.addAll(moveGenerator.generateMoves(this, square));
            }
        }

        return moves;
    }




    private boolean hasCastlingRight(PieceColor color, Square kingSquare, Square rookSquare) {

        int kingSquareIndex = getCastlingSquareIndex(kingSquare);
        int rookSquareIndex = getCastlingSquareIndex(rookSquare);

        return !hasCastled(color) &&
                castlingSquaresMoveCount[kingSquareIndex] == 0 &&
                castlingSquaresMoveCount[rookSquareIndex] == 0 &&
                pieceAt(kingSquare).filter(this::isKing).isPresent() &&
                pieceAt(rookSquare).filter(this::isRook).isPresent() ;

    }

    private boolean isKing(Piece piece) {
        return PieceType.KING.equals(piece.getType());
    }

    private boolean isRook(Piece piece) {
        return PieceType.ROOK.equals(piece.getType());
    }



    @Override
    public boolean hasKingSideCastlingRight(PieceColor pieceColor) {

        Square kingSquare;
        Square rookSquare;

        switch (pieceColor) {
            case BLACK:
                kingSquare = Square.E8;
                rookSquare = Square.H8;
                break;
            case WHITE:
                kingSquare = Square.E1;
                rookSquare = Square.H1;
                break;
            default:
                throw new AssertionError();
        }

        return hasCastlingRight(pieceColor, kingSquare, rookSquare);
    }

    @Override
    public boolean hasQueenSideCastlingRight(PieceColor pieceColor) {
        Square kingSquare;
        Square rookSquare;

        switch (pieceColor) {
            case BLACK:
                kingSquare = Square.E8;
                rookSquare = Square.A8;
                break;
            case WHITE:
                kingSquare = Square.E1;
                rookSquare = Square.A1;
                break;
            default:
                throw new AssertionError();
        }

        return hasCastlingRight(pieceColor, kingSquare, rookSquare);
    }


}
