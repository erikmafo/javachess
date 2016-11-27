package com.erikmafo.javachess.board;

/**
 * Created by erikmafo on 27.11.16.
 */
public class Boards {

    private final static String START_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";


    public static Board newBoard() {

        try {
            return newBoardFromFen(START_POSITION);
        } catch (FenParseException ex) {
            // should never occur
            throw new Error(ex);
        }
    }

    public static Board newBoardFromFen(String fen) throws FenParseException {
        return new FenParser().parse(fen);
    }



}
