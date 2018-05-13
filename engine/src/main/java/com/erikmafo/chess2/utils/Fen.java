package com.erikmafo.chess2.utils;

import com.erikmafo.chess.board.CastlingRight;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import com.erikmafo.chess.utils.parser.FenParseException;
import com.erikmafo.chess2.movegeneration.Board;
import com.erikmafo.chess2.movegeneration.X88Board;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by erikmafo on 20.11.17.
 */
public final class Fen {

    private Fen() {}

    public static Board toX88Board(String fen) throws FenParseException {

        FenParseResult result = parse(fen);

        return new X88Board(result.getPiecePlacement(), result.getColorToMove(), result.getCastlingRighs(), result.getEnPassentTarget());
    }



    @NotNull
    public static FenParseResult parse(String fen) throws FenParseException {

        String[] fenParts = fen.split("\\s+");

        //if (fenParts.length != 6) {
        //    throw new FenParseException("FEN string must have 6 parts separated by whitespace.");
        //}

        Map<Square, Piece> pieceMap = parsePiecePlacement(fenParts[0]);
        PieceColor colorToMove = parseActiveColor(fenParts[1]);
        Map<PieceColor, CastlingRight> castlingRightMap =  parseCastlingAvailability(fenParts[2]);
        Square enPassentTarget = parseEnPassentTarget(fenParts[3]);

        return new FenParseResult(pieceMap, castlingRightMap, colorToMove, enPassentTarget);
    }

    private static PieceColor parseActiveColor(String fenActiveColor) throws FenParseException {
        if ("w".equalsIgnoreCase(fenActiveColor)) {
            return PieceColor.WHITE;
        } else if ("b".equalsIgnoreCase(fenActiveColor)) {
            return PieceColor.BLACK;
        } else {
            throw new FenParseException("Second part of fen string should be 'w' or 'b', but was " + fenActiveColor);
        }
    }


    private static Map<Square, Piece> parsePiecePlacement(String fenPiecePlacement) throws FenParseException {

        Map<Square, Piece> pieceMap = new HashMap<>();
        String[] ranks = fenPiecePlacement.split("/");
        for (int i = 0; i < ranks.length; i++) {
            String rank = ranks[i];
            int fileIndex = -1;
            for (int j = 0; j < rank.length(); j++) {
                char token = rank.charAt(j);
                if (Character.isAlphabetic(token)) {
                    fileIndex += 1;
                    putPiece(token, fileIndex, 7 - i, pieceMap);
                } else if (Character.isDigit(token)) {
                    fileIndex += Character.getNumericValue(token); // TODO test
                    // if valid
                    // number
                } else {
                    throw new FenParseException("Invalid character " + token);
                }
            }
        }

        return pieceMap;

    }

    private static void putPiece(char token, int fileIndex, int rankIndex, Map<Square, Piece> builder) throws FenParseException {

        PieceColor color = Character.isUpperCase(token) ? PieceColor.WHITE : PieceColor.BLACK;

        Square coordinate = Square.valueOf(fileIndex, rankIndex);

        switch (Character.toLowerCase(token)) {
            case 'p':
                builder.put(coordinate, new Piece(color, PieceType.PAWN));
                break;
            case 'r':
                builder.put(coordinate, new Piece(color, PieceType.ROOK));
                break;
            case 'n':
                builder.put(coordinate, new Piece(color, PieceType.KNIGHT));
                break;
            case 'b':
                builder.put(coordinate, new Piece(color, PieceType.BISHOP));
                break;
            case 'q':
                builder.put(coordinate, new Piece(color, PieceType.QUEEN));
                break;
            case 'k':
                builder.put(coordinate, new Piece(color, PieceType.KING));
                break;
            default:
                throw new FenParseException(String.format("Invalid piece token: %s", token));
        }

    }

    private static Map<PieceColor, CastlingRight> parseCastlingAvailability(String fenCastling) {

        Map<PieceColor, CastlingRight> castlingRightMap = new HashMap<>();

        CastlingRight blackCastlingRights = parseCastlingAvailability(fenCastling, "k", "q");

        CastlingRight whiteCastlingRights = parseCastlingAvailability(fenCastling, "K", "Q");

        castlingRightMap.put(PieceColor.BLACK, blackCastlingRights);

        castlingRightMap.put(PieceColor.WHITE, whiteCastlingRights);

        return castlingRightMap;

    }

    private static CastlingRight parseCastlingAvailability(String fenCastling, String kingToken, String queenToken) {

        CastlingRight castlingRightsId = CastlingRight.NONE;

        if (fenCastling.contains(kingToken)) {
            if (fenCastling.contains(queenToken)) {
                castlingRightsId = CastlingRight.BOTH;
            } else {
                castlingRightsId = CastlingRight.KING_SIDE;
            }
        } else if (fenCastling.contains(queenToken)) {
            castlingRightsId = CastlingRight.QUEEN_SIDE;
        }

        return castlingRightsId;

    }

    private static Square parseEnPassentTarget(String square) {

        if (square.length() == 2 && Character.isAlphabetic(square.charAt(0))) {
            // TODO check if valid square
            int fileIndex = getFileIndexOf(square);
            int rankIndex = getRankIndexOf(square);

            return Square.valueOf(fileIndex, rankIndex);
        }

        return null;

    }

    private static int getFileIndexOf(String square) {
        final String files = "abcdefgh";
        return files.indexOf(square.charAt(0));
    }

    private static int getRankIndexOf(String square) {
        return Integer.parseInt("" + square.charAt(1)) - 1;
    }

    private static int to0x88(int fileIndex, int rankIndex) {
        return 16 * rankIndex + fileIndex;
    }


}
