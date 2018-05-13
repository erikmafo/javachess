package com.erikmafo.chess2.testingutils;

import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import com.erikmafo.chess2.movegeneration.Move;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.mockito.AdditionalMatchers;

import java.util.Arrays;

import static org.hamcrest.core.IsCollectionContaining.hasItem;

/**
 * Created by erikmafo on 26.08.17.
 */
public class IsMove {


    private static Matcher<Move> getMatcher(PieceColor color, PieceType movingPiece, Move.Kind kind, Square from, Square to, PieceType captured) {

        return new TypeSafeMatcher<Move>() {
            @Override
            protected boolean matchesSafely(Move move) {

                return IsMove.matches(move, color, kind, movingPiece, from, to);
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("Move with moving color: ")
                        .appendValue(color)
                        .appendText(", piece type: ")
                        .appendValue(movingPiece)
                        .appendText(", from: ")
                        .appendValue(from)
                        .appendText(", to: ")
                        .appendValue(to);
            }
        };


    }

    private static boolean matches(Move move, PieceColor color, Move.Kind kind, PieceType movingPiece, Square from, Square to) {
        if (color != null && !color.equals(move.getMovingColor())) {
            return false;
        }

        if (kind != null && !kind.equals(move.kind())) {
            return false;
        }

        if (movingPiece != null && !movingPiece.equals(move.getMovingPieceType())) {
            return false;
        }

        if (from != null && !from.equals(move.from())) {
            return false;
        }

        if (to != null && !to.equals(move.to())) {
            return false;
        }

        return true;
    }

    public static Matcher<Move> isCastlingMove(PieceColor color, Move.Kind castlingMoveKind) {

        if (castlingMoveKind == Move.Kind.KING_SIDE_CASTLE) {
            Square from = color.isWhite() ? Square.E1 : Square.E8;
            Square to = color.isWhite() ? Square.G1 : Square.G8;

            return getMatcher(color, PieceType.KING, castlingMoveKind, from, to, null);
        } else if (castlingMoveKind == Move.Kind.QUEEN_SIDE_CASTLE) {
            Square from = color.isWhite() ? Square.E1 : Square.E8;
            Square to = color.isWhite() ? Square.C1 : Square.C8;
            return getMatcher(color, PieceType.KING, Move.Kind.QUEEN_SIDE_CASTLE, from, to, null);
        } else {
            throw new AssertionError();
        }
    }

    public static Matcher<Move> isEnPassentMove(PieceColor color, Square from, Square to) {
        return getMatcher(color, PieceType.PAWN, Move.Kind.EN_PASSENT, from, to, PieceType.PAWN);
    }


    public static Matcher<Move> isCaptureMove(PieceColor movingColor, PieceType movingPiece, Square from, Square to, PieceType captured) {
        return getMatcher(movingColor, movingPiece, Move.Kind.CAPTURE, from, to, captured);
    }


    public static Matcher<Move> isSinglePawnPush(PieceColor movingColor, Square from, Square to) {
        return getMatcher(movingColor, PieceType.PAWN, Move.Kind.QUIET, from, to, null);
    }

    public static Matcher<Move> isDoublePawnPush(PieceColor movingColor, Square from, Square to) {
        return getMatcher(movingColor, PieceType.PAWN, Move.Kind.DOUBLE_PAWN_PUSH, from, to, null);
    }

    public static Matcher<Move> isMove(PieceColor movingColor, PieceType pieceType, Square from, Square to) {
        return getMatcher(movingColor, pieceType, null, from, to, null);
    }

    public static Matcher<Move> isKnightPromotion(PieceColor color, Square from, Square to, PieceType captured) {
        return getMatcher(color, PieceType.PAWN, Move.Kind.KNIGHT_PROMOTION, from, to, captured);
    }

    public static Matcher<Move> isBishopPromotion(PieceColor color, Square from, Square to, PieceType captured) {
        return getMatcher(color, PieceType.PAWN, Move.Kind.BISHOP_PROMOTION, from, to, captured);
    }

    public static Matcher<Move> isRookPromotion(PieceColor color, Square from, Square to, PieceType captured) {
        return getMatcher(color, PieceType.PAWN, Move.Kind.ROOK_PROMOTION, from, to, captured);
    }

    public static Matcher<Move> isQueenPromotion(PieceColor color, Square from, Square to, PieceType captured) {
        return getMatcher(color, PieceType.PAWN, Move.Kind.QUEEN_PROMOTION, from, to, captured);
    }


    public static Matcher<Iterable<Move>> hasPromotionMoves(PieceColor color, Square from, Square to, PieceType captured) {
        return new TypeSafeMatcher<Iterable<Move>>() {
            @Override
            protected boolean matchesSafely(Iterable<Move> moves) {
                return hasItem(isBishopPromotion(color, from, to, captured)).matches(moves) &&
                        hasItem(isKnightPromotion(color, from, to, captured)).matches(moves) &&
                        hasItem(isRookPromotion(color, from, to, captured)).matches(moves) &&
                        hasItem(isQueenPromotion(color, from, to, captured)).matches(moves);
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("Promotion moves with moving color: ")
                        .appendValue(color)
                        .appendText(", from: ")
                        .appendValue(from)
                        .appendText(" to: ")
                        .appendValue(to)
                        .appendText(", captured piece: ")
                        .appendValue(captured);
            }
        };
    }


    public static Matcher<Iterable<Move>> hasMoves(PieceColor movingColor, PieceType pieceType, Square from, Square... to) {

        return new TypeSafeMatcher<Iterable<Move>>() {

            @Override
            protected boolean matchesSafely(Iterable<Move> moves) {
                boolean match = true;

                for (Square square : to) {

                    Matcher<Move> matcher = getMatcher(movingColor, pieceType, null, from, square, null);

                    if (!hasMatch(moves, matcher)) {
                        match = false;
                        break;
                    }
                }

                return match;
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("Moves with moving color: ")
                        .appendValue(movingColor)
                        .appendText(", piece type: ")
                        .appendValue(pieceType)
                        .appendText(", from: ")
                        .appendValue(from)
                        .appendText(" to squares: ")
                        .appendValueList("", ", ", "", Arrays.asList(to));
            }
        };
    }

    private static boolean hasMatch(Iterable<Move> moves, Matcher<Move> moveMatcher) {

        boolean res = false;

        for (Move move : moves) {
            if (moveMatcher.matches(move)) {
                res = true;
                break;
            }
        }

        return res;
    }
}
