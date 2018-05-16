package com.erikmafo.chess2.movegeneration;

import com.erikmafo.chess.board.BasicOffset;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static com.erikmafo.chess2.testingutils.IsMove.*;
import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class X88BoardTest {

    @Test
    @Parameters({
            "WHITE, D4, ROOK, G4, true",
            "WHITE, D4, ROOK, G3, false",
            "WHITE, E3, BISHOP, G5, true",
            "WHITE, C1, KNIGHT, D3, true",
            "WHITE, C1, KNIGHT, C3, false",
            "WHITE, C1, PAWN, B2, true",
            "WHITE, C1, PAWN, D2, true",
            "WHITE, C1, PAWN, C2, false"
    })
    public void isCheckShouldReturnTrueWhenKingIsAttacked(
            PieceColor kingColor,
            Square kingSquare,
            PieceType opponentPieceType,
            Square opponentSquare,
            boolean isChecked) {

        //Given
        Map<Square, Piece> map = new HashMap<>();
        map.put(kingSquare, new Piece(kingColor, PieceType.KING));
        map.put(opponentSquare, new Piece(kingColor.opponent(), opponentPieceType));
        Board board = new X88Board(map);

        //When
        boolean isCheck = board.isCheck();

        //Then
        assertThat("should return true/false according to whether king is attacked",
                isCheck, is(isChecked));
    }

    @Test
    public void shouldBringBackCapturedPieceAfterUndoingEnPassentMove() {

        //Given
        Map<Square, Piece> map = new HashMap<>();
        Piece whitePawn = new Piece(PieceColor.WHITE, PieceType.PAWN);
        Piece blackPawn = new Piece(PieceColor.BLACK, PieceType.PAWN);
        map.put(Square.C7, blackPawn);

        map.put(Square.D5, whitePawn);
        Board board = new X88Board(map);
        Move move = new Move(PieceColor.BLACK, PieceType.PAWN,
                Move.Kind.DOUBLE_PAWN_PUSH, Square.C7, Square.C5);

        //When
        board.play(move);
        board.play(new Move(PieceColor.WHITE, PieceType.PAWN, Move.Kind.EN_PASSENT, Square.D5, Square.C6));
        board.undoLastMove();

        //Then
        assertThat(board.at(Square.C5), is(blackPawn));
        assertThat(board.at(Square.D5), is(whitePawn));
    }

    @Test
    public void shouldRemoveCapturedPieceWhenPlayingEnPassentMove()  {

        //Given
        Map<Square, Piece> map = new HashMap<>();
        Piece whitePawn = new Piece(PieceColor.WHITE, PieceType.PAWN);
        Piece blackPawn = new Piece(PieceColor.BLACK, PieceType.PAWN);
        map.put(Square.C7, blackPawn);
        map.put(Square.D5, whitePawn);
        Board board = new X88Board(map);
        Move move = new Move(PieceColor.BLACK, PieceType.PAWN,
                Move.Kind.DOUBLE_PAWN_PUSH, Square.C7, Square.C5);

        //When
        board.play(move);
        board.play(new Move(PieceColor.WHITE, PieceType.PAWN, Move.Kind.EN_PASSENT, Square.D5, Square.C6));

        //Then
        assertThat(board.at(Square.C5), is(nullValue()));
        assertThat(board.at(Square.C6), is(whitePawn));
    }

    @Test
    public void shouldRemoveCapturedPiece() {

        //Given
        Map<Square, Piece> map = new HashMap<>();
        Piece whitePawn = new Piece(PieceColor.WHITE, PieceType.PAWN);
        Piece blackPawn = new Piece(PieceColor.BLACK, PieceType.PAWN);
        map.put(Square.D2, whitePawn);
        map.put(Square.C3, blackPawn);
        Board board = new X88Board(map);
        Move move = new Move(PieceColor.WHITE, PieceType.PAWN,
                Move.Kind.CAPTURE, Square.D2, Square.C3, PieceType.PAWN);

        //When
        board.play(move);

        //Then
        assertThat(board.at(Square.C3), is(whitePawn));
        assertThat(board.at(Square.D2), is(nullValue()));
    }

    @Test
    public void shouldMovePieceToExpectedSquareWhenMoveIsPlayed() {

        //Given
        Piece piece = new Piece(PieceColor.WHITE, PieceType.BISHOP);
        Square from = Square.E4;
        Square to = Square.H7;
        Map<Square, Piece> map = new HashMap<>();
        map.put(from, piece);
        Board board = new X88Board(map);

        //When
        board.play(new Move(piece, Move.Kind.QUIET, from, to));

        //Then
        assertThat(board.at(to), is(piece));
    }

    @Test
    public void shouldBringTheMovedPieceBackToPreviousSquareWhenUndoLastMove() {

        //Given
        Map<Square, Piece> map = new HashMap<>();
        Piece whiteBishop = new Piece(PieceColor.WHITE, PieceType.BISHOP);
        map.put(Square.E4, whiteBishop);
        Board board = new X88Board(map);

        //When
        board.play(new Move(PieceColor.WHITE, PieceType.BISHOP, Move.Kind.QUIET, Square.E4, Square.G6));
        board.undoLastMove();

        //Then
        assertThat(board.at(Square.E4), is(whiteBishop));
        assertThat(board.at(Square.G6), is(nullValue()));
    }

    @Test
    public void shouldBringBackCapturedPieceWhenUndoLastMove() {

        //Given
        Map<Square, Piece> map = new HashMap<>();
        Piece whitePawn = new Piece(PieceColor.WHITE, PieceType.PAWN);
        Piece blackPawn = new Piece(PieceColor.BLACK, PieceType.PAWN);
        map.put(Square.D2, whitePawn);
        map.put(Square.C3, blackPawn);
        Board board = new X88Board(map);
        Move move = new Move(PieceColor.WHITE, PieceType.PAWN,
                Move.Kind.CAPTURE, Square.D2, Square.C3, PieceType.PAWN);

        //When
        board.play(move);
        board.undoLastMove();

        //Then
        assertThat(board.at(Square.D2), is(whitePawn));
        assertThat(board.at(Square.C3), is(blackPawn));
    }

    @Test
    public void shouldGenerateEmptyMoveListWhenTheBoardIsEmpty() {

        //Given
        Board board = new X88Board(new HashMap<>());

        //When
        List<Move> moves = board.generateMoves();

        //Then
        assertThat(moves, is(emptyList()));
    }

    @Test
    public void isCheckShouldReturnFalseIfBoardIsEmpty() {

        //Given
        Board board = new X88Board(new HashMap<>());

        //Then
        assertFalse(board.isCheck());
    }

    @Test
    public void isStalemateShouldReturnTrueIfBoardIsEmpty() {

        //Given
        Board board = new X88Board(new HashMap<>());

        //Then
        assertTrue(board.isStalemate());
    }


    @Test
    public void shouldGenerateCorrectKnightMovesFromE4()  {

        //Given
        Map<Square, Piece> map = new HashMap<>();
        Piece whiteKnigth = new Piece(PieceColor.WHITE, PieceType.KNIGHT);
        map.put(Square.E4, whiteKnigth);
        Board board = new X88Board(map);

        //When
        List<Move> whiteMoves = board.generateMoves();

        //Then
        assertThat("should be correct number of moves", whiteMoves.size(), is(8));
        assertThat(whiteMoves, hasMoves(PieceColor.WHITE, PieceType.KNIGHT, Square.E4,
                Square.F2, Square.D2, Square.C3, Square.C5, Square.D6, Square.F6,
                Square.G5, Square.G3));
    }


    @Test
    public void shouldGenerateCorrectKnightMovesFromA1() {

        //Given
        Map<Square, Piece> map = new HashMap<>();
        Piece whiteKnigth = new Piece(PieceColor.WHITE, PieceType.KNIGHT);
        map.put(Square.A1, whiteKnigth);
        Board board = new X88Board(map);

        //When
        List<Move> whiteMoves = board.generateMoves();

        //Then
        assertThat("should be correct number of moves", whiteMoves.size(), is(2));
        assertThat(whiteMoves, hasMoves(PieceColor.WHITE, PieceType.KNIGHT, Square.A1, Square.B3, Square.C2));
    }


    @Test
    public void shouldGenerateCorrectKnightAttackMoves() {

        //Given
        Map<Square, Piece> map = new HashMap<>();
        Piece whiteKnigth = new Piece(PieceColor.WHITE, PieceType.KNIGHT);
        map.put(Square.A1, whiteKnigth);
        map.put(Square.B3, new Piece(PieceColor.BLACK, PieceType.PAWN));
        map.put(Square.C2, new Piece(PieceColor.BLACK, PieceType.PAWN));
        Board board = new X88Board(map);

        //When
        List<Move> whiteMoves = board.generateMoves();

        //Then
        assertThat("should be correct number of moves", whiteMoves.size(), is(2));
        assertThat(whiteMoves, hasItem(isCaptureMove(PieceColor.WHITE, PieceType.KNIGHT, Square.A1, Square.B3, PieceType.PAWN)));
        assertThat(whiteMoves, hasItem(isCaptureMove(PieceColor.WHITE, PieceType.KNIGHT, Square.A1, Square.C2, PieceType.PAWN)));

    }

    @Test
    public void shouldGenerateCorrectRookMoves() {

        //Given
        PieceColor movingColor = PieceColor.WHITE;
        Map<Square, Piece> map = new HashMap<>();
        Piece piece = new Piece(movingColor, PieceType.ROOK);
        map.put(Square.A1, piece);
        Board board = new X88Board(map, movingColor);

        //When
        List<Move> moves = board.generateMoves();

        //Then
        assertThat("should be correct number of moves", moves.size(), is(14));
        assertThat(moves, hasMoves(movingColor, PieceType.ROOK, Square.A1,
                Square.A2, Square.A3, Square.A4, Square.A5, Square.A6, Square.A7, Square.A8,
                Square.B1, Square.C1, Square.D1, Square.E1, Square.F1, Square.G1, Square.H1));
    }

    @Test
    public void shouldGenerateCorrectBishopMoves() {

        //Given
        PieceColor movingColor = PieceColor.WHITE;
        Piece piece = new Piece(movingColor, PieceType.BISHOP);
        Square from = Square.A1;
        Map<Square, Piece> map = new HashMap<>();
        map.put(from, piece);
        Board board = new X88Board(map, movingColor);

        //When
        List<Move> moves = board.generateMoves();

        //Then
        assertThat("should be correct number of moves", moves.size(), is(7));
        assertThat(moves, hasMoves(movingColor, PieceType.BISHOP, Square.A1,
                Square.B2, Square.C3, Square.D4, Square.E5, Square.F6, Square.G7, Square.H8));
    }


    @Test
    public void shouldGenerateCorrectKingMoves() {

        //Given
        PieceColor movingColor = PieceColor.WHITE;
        Piece piece = new Piece(movingColor, PieceType.KING);
        Square from = Square.E1;
        Map<Square, Piece> map = new HashMap<>();
        map.put(from, piece);
        Board board = new X88Board(map, movingColor);

        //When
        List<Move> moves = board.generateMoves();

        //Then
        assertThat("should be correct number of moves", moves.size(), is(5));
        assertThat(moves, hasMoves(movingColor, PieceType.KING, Square.E1,
                Square.D1, Square.D2, Square.E2, Square.F2, Square.F1));
    }

    @Test
    @Parameters({
            "WHITE, D2, D3, D4",
            "BLACK, D7, D6, D5"
    })
    public void shouldGenerateCorrectPawnMoves(PieceColor pawnColor,
                                               Square from,
                                               Square expectedSinglePush,
                                               Square expectedDoublePush) {
        //Given
        Map<Square, Piece> map = new HashMap<>();
        Piece piece = new Piece(pawnColor, PieceType.PAWN);
        map.put(from, piece);
        Board board = new X88Board(map, pawnColor);

        //When
        List<Move> moves = board.generateMoves();

        //Then
        assertThat("should be correct number of moves", moves.size(), is(2));
        assertThat(moves, hasItem(isSinglePawnPush(pawnColor, from, expectedSinglePush)));
        assertThat(moves, hasItem(isDoublePawnPush(pawnColor, from, expectedDoublePush)));
    }


    @Test
    public void shouldGenerateCorrectPawnAttackMoves() {

        //Given
        PieceColor pawnColor = PieceColor.WHITE;
        Square from = Square.E5;
        Square target1 = Square.D6;
        Square target2 = Square.F6;
        Map<Square, Piece> map = new HashMap<>();
        Piece pawn = new Piece(pawnColor, PieceType.PAWN);
        map.put(from, pawn);
        map.put(target1, new Piece(pawnColor.opponent(), PieceType.PAWN));
        map.put(target2, new Piece(pawnColor.opponent(), PieceType.PAWN));
        Board board = new X88Board(map, pawnColor);

        //When
        List<Move> moves = board.generateMoves();

        //Then
        assertThat("should be correct number of moves", moves.size(), is(3));
        assertThat(moves, hasItem(isCaptureMove(pawnColor, PieceType.PAWN, from, target1, PieceType.PAWN)));
        assertThat(moves, hasItem(isCaptureMove(pawnColor, PieceType.PAWN, from, target2, PieceType.PAWN)));
        assertThat(moves, hasItem(isSinglePawnPush(pawnColor, from, from.next(BasicOffset.UP))));
    }


    @Test
    public void shouldGenerateCorrectInitialPawnMoves() {

        //Given
        Map<Square, Piece> map = new HashMap<>();
        map.put(Square.H2, new Piece(PieceColor.WHITE, PieceType.PAWN));
        map.put(Square.A7, new Piece(PieceColor.BLACK, PieceType.PAWN));
        Board board = new X88Board(map);

        //When
        board.play(new Move(PieceColor.WHITE, PieceType.PAWN, Move.Kind.QUIET, Square.H2, Square.H3));
        board.play(new Move(PieceColor.BLACK, PieceType.PAWN, Move.Kind.DOUBLE_PAWN_PUSH, Square.A7, Square.A5));
        List<Move> movesWhite = board.generateMoves();
        board.play(new Move(PieceColor.WHITE, PieceType.PAWN, Move.Kind.QUIET, Square.H3, Square.H4));
        List<Move> movesBlack = board.generateMoves();

        //Then
        assertThat("should be correct number of moves", movesWhite.size(), is(1));
        assertThat(movesWhite, hasItem(isSinglePawnPush(PieceColor.WHITE, Square.H3, Square.H4)));
        assertThat(movesBlack.size(), is(1));
        assertThat(movesBlack, hasItem(isSinglePawnPush(PieceColor.BLACK, Square.A5, Square.A4)));
    }


    @Test
    public void shouldGenerateCorrectEnPassentMove() {

        //Given
        Map<Square, Piece> map = new HashMap<>();
        map.put(Square.C2, new Piece(PieceColor.WHITE, PieceType.PAWN));
        map.put(Square.D4, new Piece(PieceColor.BLACK, PieceType.PAWN));
        Board board = new X88Board(map);


        //When
        Move move = new Move(PieceColor.WHITE, PieceType.PAWN, Move.Kind.DOUBLE_PAWN_PUSH, Square.C2, Square.C4);
        board.play(move);
        List<Move> moves = board.generateMoves();

        //Then
        assertThat("should find correct number of moves", moves.size(), is(2));
        assertThat(moves, hasItem(isEnPassentMove(PieceColor.BLACK, Square.D4, Square.C3)));
        assertThat(moves, hasItem(isSinglePawnPush(PieceColor.BLACK, Square.D4, Square.D3)));

    }

    @Test
    public void shouldGenerateCorrectMovesWhenSlidingPieceIsBlockedBySameColor() {

        //Given
        PieceColor movingColor = PieceColor.WHITE;
        Piece piece = new Piece(movingColor, PieceType.BISHOP);
        Square from = Square.A1;
        Map<Square, Piece> map = new HashMap<>();
        map.put(from, piece);
        map.put(Square.B2, new Piece(PieceColor.WHITE, PieceType.PAWN));
        Board board = new X88Board(map, movingColor);

        //When
        List<Move> moves = board.generateMoves();


        //Then
        assertThat("should be correct number of moves", moves.size(), is(2));
        assertThat(moves, hasItem(isSinglePawnPush(PieceColor.WHITE, Square.B2, Square.B3)));
        assertThat(moves, hasItem(isDoublePawnPush(PieceColor.WHITE, Square.B2, Square.B4)));
    }


    @Test
    public void shouldGenerateCorrectMovesAfterUndoLastMove() {

        //Given
        PieceColor movingColor = PieceColor.WHITE;
        Map<Square, Piece> map = new HashMap<>();
        map.put(Square.B2, new Piece(PieceColor.WHITE, PieceType.PAWN));
        Board board = new X88Board(map, movingColor);

        //When
        board.play(new Move(movingColor, PieceType.PAWN, Move.Kind.QUIET, Square.B2, Square.B3));
        board.undoLastMove();
        List<Move> moves = board.generateMoves();


        //Then
        assertThat("should be correct number of moves", moves.size(), is(2));
        assertThat(moves, hasItem(isSinglePawnPush(PieceColor.WHITE, Square.B2, Square.B3)));
        assertThat(moves, hasItem(isDoublePawnPush(PieceColor.WHITE, Square.B2, Square.B4)));
    }

    @Test
    public void shouldGenerateCaptureMovesWhenQueenIsBlockedByOpponent() {

        //Given
        PieceColor movingColor = PieceColor.WHITE;
        Piece piece = new Piece(movingColor, PieceType.QUEEN);
        Square from = Square.E1;
        Map<Square, Piece> map = new HashMap<>();
        map.put(from, piece);
        map.put(Square.D1, new Piece(PieceColor.BLACK, PieceType.KNIGHT));
        map.put(Square.D2, new Piece(PieceColor.BLACK, PieceType.KNIGHT));
        map.put(Square.E2, new Piece(PieceColor.BLACK, PieceType.KNIGHT));
        map.put(Square.F2, new Piece(PieceColor.BLACK, PieceType.KNIGHT));
        map.put(Square.F1, new Piece(PieceColor.BLACK, PieceType.KNIGHT));
        Board board = new X88Board(map, movingColor);

        //When
        List<Move> moves = board.generateMoves();

        //Then
        assertThat("should be correct number of moves", moves.size(), is(5));
        assertThat(moves, hasItem(isCaptureMove(movingColor, PieceType.QUEEN, Square.E1, Square.D1, PieceType.KNIGHT)));
        assertThat(moves, hasItem(isCaptureMove(movingColor, PieceType.QUEEN, Square.E1, Square.D2, PieceType.KNIGHT)));
        assertThat(moves, hasItem(isCaptureMove(movingColor, PieceType.QUEEN, Square.E1, Square.E2, PieceType.KNIGHT)));
        assertThat(moves, hasItem(isCaptureMove(movingColor, PieceType.QUEEN, Square.E1, Square.F2, PieceType.KNIGHT)));
        assertThat(moves, hasItem(isCaptureMove(movingColor, PieceType.QUEEN, Square.E1, Square.F1, PieceType.KNIGHT)));
    }


    @Test
    @Parameters({
            "WHITE, E1, H1, KING_SIDE_CASTLE",
            "BLACK, E8, H8, KING_SIDE_CASTLE",
            "WHITE, E1, A1, QUEEN_SIDE_CASTLE",
            "BLACK, E8, A8, QUEEN_SIDE_CASTLE"
    })
    public void shouldGenerateCorrectCastlingMove(PieceColor color,
                                                  Square kingSquare,
                                                  Square rookSquare,
                                                  Move.Kind expectedMoveKind) {
        //Given
        Piece king = new Piece(color, PieceType.KING);
        Piece rook = new Piece(color, PieceType.ROOK);
        Map<Square, Piece> map = new HashMap<>();
        map.put(kingSquare, king);
        map.put(rookSquare, rook);
        Board board = new X88Board(map, color);

        //When
        List<Move> moves = board.generateMoves();

        //Then
        assertThat(moves, hasItem(isCastlingMove(color, expectedMoveKind)));
    }

    @Test
    @Parameters({
            "WHITE, E1, H1, E8",
            "WHITE, E1, H1, F8",
            "WHITE, E1, H1, G8",
            "WHITE, E1, A1, E8",
            "WHITE, E1, A1, D8",
            "WHITE, E1, A1, C8",
            "BLACK, E8, H8, E1",
            "BLACK, E8, H8, F1",
            "BLACK, E8, H8, G1",
            "BLACK, E8, A8, E1",
            "BLACK, E8, A8, D1",
            "BLACK, E8, A8, C1"
    })
    public void shouldNotGenerateCastlingMoveWhenOpponentAttacksSquareBetweenKingAndRook(PieceColor color,
                                                                                         Square kingSquare,
                                                                                         Square rookSquare,
                                                                                         Square opponentQueenSquare)  {
        //Given
        Piece king = new Piece(color, PieceType.KING);
        Piece rook = new Piece(color, PieceType.ROOK);
        Map<Square, Piece> map = new HashMap<>();
        map.put(kingSquare, king);
        map.put(rookSquare, rook);
        map.put(opponentQueenSquare, new Piece(color.opponent(), PieceType.QUEEN));
        Board board = new X88Board(map, color);

        //When
        List<Move> moves = board.generateMoves();

        //Then
        assertThat(moves, not(hasItem(isCastlingMove(color, Move.Kind.KING_SIDE_CASTLE))));
        assertThat(moves, not(hasItem(isCastlingMove(color, Move.Kind.QUEEN_SIDE_CASTLE))));
    }

    @Test
    @Parameters({
            "WHITE, A7, A8",
            "BLACK, A2, A1"
    })
    public void shouldFindPawnPromotionMovesWhenPawnCanMoveToLastRank(
            PieceColor color, Square from, Square to) {

        //Given
        Piece pawn = new Piece(color, PieceType.PAWN);
        Map<Square, Piece> map = new HashMap<>();
        map.put(from, pawn);
        Board board = new X88Board(map, color);

        //When
        List<Move> moves = board.generateMoves();

        //Then
        assertThat(moves, hasPromotionMoves(color, from, to, null));
        assertThat(moves.size(), is(4));
    }

    @Test
    public void shouldFindPawnPromotionMovesWhenPawnCanCaptureOnLastRank() {

        //Given
        Piece pawn = new Piece(PieceColor.WHITE, PieceType.PAWN);
        Map<Square, Piece> map = new HashMap<>();
        map.put(Square.E7, pawn);
        map.put(Square.E8, new Piece(PieceColor.BLACK, PieceType.BISHOP));
        map.put(Square.F8, new Piece(PieceColor.BLACK, PieceType.KNIGHT));
        Board board = new X88Board(map, PieceColor.WHITE);

        //When
        List<Move> moves = board.generateMoves();

        //Then
        assertThat(moves, hasPromotionMoves(PieceColor.WHITE, Square.E7, Square.F8, PieceType.KNIGHT));
        assertThat(moves.size(), is(4));
    }

    @Test
    public void shouldPlayPawnPromotionMoveCorrectly() throws Exception {

        //Given
        Piece pawn = new Piece(PieceColor.WHITE, PieceType.PAWN);
        Map<Square, Piece> map = new HashMap<>();
        map.put(Square.E7, pawn);
        Board board = new X88Board(map, PieceColor.WHITE);

        //When
        Move move = board.generateMoves()
                .stream().filter(m -> m.kind().equals(Move.Kind.KNIGHT_PROMOTION))
                .findFirst()
                .get();

        board.play(move);

        //Then
        assertThat(board.at(Square.E8).getColor(), is(PieceColor.WHITE));
        assertThat(board.at(Square.E8).getType(), is(PieceType.KNIGHT));
    }

    @Test
    public void shouldUndoPawnPromotionCorrectly() throws Exception {

        //Given
        Piece pawn = new Piece(PieceColor.WHITE, PieceType.PAWN);
        Map<Square, Piece> map = new HashMap<>();
        map.put(Square.E7, pawn);
        Board board = new X88Board(map, PieceColor.WHITE);

        //When
        Move move = board.generateMoves()
                .stream().filter(m -> m.kind().equals(Move.Kind.KNIGHT_PROMOTION))
                .findFirst()
                .get();

        board.play(move);
        board.undoLastMove();

        //Then
        assertThat(board.at(Square.E7), is(pawn));
    }
}