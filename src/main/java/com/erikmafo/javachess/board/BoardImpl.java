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


    private final Map<BoardCoordinate, Piece> pieceEntryEnumMap = new EnumMap<>(BoardCoordinate.class);

    private final Map<Integer, BoardCoordinate> enPassentTargets = new HashMap<>();

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
    private BoardCoordinate lastMoveTo;

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
    public void movePiece(BoardCoordinate from, BoardCoordinate to) {

        lastMovedPiece = pieceEntryEnumMap.remove(from);
        pieceEntryEnumMap.put(to, lastMovedPiece);
        lastMoveTo = to;

    }

    @Override
    public void remove(BoardCoordinate boardCoordinate) {
        pieceEntryEnumMap.remove(boardCoordinate);
    }

    @Override
    public void put(BoardCoordinate boardCoordinate, Piece piece) {
        pieceEntryEnumMap.put(boardCoordinate, piece);
    }

    @Override
    public void setEnPassentTarget(BoardCoordinate boardCoordinate) {
        enPassentTargets.put(moveCount + 1, boardCoordinate);
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


    private int getCastlingSquareIndex(BoardCoordinate castlingSquare) {

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

        int index = getCastlingSquareIndex(lastMoveTo);

        if (index > 0) {
            castlingSquaresMoveCount[index] -= 1;
        }

    }

    @Override
    public PieceColor getColorToMove() {
        return colorToMove;
    }

    @Override
    public BoardCoordinate getKingLocation(PieceColor kingColor) {
        BoardCoordinate kingLocation = BoardCoordinate.OFF_BOARD;
        for (BoardCoordinate square : pieceEntryEnumMap.keySet()) {
            if (pieceAt(square).filter(piece -> piece.is(kingColor, PieceType.KING)).isPresent()) {
                kingLocation = square;
                break;
            }
        }
        return kingLocation;
    }

    @Override
    public Optional<Piece> pieceAt(BoardCoordinate boardCoordinate) {
        return Optional.ofNullable(getNullablePiece(boardCoordinate));
    }

    @Override
    public Piece getNullablePiece(BoardCoordinate boardCoordinate) {
        return pieceEntryEnumMap.get(boardCoordinate);
    }

    @Override
    public boolean hasCastled(PieceColor color) {
        return this.hasCastled.getOrDefault(color, false);
    }

    @Override
    public Optional<BoardCoordinate> enPassentTarget() {
        return Optional.ofNullable(enPassentTargets.getOrDefault(moveCount, null));
    }

    @Override
    public boolean isOccupied(BoardCoordinate boardCoordinate) {
        return pieceEntryEnumMap.containsKey(boardCoordinate);
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

        for (BoardCoordinate square : pieceEntryEnumMap.keySet()) {
            if (getNullablePiece(square).getColor().equals(colorToMove)) {
                moves.addAll(moveGenerator.generateMoves(this, square));
            }
        }

        return moves;
    }




    private boolean hasCastlingRight(PieceColor color, BoardCoordinate kingSquare, BoardCoordinate rookSquare) {

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

        BoardCoordinate kingSquare;
        BoardCoordinate rookSquare;

        switch (pieceColor) {
            case BLACK:
                kingSquare = BoardCoordinate.E8;
                rookSquare = BoardCoordinate.H8;
                break;
            case WHITE:
                kingSquare = BoardCoordinate.E1;
                rookSquare = BoardCoordinate.H1;
                break;
            default:
                throw new AssertionError();
        }

        return hasCastlingRight(pieceColor, kingSquare, rookSquare);
    }

    @Override
    public boolean hasQueenSideCastlingRight(PieceColor pieceColor) {
        return false;
    }


}
