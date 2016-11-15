package com.erikmafo.javachess.boardrep;

import com.erikmafo.javachess.pieces.CastlingPiece;
import com.erikmafo.javachess.pieces.PieceType;
import com.erikmafo.javachess.moves.Move;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.Piece;

import java.util.*;

import static com.erikmafo.javachess.pieces.PieceColor.BLACK;
import static com.erikmafo.javachess.pieces.PieceColor.WHITE;


public class Board implements ReadableBoard, MoveTarget, PlayableBoard {


    private final Piece[] whitePieceEntries, blackPieceEntries;
    private final CastlingPiece blackKing, whiteKing;
    private final CastlingPiece whiteKingSideRook, whiteQueenSideRook, blackKingSideRook, blackQueenSideRook;

    private final Map<BoardCoordinate, Piece> pieceEntryEnumMap = new EnumMap<>(BoardCoordinate.class);

    private final List<Move> moveHistory = new LinkedList<>();
    private final LinkedList<Piece> capturedPieces = new LinkedList<>();
    private boolean whiteHasCastled;
    private boolean blackHasCastled;

    public static Board createInstance() {
        return new Board();
    }


    // ------- ReadableBoard interface -------

    @Override
    public final boolean isOccupiedAt(BoardCoordinate location) {
        return pieceEntryEnumMap.get(location) != null;
    }

    @Override
    public final PieceColor getMovingColor() {
        return moveHistory.size() % 2 == 0 ? WHITE : BLACK;
    }

    @Override
    public final PieceColor getPieceColorAt(BoardCoordinate location) {
        return pieceEntryEnumMap.get(location).getPieceColor();
    }

    @Override
    public final PieceType getPieceTypeAt(BoardCoordinate location) {
        return pieceEntryEnumMap.get(location).getPieceType();
    }

    @Override
    public BoardCoordinate getKingLocation(PieceColor color) {
        return color == PieceColor.WHITE ? whiteKing.getCoordinate() : blackKing.getCoordinate();
    }

    @Override
    public Move getLastMove() {
        if (moveHistory.isEmpty()) {
            return null;
        }
        return moveHistory.get(moveHistory.size() - 1);
    }


    @Override
    public int getAttackedSquaresCount(PieceColor color) {
        int sum = 0;
        for (Piece piece : getPieceEntriesHeldBy(color)) {
            sum += piece.getNumberOfAttacks();
        }
        return sum;
    }

    // ------- MoveTarget Interface --------


    @Override
    public void movePiece(BoardCoordinate from, BoardCoordinate to) {
        pieceEntryEnumMap.put(to, pieceEntryEnumMap.get(from));
        pieceEntryEnumMap.get(to).move(to);
        pieceEntryEnumMap.remove(from);

        updateAttackTables(from, to);
    }


    @Override
    public void movePieceBackwards(BoardCoordinate from, BoardCoordinate to) {
        pieceEntryEnumMap.put(to, pieceEntryEnumMap.get(from));
        pieceEntryEnumMap.get(to).moveBackwards(to);
        pieceEntryEnumMap.remove(from);

        updateAttackTables(from, to);
    }

    @Override
    public void movePieceAndDoCapture(BoardCoordinate from, BoardCoordinate to,
                                      BoardCoordinate capturePieceLocation) {

        // do capture
        Piece capturedPiece = pieceEntryEnumMap.get(capturePieceLocation);
        if (capturedPiece == null) {
            String msg = "Attempted to capture EMPTY at coordinate " +
                    to + " Moving color: " + getMovingColor() + ".";
            throw new IllegalStateException(msg);
        }
        capturedPieces.add(capturedPiece);
        capturedPiece.capture();
        pieceEntryEnumMap.remove(capturePieceLocation);
        // move piece
        pieceEntryEnumMap.put(to, pieceEntryEnumMap.get(from));
        pieceEntryEnumMap.get(to).move(to);
        pieceEntryEnumMap.remove(from);


        updateAttackTables(from, to);
    }

    @Override
    public void undoLastCapture() {
        if (capturedPieces.isEmpty()) {
            throw new IllegalStateException("Attempted to uncapture piece when no captured pieces exist.");
        }
        Piece lastCaptured = capturedPieces.getLast();
        lastCaptured.unCapture();
        pieceEntryEnumMap.put(lastCaptured.getCoordinate(), lastCaptured);
        capturedPieces.removeLast();
        updateAttackTables(lastCaptured.getCoordinate());
    }

    @Override
    public void promotePawn(BoardCoordinate boardCoordinate, PieceType newPieceType) {
        pieceEntryEnumMap.get(boardCoordinate).setPieceType(newPieceType);
    }


    @Override
    public void setHasCastled(PieceColor color, boolean hasCastled) {
        if (color.isWhite()) {
            whiteHasCastled = hasCastled;
        } else {
            blackHasCastled = hasCastled;
        }
    }


    private Piece[] getPieceEntriesHeldBy(PieceColor color) {
        return color == WHITE ? whitePieceEntries : blackPieceEntries;
    }


    // ------- PlayableBoard Interface -------


    @Override
    public void undoLast() {
        if (!moveHistory.isEmpty()) {
            getLastMove().rewind(this);
            moveHistory.remove(moveHistory.size() - 1);
        } else {
            throw new IllegalStateException("Cannot undo last since no moves have been played");
        }

    }

    @Override
    public void play(Move move) {
        move.execute(this);
        moveHistory.add(move);
    }

    public int sumMaterial(PieceColor color) {
        int sum = 0;
        for (Piece piece : getPieceEntriesHeldBy(color)) {
            if (!piece.isCaptured()) {
                sum += piece.getPieceType().getValue();
            }
        }
        return sum;
    }

    // move generation
    @Override
    public void fillWithPossibleMoves(List<Move> moves) {
        Piece[] pieceEntries = getPieceEntriesHeldBy(getMovingColor());
        for (Piece piece : pieceEntries) {
            if (piece.isCaptured()) {
                continue;
            }
            piece.findPossibleMoves(this, moves);
        }
    }

    @Override
    public List<Move> getPossibleMoves() {

        List<Move> moves = new ArrayList<>();

        Piece[] pieceEntries = getPieceEntriesHeldBy(getMovingColor());
        for (Piece piece : pieceEntries) {
            if (!piece.isCaptured()) {
                piece.findPossibleMoves(this, moves);
            }
        }

        return moves;

    }

    @Override
    public boolean isChecked(PieceColor color) {
        for (Piece piece : getPieceEntriesHeldBy(color.getOpposite())) {
            if (!piece.isCaptured() && piece.attacksPieceAt(getKingLocation(color))) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean isKingSideCastlingPossible(PieceColor color) {
        boolean isPossible;
        if (color.isWhite()) {
            isPossible = !whiteKing.hasMoved() && !whiteKingSideRook.hasMoved() && !whiteKingSideRook.isCaptured();
        } else {
            isPossible = !blackKing.hasMoved() && !blackKingSideRook.hasMoved() && !blackKingSideRook.isCaptured();
        }
        return isPossible;
    }

    @Override
    public boolean isQueenSideCastlingPossible(PieceColor color) {
        boolean isPossible;
        if (color.isWhite()) {
            isPossible = !whiteKing.hasMoved() && !whiteQueenSideRook.hasMoved() && !whiteQueenSideRook.isCaptured();
        } else {
            isPossible = !blackKing.hasMoved() && !blackQueenSideRook.hasMoved() && !blackQueenSideRook.isCaptured();
        }
        return isPossible;
    }

    @Override
    public boolean hasCastled(PieceColor color) {
        return color.isWhite() ? whiteHasCastled : blackHasCastled;
    }

    public boolean isAttacked(PieceColor color, BoardCoordinate coordinate) {
        for (Piece piece : getPieceEntriesHeldBy(color.getOpposite())) {
            if (!piece.isCaptured() && piece.attacksSquareAt(coordinate)) {
                return true;
            }
        }
        return false;
    }

    // constructor


    // ------ move validation -------

    public void search(Collection<BoardCoordinate> results, BoardCoordinate start, Offset[] offsets) {
        for (Offset offset : offsets) {
            BoardCoordinate current = start.next(offset);
            if (current.isOnBoard() && isOccupiedAt(current)) {
                results.add(current);
            }
        }
    }


    public void slideSearch(Collection<BoardCoordinate> results, BoardCoordinate start, Set<Offset> offsets) {
        for (Offset offset : offsets) {
            BoardCoordinate current = start.next(offset);
            while (current.isOnBoard()) {
                if (isOccupiedAt(current)) {
                    results.add(current);
                    break;
                }
                current = current.next(offset);
            }

        }
    }


    private void updateAttackTables(BoardCoordinate... coordinates) {
        Set<BoardCoordinate> results = new HashSet<>();

        for (BoardCoordinate coordinate : coordinates) {
            search(results, coordinate, Offset.values());
            slideSearch(results, coordinate, Offset.getQueenAttackOffsets());
        }

        for (BoardCoordinate coordinate : results) {
            pieceEntryEnumMap.get(coordinate).updateAttackTable(this);
        }
    }


    /**
     * Creates a new board with the standard starting position.
     */
    public Board() {

        whitePieceEntries = new Piece[16];

        whiteKing = new CastlingPiece(PieceColor.WHITE, PieceType.KING, BoardCoordinate.E1);
        whiteKingSideRook = new CastlingPiece(PieceColor.WHITE, PieceType.ROOK, BoardCoordinate.H1);
        whiteQueenSideRook = new CastlingPiece(PieceColor.WHITE, PieceType.ROOK, BoardCoordinate.A1);

        whitePieceEntries[0] = whiteKing;
        whitePieceEntries[1] = new Piece(PieceColor.WHITE, PieceType.QUEEN, BoardCoordinate.D1);
        whitePieceEntries[2] = new Piece(PieceColor.WHITE, PieceType.BISHOP, BoardCoordinate.C1);
        whitePieceEntries[3] = new Piece(PieceColor.WHITE, PieceType.BISHOP, BoardCoordinate.F1);
        whitePieceEntries[4] = new Piece(PieceColor.WHITE, PieceType.KNIGHT, BoardCoordinate.B1);
        whitePieceEntries[5] = new Piece(PieceColor.WHITE, PieceType.KNIGHT, BoardCoordinate.G1);
        whitePieceEntries[6] = whiteKingSideRook;
        whitePieceEntries[7] = whiteQueenSideRook;
        BoardCoordinate curr = BoardCoordinate.A2;
        for (int i = 0; i < 8; i++) {
            whitePieceEntries[8 + i] = new Piece(PieceColor.WHITE, PieceType.PAWN, curr);
            curr = curr.next(Offset.RIGHT);
        }

        blackPieceEntries = new Piece[16];
        blackKing = new CastlingPiece(PieceColor.BLACK, PieceType.KING, BoardCoordinate.E8);
        blackKingSideRook = new CastlingPiece(PieceColor.BLACK, PieceType.ROOK, BoardCoordinate.H8);
        blackQueenSideRook = new CastlingPiece(PieceColor.BLACK, PieceType.ROOK, BoardCoordinate.A8);

        blackPieceEntries[0] = blackKing;
        blackPieceEntries[1] = new Piece(PieceColor.BLACK, PieceType.QUEEN, BoardCoordinate.D8);
        blackPieceEntries[2] = new Piece(PieceColor.BLACK, PieceType.BISHOP, BoardCoordinate.C8);
        blackPieceEntries[3] = new Piece(PieceColor.BLACK, PieceType.BISHOP, BoardCoordinate.F8);
        blackPieceEntries[4] = new Piece(PieceColor.BLACK, PieceType.KNIGHT, BoardCoordinate.B8);
        blackPieceEntries[5] = new Piece(PieceColor.BLACK, PieceType.KNIGHT, BoardCoordinate.G8);
        blackPieceEntries[6] = blackKingSideRook;
        blackPieceEntries[7] = blackQueenSideRook;

        curr = BoardCoordinate.A7;
        for (int i = 0; i < 8; i++) {
            blackPieceEntries[8 + i] = new Piece(PieceColor.BLACK, PieceType.PAWN, curr);
            curr = curr.next(Offset.RIGHT);
        }

        for (Piece piece : whitePieceEntries) {
            pieceEntryEnumMap.put(piece.getCoordinate(), piece);
        }

        for (Piece piece : blackPieceEntries) {
            pieceEntryEnumMap.put(piece.getCoordinate(), piece);
        }


        //update attack tables

        updateAttackTables();


    }


    private void updateAttackTables() {
        for (Piece piece : whitePieceEntries) {
            piece.updateAttackTable(this);
        }

        for (Piece piece : blackPieceEntries) {
            piece.updateAttackTable(this);
        }
    }

}
