package com.erikmafo.javachess.board;


import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;


/**
 * @author erikf
 */
public class FenParser {

    /*private final static String START_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public PlayableBoard createInstance() {
        try {
            return parse(START_POSITION);
        } catch (FenParseException e) {
            throw new AssertionError(); // should never happen

        }
    }*/

    public Board parse(String fen) throws FenParseException {

        String[] fenParts = fen.split("\\s+");

        if (fenParts.length != 6) {
            throw new FenParseException("FEN string must have 6 parts separated by whitespace.");
        }

        BoardBuilder builder = new BoardBuilder();
        parsePiecePlacement(fenParts[0], builder);
        parseCastlingAvailability(fenParts[2], builder);
        parseEnPassentTarget(fenParts[3], builder);
        return builder.build();
    }


    private static void parsePiecePlacement(String fenPiecePlacement, BoardBuilder builder) throws FenParseException {
        String[] ranks = fenPiecePlacement.split("/");
        for (int i = 0; i < ranks.length; i++) {
            String rank = ranks[i];
            int fileIndex = -1;
            for (int j = 0; j < rank.length(); j++) {
                char token = rank.charAt(j);
                if (Character.isAlphabetic(token)) {
                    fileIndex += 1;
                    putPiece(token, fileIndex, 7 - i, builder);
                } else if (Character.isDigit(token)) {
                    fileIndex += Character.getNumericValue(token); // TODO test
                    // if valid
                    // number
                } else {
                    throw new FenParseException("Invalid character " + token);
                }
            }
        }

    }

    private static void putPiece(char token, int fileIndex, int rankIndex, BoardBuilder builder) throws FenParseException {

        PieceColor color = Character.isUpperCase(token) ? PieceColor.WHITE : PieceColor.BLACK;

        BoardCoordinate coordinate = BoardCoordinate.valueOf(fileIndex, rankIndex);

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

    private static void parseCastlingAvailability(String fenCastling, BoardBuilder builder) {


        CastlingRight blackCastlingRights = parseCastlingAvailability(fenCastling, "k", "q");

        CastlingRight whiteCastlingRights = parseCastlingAvailability(fenCastling, "K", "Q");

        builder.setCastlingRights(PieceColor.BLACK, blackCastlingRights);

        builder.setCastlingRights(PieceColor.WHITE, whiteCastlingRights);

    }

    private static CastlingRight parseCastlingAvailability(String fenCastling, String kingToken, String queenToken) {

        CastlingRight castlingRightsId = CastlingRight.NONE;

        if (fenCastling.contains(kingToken)) {
            if (fenCastling.contains(queenToken)) {
                castlingRightsId = CastlingRight.BOTH;
            } else {
                castlingRightsId = CastlingRight.SHORT;
            }
        } else if (fenCastling.contains(queenToken)) {
            castlingRightsId = CastlingRight.LONG;
        }

        return castlingRightsId;

    }

    private static void parseEnPassentTarget(String square, BoardBuilder builder) {

        if (square.length() == 2 && Character.isAlphabetic(square.charAt(0))) {
            // TODO check if valid square
            int fileIndex = getFileIndexOf(square);
            int rankIndex = getRankIndexOf(square);

            builder.setEnPassentTarget(BoardCoordinate.valueOf(fileIndex, rankIndex));
        }

    }

    private static int getFileIndexOf(String square) {
        final String files = "abcdefgh";
        return files.indexOf(square.charAt(0));
    }

    private static int getRankIndexOf(String square) {
        return Integer.parseInt(" " + square.charAt(1)) - 1;
    }

    private static int to0x88(int fileIndex, int rankIndex) {
        return 16 * rankIndex + fileIndex;
    }


}

