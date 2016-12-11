package com.erikmafo.javachess.parser;


import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.Optional;

/**
 * Created by erikf on 16.07.2016.
 */
public class MoveParser {


    public MoveParser(MoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    public enum Format {

        LONG_ALGEBRAIC

    }

    public enum MoveEncoding {
        EN_PASSENT,
        KING_CASTLING,
        QUEEN_CASTLING,
        CAPTURE,
        QUIET,
        DOUBLE_PAWN_PUSH
    }


    private final MoveFactory moveFactory;

    private final static String FILES = "abcdefgh";
    private final static String RANKS = "12345678";

    public Move parse(Board board, String moveString, Format format) throws InvalidMoveException {

        Move move;

        switch (format) {
            case LONG_ALGEBRAIC:
                move = parseFromLongAlgebraic(board, moveString);
                break;
            default:
                throw new AssertionError("Unsupported format " + format);
        }

        return move;
    }


    private Move parseFromLongAlgebraic(Board board, String longAlgebraicMove) throws MoveFormatException, InvalidMoveException {

        String moveString = longAlgebraicMove.toUpperCase();

        if (moveString.length() != 4) {
            throw new MoveFormatException("Long algebraic move must consist of 4 characters");
        }

        String from = moveString.substring(0, 2);

        String to = moveString.substring(2);

        Square fromSquare = getSquare(from);

        Square toSquare = getSquare(to);

        MoveEncoding moveEncoding = determineEncoding(board, fromSquare, toSquare);

        Move move = getMove(board, fromSquare, toSquare, moveEncoding);

        return move;
    }

    private Square getSquare(String sq) {
        try {
            return Square.valueOf(sq);
        } catch (IllegalArgumentException ex) {
            throw new MoveFormatException("No such square: " + sq, ex);
        }
    }




    private Move getMove(Board board, Square fromSquare, Square toSquare, MoveEncoding moveEncoding) {
        Move move = null;

        switch (moveEncoding) {

            case EN_PASSENT:
                Optional<Square> enPassentTarget = board.enPassentTarget();
                if (enPassentTarget.isPresent()) {
                    Piece captured = board.getNullablePiece(enPassentTarget.get());
                    move = moveFactory.newEnPassentMove(board, fromSquare, toSquare, captured);
                }
                break;
            case KING_CASTLING:
                if (fromSquare.getRank() == 0) {
                    move = moveFactory.newCastlingMove(board, fromSquare, toSquare, Square.H1, Square.F1);
                } else {
                    move = moveFactory.newCastlingMove(board, fromSquare, toSquare, Square.H8, Square.F8);
                }
                break;
            case QUEEN_CASTLING:
                if (fromSquare.getRank() == 0) {
                    move = moveFactory.newCastlingMove(board, fromSquare, toSquare, Square.A1, Square.D1);
                } else {
                    move = moveFactory.newCastlingMove(board, fromSquare, toSquare, Square.A8, Square.D8);
                }
                break;
            case CAPTURE:
                move = moveFactory.newCaptureMove(board, fromSquare, toSquare, board.getNullablePiece(toSquare));
                break;
            case QUIET:
                move = moveFactory.newQuietMove(board, fromSquare, toSquare);
                break;
            case DOUBLE_PAWN_PUSH:
                move = moveFactory.newDoublePawnPushMove(board, fromSquare, toSquare);
                break;
        }
        return move;
    }


    private MoveEncoding determineEncoding(Board board, Square from, Square to) throws MoveFormatException, InvalidMoveException {

        MoveEncoding result = MoveEncoding.QUIET;

        Piece piece = board.getNullablePiece(from);

        if (piece == null) {
            throw new InvalidMoveException("No piece is present at " + from); // No piece at from-square. Default to quiet move.
        }

        int absRankDiff = Math.abs(to.getRank() - from.getRank());
        int fileDiff = to.getFile() - from.getFile();

        if (board.isOccupied(to)) {
            result = MoveEncoding.CAPTURE;
        } else if (PieceType.PAWN.equals(piece.getType())) {

            if (absRankDiff > 1) {
                result = MoveEncoding.DOUBLE_PAWN_PUSH;
            } else if (fileDiff != 0) {
                result = MoveEncoding.EN_PASSENT;
            }

        } else if (PieceType.KING.equals(piece.getType())) {

            if (fileDiff > 1) {
                result = MoveEncoding.KING_CASTLING;
            } else if (fileDiff < -1) {
                result = MoveEncoding.QUEEN_CASTLING;
            }

        }

        return result;

    }



}
