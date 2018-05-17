package com.erikmafo.chess2.movegeneration;

import com.erikmafo.chess.board.KnightOffset;
import com.erikmafo.chess.board.BasicOffset;
import com.erikmafo.chess.board.CastlingRight;
import com.erikmafo.chess.board.Offset;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

/**
 *
 * @author erikmafo
 */
public class X88Board implements Board {

    private final Map<Square, Piece> pieces;
    private final Map<PieceType, MoveGenerator> moveGenerators;
    private final Stack<Move> moveHistory = new Stack<>();
    private final Map<PieceColor, CastlingRight> initialCastlingRightMap = new EnumMap<>(PieceColor.class);
    private final Map<PieceColor, Boolean> hasCastled = new EnumMap<>(PieceColor.class);

    /**
     * The initial squares for king and rook are referred to as castling squares. We need
     * to keep track of movement on these squares in order to decide castling rights.
     */
    private final int[] castlingSquaresMoveCount = new int[6];
    private PieceColor colorToMove = PieceColor.WHITE;
    private int moveCount;

    public X88Board(Map<Square, Piece> pieceConfig) {
        this(pieceConfig, PieceColor.WHITE);
    }

    public X88Board(Map<Square, Piece> pieces, PieceColor colorToMove) {
        this(pieces, colorToMove,null,  null);
    }

    public X88Board(Map<Square, Piece> pieces, PieceColor colorToMove, Map<PieceColor, CastlingRight> initialCastlingRightMap, Square enPassentTarget) {

        if (pieces.isEmpty()) {
            this.pieces = new EnumMap<>(Square.class);
        } else {
            this.pieces = new EnumMap<>(pieces);
        }

        this.moveGenerators = getPieceTypeMoveGeneratorMap();

        this.colorToMove = colorToMove;

        if (initialCastlingRightMap != null) {
            this.initialCastlingRightMap.putAll(initialCastlingRightMap);
        } else {
            initialCastlingRightMap = new EnumMap<>(PieceColor.class);
            initialCastlingRightMap.put(PieceColor.WHITE, CastlingRight.BOTH);
            initialCastlingRightMap.put(PieceColor.BLACK, CastlingRight.BOTH);
        }

        if(!isValidBoard()) {
            throw new AssertionError("Invalid state in board");
        }
    }

    @NotNull
    private Map<PieceType, MoveGenerator> getPieceTypeMoveGeneratorMap() {
        Map<PieceType, MoveGenerator> moveGenerators = new EnumMap<>(PieceType.class);
        moveGenerators.put(PieceType.PAWN, new PawnMoveGenerator());
        moveGenerators.put(PieceType.KNIGHT, new StandardMoveGenerator(false, KnightOffset.values()));
        moveGenerators.put(PieceType.ROOK, new StandardMoveGenerator(true, BasicOffset.rookValues()));
        moveGenerators.put(PieceType.BISHOP, new StandardMoveGenerator(true, BasicOffset.bishopValues()));
        moveGenerators.put(PieceType.QUEEN, new StandardMoveGenerator(true, BasicOffset.values()));
        moveGenerators.put(PieceType.KING, new KingMoveGenerator());
        return moveGenerators;
    }

    @Override
    public void play(Move move) {

        assert move.from().isOnBoard();
        assert move.to().isOnBoard();

        putPiece(move.to(), pieces.remove(move.from()));

        if (Move.Kind.KING_SIDE_CASTLE.equals(move.kind())){
            Square rookInitialSquare = move.from().next(BasicOffset.RIGHT, 3);
            Square rookTargetSquare = rookInitialSquare.next(BasicOffset.LEFT, 2);
            putPiece(rookTargetSquare, pieces.remove(rookInitialSquare));
            hasCastled.put(move.getMovingColor(), true);
        } else if (Move.Kind.QUEEN_SIDE_CASTLE.equals(move.kind())) {
            Square rookInitialSquare = move.from().next(BasicOffset.LEFT, 4);
            Square rookTargetSquare = rookInitialSquare.next(BasicOffset.RIGHT, 3);
            putPiece(rookTargetSquare, pieces.remove(rookInitialSquare));
            hasCastled.put(move.getMovingColor(), true);
        } else if (Move.Kind.EN_PASSENT.equals(move.kind())) {
            Offset offset = move.getMovingColor().isWhite() ? BasicOffset.DOWN : BasicOffset.UP;
            Square capturedPieceSquare = move.to().next(offset);
            Piece captured = pieces.remove(capturedPieceSquare);
            assert captured != null;
        } else if (Move.Kind.BISHOP_PROMOTION.equals(move.kind())) {
            putPiece(move.to(), new Piece(move.getMovingColor(), PieceType.BISHOP));
        } else if (Move.Kind.KNIGHT_PROMOTION.equals(move.kind())) {
            putPiece(move.to(), new Piece(move.getMovingColor(), PieceType.KNIGHT));
        } else if (Move.Kind.ROOK_PROMOTION.equals(move.kind())) {
            putPiece(move.to(), new Piece(move.getMovingColor(), PieceType.ROOK));
        } else if (Move.Kind.QUEEN_PROMOTION.equals(move.kind())) {
            putPiece(move.to(), new Piece(move.getMovingColor(), PieceType.QUEEN));
        }

        int castlingSquareIndex = toCastlingSquareIndex(move.to());

        if (castlingSquareIndex > 0) { // is castling square?
            castlingSquaresMoveCount[castlingSquareIndex]++;
        }

        colorToMove = colorToMove.opponent();
        moveHistory.push(move);
        moveCount++;

        assert isValidBoard() : move;
    }

    private void putPiece(@NotNull Square square, @NotNull Piece piece) {
        pieces.put(square, piece);
    }

    private boolean isValidBoard() {

        for (Square square : pieces.keySet()) {
            Piece piece = pieces.get(square);
            if (piece == null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void undoLastMove() {

        Move lastMove = moveHistory.pop();
        colorToMove = lastMove.getMovingColor();
        Piece piece = pieces.remove(lastMove.to());

        putPiece(lastMove.from(), piece);

        if (Move.Kind.EN_PASSENT.equals(lastMove.kind())) {
            Offset offset = lastMove.getMovingColor().isWhite() ? BasicOffset.DOWN : BasicOffset.UP;
            Square square = lastMove.to().next(offset);
            putPiece(square, new Piece(lastMove.getMovingColor().opponent(), PieceType.PAWN));
        } else if (lastMove.getCapturedPieceType() != null) {
            putPiece(lastMove.to(),
                    new Piece(lastMove.getMovingColor().opponent(), lastMove.getCapturedPieceType()));
        } else if (Move.Kind.KING_SIDE_CASTLE.equals(lastMove.kind())) {
            Square rookFromSquare = lastMove.from().next(BasicOffset.RIGHT);
            Square rookToSquare = rookFromSquare.next(BasicOffset.RIGHT, 2);
            putPiece(rookToSquare, pieces.remove(rookFromSquare));
            hasCastled.put(lastMove.getMovingColor(), false);
        } else if (Move.Kind.QUEEN_SIDE_CASTLE.equals(lastMove.kind())) {
            Square rookFromSquare = lastMove.from().next(BasicOffset.LEFT);
            Square rookTargetSquare = rookFromSquare.next(BasicOffset.LEFT, 3);
            putPiece(rookTargetSquare, pieces.remove(rookFromSquare));
            hasCastled.put(lastMove.getMovingColor(), false);
        } else if (isPawnPromotion(lastMove.kind())) {
            putPiece(lastMove.from(), new Piece(lastMove.getMovingColor(), PieceType.PAWN));
        }

        int castlingSquareIndex = toCastlingSquareIndex(lastMove.to());

        if (castlingSquareIndex > 0) { // is castling square?
            castlingSquaresMoveCount[castlingSquareIndex]--;
        }

        moveCount--;

        assert isValidBoard() : lastMove;
    }

    private boolean isPawnPromotion(Move.Kind kind) {
        return kind == Move.Kind.QUEEN_PROMOTION ||
                kind == Move.Kind.ROOK_PROMOTION ||
                kind == Move.Kind.BISHOP_PROMOTION ||
                kind == Move.Kind.KNIGHT_PROMOTION;
    }

    private Square getEnPassentTarget() {
        //return enPassentTargets[moveCount];
        if (moveHistory.empty()) {
            return null;
        }
        Move lastMove = moveHistory.peek();
        if (lastMove.kind() != Move.Kind.DOUBLE_PAWN_PUSH) {
            return null;
        }

        Offset pushedPawnDirection = lastMove.getMovingColor().isWhite() ? BasicOffset.UP : BasicOffset.DOWN;

        return lastMove.from().next(pushedPawnDirection);
    }

    @Contract(pure = true)
    private int toCastlingSquareIndex(@NotNull  Square square) {

        int index;

        switch (square) {
            case E1:
                index = 0;
                break;
            case H1:
                index = 1;
                break;
            case A1:
                index = 2;
                break;
            case E8:
                index = 3;
                break;
            case H8:
                index = 4;
                break;
            case A8:
                index = 5;
                break;
            default:
                index = -1;
                break;
        }

        return index;
    }

    @Override
    public PieceColor colorToMove() {
        return colorToMove;
    }

    @Override
    public Piece at(Square square) {
        return pieces.get(square);
    }

    @Override
    public boolean isCheck() {
        return isAttackedBy(colorToMove.opponent(), getKingLocation(colorToMove));
    }

    @Override
    public boolean isChecked(PieceColor color) {
        return isAttackedBy(color.opponent(), getKingLocation(color));
    }

    private final Offset[] knightOffsets = KnightOffset.values();
    private final Offset[] rookOffsets = BasicOffset.rookValues();
    private final Offset[] bishopOffsets = BasicOffset.bishopValues();
    private final Offset[] kingOffsets = BasicOffset.values();
    private final Offset[] queenOffsets = BasicOffset.values();
    private final Offset[] blackPawnOffsets = {BasicOffset.DOWN_LEFT, BasicOffset.DOWN_RIGHT};
    private final Offset[] whitePawnOffsets = {BasicOffset.UP_LEFT, BasicOffset.UP_RIGHT};

    private boolean isAttackedBy(PieceColor opponent, Square square) {

        boolean isAttacked;

        if (search(piece -> piece.is(opponent, PieceType.BISHOP), square, true, bishopOffsets) != null ||
                search(piece -> piece.is(opponent, PieceType.ROOK), square, true, rookOffsets) != null ||
                search(piece -> piece.is(opponent, PieceType.KNIGHT), square, false, knightOffsets) != null ||
                search(piece -> piece.is(opponent, PieceType.QUEEN), square, true, queenOffsets) != null ||
                search(piece -> piece.is(opponent, PieceType.KING), square, false, kingOffsets) != null) {
            isAttacked = true;
        } else {
            Offset[] pawnOffsets = opponent.isWhite() ? blackPawnOffsets : whitePawnOffsets;
            isAttacked = search(piece -> piece.is(opponent, PieceType.PAWN), square, false, pawnOffsets) != null;
        }

        return isAttacked;
    }

    public Piece search(Predicate<Piece> piecePredicate, Square start, boolean slide, Offset... offsets) {

        Piece result = null;

        for (Offset offset : offsets) {

            Square current = start.next(offset);

            while (current.isOnBoard()){

                Piece piece = pieces.get(current);
                if (piece != null && piecePredicate.test(piece)) {
                    result = piece;
                    break;
                }

                if (slide) {
                    if (piece != null) {
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

    private Square getKingLocation(PieceColor color) {
        Square kingLocation = Square.OFF_BOARD;
        for (Square square : pieces.keySet()) {
            Piece piece = pieces.get(square);
            if (piece != null && piece.is(color, PieceType.KING)) {
                kingLocation = square;
                break;
            }
        }
        return kingLocation;
    }

    @Override
    public boolean isStalemate() {

        return pieces.isEmpty();

    }

    @Override
    public CastlingRight getCastlingRigth(PieceColor color) {
        return null;
    }

    private boolean casCastle(PieceColor color, Square kingSquare, Square rookSquare, Move.Kind castlingKind) {

        CastlingRight initialCastlingRight = initialCastlingRightMap.get(color);

        if (Move.Kind.KING_SIDE_CASTLE == castlingKind) {
            if (initialCastlingRight == CastlingRight.NONE || initialCastlingRight == CastlingRight.QUEEN_SIDE) {
                return false;
            }

        } else if (Move.Kind.QUEEN_SIDE_CASTLE == castlingKind) {
            if (initialCastlingRight == CastlingRight.NONE || initialCastlingRight == CastlingRight.KING_SIDE) {
                return false;
            }
        } else {
            assert false : "Move kind should be either king side castle or queen side castle";
        }

        int kingSquareIndex = toCastlingSquareIndex(kingSquare);
        int rookSquareIndex = toCastlingSquareIndex(rookSquare);

        return !hasCastled.getOrDefault(color, false) &&
                castlingSquaresMoveCount[kingSquareIndex] == 0 &&
                castlingSquaresMoveCount[rookSquareIndex] == 0 &&
                at(kingSquare) != null && at(kingSquare).getType() == PieceType.KING &&
                at(rookSquare) != null && at(rookSquare).getType() == PieceType.ROOK;
    }

    @Override
    public long hash() {
        return 0;
    }

    @Override
    public List<Move> generateMoves() {

        List<Move> moves = new ArrayList<>();

        for (Square square : pieces.keySet()) {

            Piece piece = pieces.get(square);

            assert piece != null : "The piece map should never contain null";

            if (!colorToMove.equals(piece.getColor())) {
                continue;
            }

            MoveGenerator generator = moveGenerators.get(piece.getType());

            assert generator != null : "Each piece type should have a move generator";

            generator.generateMoves(moves, colorToMove, square);
        }

        assert moves.stream().allMatch(move -> pieces.containsKey(move.from()));

        return moves;
    }

    @Override
    public int count(PieceColor color, PieceType pieceType) {
        return 0;
    }

    @Override
    public Board deepCopy() {
        return null;
    }

    private static Move newSinglePawnPushMove(PieceColor color, Square from, Square to) {
        return new Move(color, PieceType.PAWN, Move.Kind.QUIET, from, to, null);
    }

    private static Move newDoublePawnPushMove(PieceColor color, Square from, Square to) {
        return new Move(color, PieceType.PAWN, Move.Kind.DOUBLE_PAWN_PUSH, from, to, null);
    }

    private static Offset getPawnDirection(PieceColor pieceColor) {
        return pieceColor.isWhite() ? BasicOffset.UP : BasicOffset.DOWN;
    }

    private static Move newQuietMove(PieceColor pieceColor, PieceType pieceType, Square from, Square target) {
        return new Move(pieceColor, pieceType, Move.Kind.QUIET, from, target);
    }

    private static Move newCaptureMove(PieceType pieceType, Square from, Square target, Piece targetPiece) {
        return new Move(targetPiece.getColor().opponent(), pieceType, Move.Kind.CAPTURE, from, target, targetPiece.getType());
    }

    private static int getSeventRank(PieceColor color) {
        return color.isWhite() ? 7 : 1;
    }

    private static int getSecondRank(PieceColor color) {
        return color.isWhite() ? 1 : 6;
    }

    private static int getLastRank(PieceColor color) {
        return color.isWhite() ? 7 : 0;
    }

    private static int getEnPassentRank(PieceColor color) {
        return color.isWhite() ? 4 : 3;
    }

    private interface MoveGenerator {
        void generateMoves(List<Move> appendTo, PieceColor pieceColor, Square from);
    }

    private class PawnMoveGenerator implements MoveGenerator {

        public void generateMoves(List<Move> appendTo, PieceColor colorToMove, Square from) {
            appendQuietMoves(appendTo, colorToMove, from);
            appendPawnAttackMoves(appendTo, colorToMove, from, getPawnAttackOffsets(colorToMove));
            appendEnPassentMove(appendTo, colorToMove, from);
        }

        private Offset[] getPawnAttackOffsets(PieceColor pawnColor) {
            return pawnColor.isWhite() ? whitePawnOffsets : blackPawnOffsets;
        }

        private void appendPawnAttackMoves(List<Move> moves, PieceColor color, Square from, Offset[] attackOffsets) {
            PieceColor opponent = color.opponent();
            for (Offset offset : attackOffsets) {
                Square target = from.next(offset);
                Piece capturedPiece = pieces.get(target);

                if (capturedPiece != null && capturedPiece.getColor().equals(opponent)) {
                    if (getLastRank(color) == target.getRank()) {
                        PieceType capturedPieceType = capturedPiece.getType();
                        moves.add(new Move(color, PieceType.PAWN, Move.Kind.KNIGHT_PROMOTION, from, target, capturedPieceType));
                        moves.add(new Move(color, PieceType.PAWN, Move.Kind.BISHOP_PROMOTION, from, target, capturedPieceType));
                        moves.add(new Move(color, PieceType.PAWN, Move.Kind.ROOK_PROMOTION, from, target, capturedPieceType));
                        moves.add(new Move(color, PieceType.PAWN, Move.Kind.QUEEN_PROMOTION, from, target, capturedPieceType));
                    } else {
                        moves.add(newCaptureMove(PieceType.PAWN, from, target, capturedPiece));
                    }
                }
            }
        }

        private void appendQuietMoves(List<Move> moves, PieceColor pawnColor, Square from) {

            // add single push
            boolean addedSingleNonPromotionPush = false;
            Square oneUp = from.next(getPawnDirection(pawnColor));
            if (oneUp.isOnBoard() && !pieces.containsKey(oneUp)) {
                if (getLastRank(pawnColor) == oneUp.getRank()) {
                    moves.add(new Move(pawnColor, PieceType.PAWN, Move.Kind.KNIGHT_PROMOTION, from, oneUp, null));
                    moves.add(new Move(pawnColor, PieceType.PAWN, Move.Kind.BISHOP_PROMOTION, from, oneUp, null));
                    moves.add(new Move(pawnColor, PieceType.PAWN, Move.Kind.ROOK_PROMOTION, from, oneUp, null));
                    moves.add(new Move(pawnColor, PieceType.PAWN, Move.Kind.QUEEN_PROMOTION, from, oneUp, null));
                } else {
                    moves.add(newSinglePawnPushMove(pawnColor, from, oneUp));
                    addedSingleNonPromotionPush = true;
                }
            }

            // add double push
            Square twoUp = oneUp.next(getPawnDirection(pawnColor));
            if (from.getRank() == getSecondRank(pawnColor) && addedSingleNonPromotionPush && !pieces.containsKey(twoUp)) {
                moves.add(newDoublePawnPushMove(pawnColor, from, twoUp));
            }
        }

        private void appendEnPassentMove(List<Move> moves, PieceColor color, Square from) {
            Square enPassentTarget = getEnPassentTarget();
            if (enPassentTarget != null && from.getRank() == getEnPassentRank(color)) {

                int fileDiff = enPassentTarget.getFile() - from.getFile();

                Piece captured = null;

                if (fileDiff == 1) {
                    captured = pieces.get(from.next(BasicOffset.RIGHT));
                } else if (fileDiff == -1) {
                    captured = pieces.get(from.next(BasicOffset.LEFT));
                }

                if (captured != null) {
                    moves.add(newEnPassentMove(color, from, enPassentTarget, captured));
                }
            }
        }
    }

    private Move newEnPassentMove(PieceColor color, Square from, Square enPassentTarget, Piece captured) {
        return new Move(color, PieceType.PAWN, Move.Kind.EN_PASSENT, from, enPassentTarget, PieceType.PAWN);
    }

    private class StandardMoveGenerator implements MoveGenerator {

        private final boolean slide;
        private final Offset[] attackOffsets;

        public StandardMoveGenerator(boolean slide, Offset[] attackOffsets) {
            this.slide = slide;
            this.attackOffsets = attackOffsets;
        }

        public void generateMoves(List<Move> appendTo, PieceColor pieceColor, Square from) {

            PieceType pieceType = pieces.get(from).getType();

            for (Offset offset : attackOffsets) {
                Square target = from.next(offset);
                while (target.isOnBoard()) {
                    Piece targetPiece = pieces.get(target);
                    if (targetPiece != null) {
                        if (targetPiece.getColor().equals(pieceColor.opponent())) {
                            appendTo.add(newCaptureMove(pieceType, from, target, targetPiece));
                        }
                        break;
                    } else {
                        appendTo.add(newQuietMove(pieceColor, pieceType, from, target));
                    }

                    if (slide) {
                        target = target.next(offset);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private class KingMoveGenerator implements MoveGenerator {

        private final MoveGenerator standarnMoveGenerator = new StandardMoveGenerator(false, BasicOffset.values());
        private final MoveGenerator castlingMoveGenerator = new CastlingMoveGenerator();

        @Override
        public void generateMoves(List<Move> appendTo, PieceColor pieceColor, Square from) {
            standarnMoveGenerator.generateMoves(appendTo, pieceColor, from);
            castlingMoveGenerator.generateMoves(appendTo, pieceColor, from);
        }
    }

    private class CastlingMoveGenerator implements MoveGenerator {

        private final Square[] whiteKingSideCastlingSquares;
        private final Square[] whiteQueenSideCastlingSquares;

        private final Square[] blackKingSideCastlingSquares;
        private final Square[] blackQueenSideCastlingSquares;

        public CastlingMoveGenerator() {
            whiteKingSideCastlingSquares = new Square[]{Square.F1, Square.G1};
            whiteQueenSideCastlingSquares = new Square[]{Square.D1, Square.C1, Square.B1};

            blackKingSideCastlingSquares = new Square[]{Square.F8, Square.G8};;
            blackQueenSideCastlingSquares = new Square[]{Square.D8, Square.C8, Square.B8};;
        }

        private boolean hasKingSideCastlingRight(PieceColor color) {
            Square kingSquare = color.isWhite() ? Square.E1 : Square.E8;
            Square rookSquare = kingSquare.next(BasicOffset.RIGHT, 3);
            return casCastle(color, kingSquare, rookSquare, Move.Kind.KING_SIDE_CASTLE);
        }

        private boolean hasQueenSideCastlingRight(PieceColor color) {
            Square kingSquare = color.isWhite() ? Square.E1 : Square.E8;
            Square rookSquare = kingSquare.next(BasicOffset.LEFT, 4);
            return casCastle(color, kingSquare, rookSquare, Move.Kind.QUEEN_SIDE_CASTLE);
        }

        private Square[] getKingSideCastlingSquares(PieceColor color) {
            return color.isWhite() ? whiteKingSideCastlingSquares : blackKingSideCastlingSquares;
        }

        private Square[] getQueenSideCastlingSquares(PieceColor color) {
            return color.isWhite() ? whiteQueenSideCastlingSquares : blackQueenSideCastlingSquares;
        }

        private Square getQueenSideCastlingTarget(PieceColor color) {
            return color.isWhite() ? Square.C1 : Square.C8;
        }

        private Square getKingSideCastlingTarget(PieceColor color) {
            return color.isWhite() ? Square.G1 : Square.G8;
        }

        private boolean isKingSideCastlingLegal(PieceColor color) {
            if (isCheck() || !hasKingSideCastlingRight(color)) {
                return false;
            }

            for (Square coordinate : getKingSideCastlingSquares(color)) {
                if (pieces.containsKey(coordinate)) {
                    return false;
                }
            }

            for (int i=0; i<2; i++) {
                Square sq = getKingSideCastlingSquares(color)[i];
                if (isAttackedBy(color.opponent(), sq)) {
                    return false;
                }
            }

            return true;
        }

        private boolean isQueenSideCastlingLegal(PieceColor color) {
            if (isCheck() || !hasQueenSideCastlingRight(color)) {
                return false;
            }

            for (Square coordinate : getQueenSideCastlingSquares(color)) {
                if (pieces.containsKey(coordinate)) {
                    return false;
                }
            }

            for (int i=0; i<2; i++) {
                Square sq = getQueenSideCastlingSquares(color)[i];
                if (isAttackedBy(color.opponent(), sq)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public void generateMoves(List<Move> appendTo, PieceColor color, Square from) {

            if (isKingSideCastlingLegal(color)) {
                appendTo.add(new Move(colorToMove, PieceType.KING, Move.Kind.KING_SIDE_CASTLE,
                        from, getKingSideCastlingTarget(colorToMove)));
            }

            if (isQueenSideCastlingLegal(color)) {
                appendTo.add(new Move(colorToMove, PieceType.KING, Move.Kind.QUEEN_SIDE_CASTLE,
                        from , getQueenSideCastlingTarget(color)));
            }
        }
    }
}
