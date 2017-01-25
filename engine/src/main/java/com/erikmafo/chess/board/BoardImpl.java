package com.erikmafo.chess.board;

import com.erikmafo.chess.movegenerator.MoveGeneratorFactory;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import org.jetbrains.annotations.Contract;

import java.util.*;

/**
 * Created by erikmafo on 19.11.16.
 */
public class BoardImpl implements Board {


    private static class CompleteMode {
        static final int PLAY = 1;
        static final int UNDO = -1;
    }


    private final Map<Square, Piece> pieceEntryEnumMap = new EnumMap<>(Square.class);

    private Square[] enPassentTargets = new Square[100];

    private final Map<PieceColor, Boolean> hasCastled = new EnumMap<>(PieceColor.class);

    private final int[] castlingSquaresMoveCount = new int[6];

    private final ZobristCalculator zobristCalculator;

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

    private Map<PieceColor, CastlingRight> initialCastlingRight = new EnumMap<>(PieceColor.class);

    BoardImpl() {
        this.zobristCalculator = new ZobristCalculator(1);
    }

    public BoardImpl(ZobristCalculator zobristCalculator) {
        this.zobristCalculator = zobristCalculator;
    }

    BoardImpl(ZobristCalculator zobristCalculator, PieceColor colorToMove, Map<PieceColor, CastlingRight> initialCastlingRight) {
        this.zobristCalculator = zobristCalculator;
        this.colorToMove = colorToMove;
        this.initialCastlingRight = initialCastlingRight;
    }

    @Override
    public void movePiece(Square from, Square to) {
        lastMovedPiece = remove(from);
        put(to, lastMovedPiece);
        lastMoveFrom = from;
        lastMoveTo = to;
    }

    @Override
    public Piece remove(Square square) {
        Piece piece = pieceEntryEnumMap.remove(square);
        zobristCalculator.shiftPiece(square, piece);
        return piece;
    }

    @Override
    public Piece put(Square square, Piece piece) {
        Piece prev = pieceEntryEnumMap.put(square, piece);
        if (prev != null) {
            zobristCalculator.shiftPiece(square, prev);
        }
        zobristCalculator.shiftPiece(square, piece);
        return prev;
    }

    @Override
    public void setEnPassentTarget(Square square) {

        zobristCalculator.shiftEnPassentTarget(square);

        if (moveCount + 3 > enPassentTargets.length) {
            enPassentTargets = Arrays.copyOf(enPassentTargets, enPassentTargets.length + 100);
        }

        enPassentTargets[moveCount + 1] = square;
    }

    @Override
    public void removeEnPassentTarget() {
        Square prev = enPassentTargets[moveCount + 1];
        enPassentTargets[moveCount + 1] = null;
        if (prev != null) {
            zobristCalculator.shiftEnPassentTarget(prev);
        }
    }

    @Override
    public void setHasCastled(PieceColor color, boolean hasCastled) {

        this.hasCastled.put(color, hasCastled);

    }

    @Override
    public void resetHalfMoveClock() {

    }


    private void complete(int completeMode) {

        moveCount += completeMode;

        int castlingSquareIndex = getCastlingSquareIndex(completeMode > 0 ? lastMoveTo : lastMoveFrom);

        if (castlingSquareIndex > 0) {
            castlingSquaresMoveCount[castlingSquareIndex] += completeMode;
        }

        toggleColorToMove();
    }

    @Override
    public void completePlay() {
        complete(CompleteMode.PLAY);
    }


    @Override
    public void completeUndo() {
        complete(CompleteMode.UNDO);
    }


    @Contract(pure = true)
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
        zobristCalculator.shiftColorToMove();
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
        return Optional.ofNullable(enPassentTargets[moveCount]);
    }

    @Override
    public boolean isOccupied(Square square) {
        return pieceEntryEnumMap.containsKey(square);
    }

    @Override
    public int getMoveCount() {
        return moveCount;
    }

    private boolean hasCastlingRight(PieceColor color, Square kingSquare, Square rookSquare) {

        int kingSquareIndex = getCastlingSquareIndex(kingSquare);
        int rookSquareIndex = getCastlingSquareIndex(rookSquare);

        return !hasCastled(color) &&
                castlingSquaresMoveCount[kingSquareIndex] == 0 &&
                castlingSquaresMoveCount[rookSquareIndex] == 0 &&
                pieceAt(kingSquare).filter(this::isKing).isPresent() &&
                pieceAt(rookSquare).filter(this::isRook).isPresent();

    }

    private boolean isKing(Piece piece) {
        return PieceType.KING.equals(piece.getType());
    }

    private boolean isRook(Piece piece) {
        return PieceType.ROOK.equals(piece.getType());
    }


    @Override
    public boolean hasKingSideCastlingRight(PieceColor pieceColor) {
        Square kingSquare = pieceColor.isWhite() ? Square.E1 : Square.E8;
        Square rookSquare = kingSquare.next(BasicOffset.RIGHT, 3);
        return hasCastlingRight(pieceColor, kingSquare, rookSquare);
    }

    @Override
    public boolean hasQueenSideCastlingRight(PieceColor pieceColor) {
        Square kingSquare = pieceColor.isWhite() ? Square.E1 : Square.E8;
        Square rookSquare = kingSquare.next(BasicOffset.LEFT, 4);
        return hasCastlingRight(pieceColor, kingSquare, rookSquare);
    }

    @Override
    public Collection<Square> getOccupiedSquares() {

        return new HashSet<>(pieceEntryEnumMap.keySet());

    }


    private class BordIterator<T> implements Iterator<T> {

        private final Iterator<T> iterator;

        private BordIterator(Iterator<T> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            return iterator.next();
        }
    }

    @Override
    public Iterator<Square> occupiedSquareIterator() {
        return new BordIterator<>(pieceEntryEnumMap.keySet().iterator());
    }

    @Override
    public Iterator<Piece> pieceIterator() {
        return new BordIterator<>(pieceEntryEnumMap.values().iterator());
    }


    @Override
    public long getTranspositionKey() {
        return zobristCalculator.getValue();
    }
}
